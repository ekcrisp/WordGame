package edu.bard.wordgame;
import java.util.Random;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;


public class LevelActivity extends Activity {
	LevelView levelView;
	String levelText;
	String spoofText;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level);
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.frame);
		Bundle extras = getIntent().getExtras();
		levelText = extras.getString("level");
		spoofText = extras.getString("fakeLevel");
		levelView = new LevelView(getApplicationContext());
		levelView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				levelView.touchListener(event);
				return false;
			}
		});
		
		relativeLayout.addView(levelView);
	}
	
	private class LevelView extends SurfaceView implements SurfaceHolder.Callback {
		PanelThread _thread;
		Paint blackPaint = new Paint();
		Typeface gameFont = Typeface.create("Helvetica",Typeface.BOLD);
		Paint tickPaint = new Paint();
		Paint whitePaint = new Paint();
		String[] levelTextArray = levelText.split(" ");
		String[] spoofTextArray = spoofText.split(" ");
		
		Boolean gameStarted = false;
		boolean drawRetry = false;
		boolean drawFail = false;
		
		int index = 0;
		
		int yOffset = 0;
		float yOffsetVelocity = 0;
		int tickSpacing = 100;
		int vanishedTicks = 0;
		int[] curTicks = {10,20,30,40,50,60,70,80,90,100};
		
		int[] leftPos = {0,0};
		int[] leftBound = {0,0};
		int[] rightPos = {0,0};
		int[] rightBound = {0,0};
		int[] startPos = {0,0};
		int[] startBound = {0,0};
		int[] vanishingPos = {0,0};
		int[] vanishingBound = {0,0};
		boolean leftCorrect = true;
		float[] vanishingXVals;
		float[] vanishingYVals;
		float[] vanishingXVelocities;
		float[] vanishingYVelocities;
		
		float gravity = 0.15f;
		
		float rightSpeed = 12;
		float rightAngle = 65;
		float rightVelocityX = (float) Math.cos(Math.toRadians(rightAngle)) * rightSpeed;
		float rightVelocityY = (float) Math.sin(Math.toRadians(rightAngle)) * rightSpeed;
		
		float leftSpeed = 12;
		float leftAngle = 115;
		float leftVelocityX = (float) Math.cos(Math.toRadians(leftAngle)) * leftSpeed;
		float leftVelocityY = (float) Math.sin(Math.toRadians(leftAngle)) * leftSpeed;
		
		Random r = new Random();
		
		public LevelView(Context context) {
			super(context);
			getHolder().addCallback(this);
			blackPaint.setColor(Color.BLACK);
			whitePaint.setColor(Color.WHITE);
			blackPaint.setTypeface(gameFont);
			blackPaint.setTextSize(40);
			tickPaint.setTextSize(20);
			for (int i = 0; i<spoofTextArray.length; i++) {
				System.out.println(i + " " + spoofTextArray[i]);
			}

		}
		
		public void touchListener(MotionEvent event) {
			
			if (!gameStarted) {
				if (checkTap(startPos, startBound, event.getX(), event.getY())) {
					
					Rect textBounds = new Rect();
					blackPaint.getTextBounds("START", 0 ,  "START".length(), textBounds);
					updateVanish(true);
					
					if (r.nextInt(2) == 0) {
						leftCorrect = true;
					}
					else {
						leftCorrect = false;
					}

					gameStarted = true;
				}
			}
			else {
				if (checkTap(leftPos, leftBound, event.getX(), event.getY() - yOffset)) {
					if (leftCorrect) {
						launch();
					}
					else {
						endGame(false);
					}
				}
				else if (checkTap(rightPos, rightBound, event.getX(), event.getY() - yOffset)) {
					if (leftCorrect) {
						endGame(false);
					}
					else {
						launch();
					}
				}
			}
		}


		public boolean checkTap(int[] pos, int[] bound, float x, float y) {
						
			if (x + 15 < pos[0]) {
				return false;
			}			
			else if (y + 40 < pos[1]) {
				return false;
			}
			else if (x - 15 > bound[0]) {
				return false;
			}
			else if (y - 40 > bound[1]) {
				return false;
			}	
			else {
				return true;
			}
		}
		
		public void launch() {
			//called when user taps correct word, advancing the game
			updateVanish(false);
			if (index!=levelTextArray.length-1) {
				index++;
			}
			else {
				endGame(true);
				return;
			}	
			if (leftCorrect) {
				startPos[0] = leftPos[0];
				startPos[1] = leftPos[1];
				rightPos[0] = leftPos[0];
				rightPos[1] = leftPos[1];
			}
			else {
				startPos[0] = rightPos[0];
				startPos[1] = rightPos[1];
				leftPos[0] = rightPos[0];
				leftPos[1] = rightPos[1];
			}
			
			//rightSpeed = 10;
			//rightAngle = 70;
			rightVelocityX = (float) Math.cos(Math.toRadians(rightAngle)) * rightSpeed;
			if (rightVelocityY<0)
				rightVelocityY = (float) Math.sin(Math.toRadians(rightAngle)) * rightSpeed;
			else 
				rightVelocityY += (float) Math.sin(Math.toRadians(rightAngle)) * rightSpeed;
			
			//leftSpeed = 10;
			//leftAngle = 110;
			leftVelocityX = (float) Math.cos(Math.toRadians(leftAngle)) * leftSpeed;
			if (leftVelocityY<0)
				leftVelocityY = (float) Math.sin(Math.toRadians(leftAngle)) * leftSpeed;
			else
				leftVelocityY += (float) Math.sin(Math.toRadians(leftAngle)) * leftSpeed;
			
			if (r.nextInt(2) == 0) {
				leftCorrect = true;
			}
			else {
				leftCorrect = false;
			}		
		}
		
		public void endGame(boolean win) {
			/*
			gameStarted = false;
			
			if (win){
				drawRetry = true;
			}
			else {
				drawFail = true;
			}
			*/
			
		}
		
		public void drawTimerBoxes(Canvas canvas) {
			canvas.drawRect( 5, canvas.getHeight() - (canvas.getHeight() / 12), 20, canvas.getHeight() - (canvas.getHeight() / 12) - 15, blackPaint);
			canvas.drawRect( 5, canvas.getHeight() - (canvas.getHeight() / 15), 20, canvas.getHeight() - (canvas.getHeight() / 15) - 15, blackPaint);
			canvas.drawRect( 5, canvas.getHeight() - (canvas.getHeight() / 18), 20, canvas.getHeight() - (canvas.getHeight() / 20) - 15, blackPaint);

		}

		@SuppressLint("NewApi") public void updatePositions(Canvas canvas) {
			canvas.drawColor(Color.LTGRAY);
			canvas.drawRect(0, 0, 2, canvas.getHeight(), blackPaint);
			//drawTimerBoxes(canvas);
			int tickIndex = 9;
			for (int i = 0; i < curTicks.length; i++) {
				canvas.drawRect(2, i*tickSpacing + yOffset - (vanishedTicks * tickSpacing), 6, (i*tickSpacing)+2 + yOffset - (vanishedTicks * tickSpacing), blackPaint);
				canvas.drawText(Integer.toString(curTicks[tickIndex--]), 2 + 20, (i*tickSpacing) + 10 + yOffset - (vanishedTicks * tickSpacing), tickPaint);
			}
			if (yOffset > vanishedTicks*tickSpacing) {		
				for (int i = 0; i<curTicks.length - 1; i++) {
					curTicks[i] += 10;
				}
				vanishedTicks++;
				
			}
			else if (yOffset < (vanishedTicks-1)*tickSpacing) {
				for (int i = 0; i<curTicks.length - 1; i++) {
					curTicks[i] -= 10;
				}
				vanishedTicks--;
				
			}
			
			if (!gameStarted) {
				if (drawRetry) {

					canvas.drawRect((canvas.getWidth()/2) - 200, (canvas.getHeight()/2) - 100, (canvas.getWidth()/2) + 200, (canvas.getHeight()/2) + 100, new Paint(Color.BLACK));
					resetVariables();
				}
				else if (drawFail) {
					canvas.drawRect((canvas.getWidth()/2) - 200, (canvas.getHeight()/2) - 100, (canvas.getWidth()/2) + 200, (canvas.getHeight()/2) + 100, new Paint(Color.RED));
					resetVariables();
				}
				else {
					Rect textBounds = new Rect();
					blackPaint.getTextBounds("START", 0 ,  "START".length(), textBounds);
					startPos[0] = canvas.getWidth()/2 - (textBounds.right/2);
					startBound[0] = startPos[0] + textBounds.right;
					startPos[1] = (canvas.getHeight() - (canvas.getHeight()/12)) + textBounds.top;
					startBound[1] = startPos[1] - textBounds.top;
					leftPos[0] = startPos[0];
					rightPos[0] = startPos[0];
					leftPos[1] = startPos[1];
					rightPos[1] = startPos[1];
					canvas.drawText("START", startPos[0], startBound[1], blackPaint);
				}
				
			}
			else {
				leftPos[0] += leftVelocityX;
				leftPos[1] -= leftVelocityY;
				leftVelocityY -= gravity;
				
				rightPos[0] += rightVelocityX;
				rightPos[1] -= rightVelocityY;
				rightVelocityY -= gravity;
				
				updateBounds();
				
				if (leftPos[0] < 6){
					if (leftVelocityX < 0)
						leftVelocityX *= -.8;
				}
				else if (leftBound[0] > canvas.getWidth() - 3) {
					if (leftVelocityX > 0) {
						leftVelocityX *= -.8;
					}
				}
				if (rightPos[0] < 6) {
					if (rightVelocityX < 0)
						rightVelocityX *= -.8;
				}
				else if (rightBound[0] > canvas.getWidth() - 3) {
					if (rightVelocityX > 0) {
						rightVelocityX *= -.8;
					}
				}
				
				if (leftBound[0] > rightPos[0]) {
					int difference = leftBound[0] - rightPos[0];
					if (rightBound[0] + difference + 15 < canvas.getWidth()) {
						rightPos[0]+=difference + 15;
						rightBound[0]+=difference + 15;
					}
					else if (leftPos[0] - difference - 15 > 0){
						leftPos[0] -= difference - 15;
						leftBound[0] -= difference - 15;
					}
					else {
						rightPos[0]+=difference/2 + 15;
						rightBound[0]+=difference/2 + 15;
						leftPos[0] -= difference/2 - 15;
						leftBound[0] -= difference/2 - 15;
					}
					if (leftVelocityX > 0 ) {
						leftVelocityX *= -1;
					}
					if (rightVelocityX < 0 ) {
						rightVelocityX *= -1;
					}
				}
				
				if (leftCorrect) {
					canvas.drawText(levelTextArray[index], leftPos[0], leftBound[1] + yOffset, blackPaint);
					canvas.drawText(spoofTextArray[index], rightPos[0], rightBound[1] + yOffset, blackPaint);
				}
				else {
					canvas.drawText(spoofTextArray[index], leftPos[0], leftBound[1] + yOffset, blackPaint);
					canvas.drawText(levelTextArray[index], rightPos[0], rightBound[1] + yOffset, blackPaint);
				}

				for (int i = 0; i<vanishingXVals.length; i++) {
					canvas.drawRect(vanishingXVals[i], vanishingYVals[i] + yOffset,vanishingXVals[i] + 2, vanishingYVals[i] + 2  + yOffset, blackPaint);
					vanishingXVals[i] += vanishingXVelocities[i];
					vanishingYVals[i] += vanishingYVelocities[i];
					vanishingYVelocities[i] += gravity;
				}
				if (leftPos[1] + yOffset < canvas.getHeight() / 6) {
					if (leftVelocityY > 0) {
						yOffsetVelocity = (float) (leftVelocityY+0.05);
					}
					else {
						yOffsetVelocity = (float) (leftVelocityY*.8);
					}
				}
				else if  (leftPos[1] + yOffset > canvas.getHeight() - (canvas.getHeight() / 6)) {
					
					if (leftVelocityY > 0) {
						yOffsetVelocity = (float) (leftVelocityY*0.8);
					}
					else {
						yOffsetVelocity = (float) (leftVelocityY-0.5);
					}
				}
				else if (leftPos[1] + yOffset < canvas.getHeight() / 4) {
					if (leftVelocityY > 0) {
						yOffsetVelocity = (float) (leftVelocityY * 0.97);
					}
					else {
						yOffsetVelocity = (float) (leftVelocityY*.85);
					}
					
				}
				else if  (leftPos[1] + yOffset > canvas.getHeight() - (canvas.getHeight() / 5)) {
					yOffsetVelocity = (float) (leftVelocityY * 0.975);
				}
				else if (leftPos[1] + yOffset < canvas.getHeight() / 3) {
					
					if (leftVelocityY > 0) {
						yOffsetVelocity = (float) (leftVelocityY * 0.925);
					}
					else {
						yOffsetVelocity = (float) (leftVelocityY*.9);
					}
					
				}
				else if  (leftPos[1] + yOffset > canvas.getHeight() - (canvas.getHeight() / 4)) {
					yOffsetVelocity = (float) (leftVelocityY * 0.925);
				}
				else if (leftPos[1] + yOffset < canvas.getHeight() / 2) {
					yOffsetVelocity = (float) (leftVelocityY * 0.85);
				}
				else if  (leftPos[1] + yOffset > canvas.getHeight() - (canvas.getHeight() / 3)) {
					yOffsetVelocity = (float) (leftVelocityY * 0.85);
				}
				else if (leftPos[1] + yOffset < canvas.getHeight() / 1.5) {
					yOffsetVelocity = (float) (leftVelocityY * 0.8);
				}
				else if  (leftPos[1] + yOffset > canvas.getHeight() - (canvas.getHeight() / 2)) {
					yOffsetVelocity = (float) (leftVelocityY * 0.8);
				}
				else {
					yOffsetVelocity = (float) (leftVelocityY * 0.7);
				}
				yOffset += yOffsetVelocity;
				/*
				float factor = (float)(((leftPos[1] + yOffset - (canvas.getHeight()/2.0)) / canvas.getHeight()) * 2.0);
				Log.i("fact", " " + factor);
				
				yOffsetVelocity = leftVelocityY * factor;
				/*
				 * 
				 */
				
				canvas.drawText("Height: " + (((9*tickSpacing) - (yOffset + leftPos[1])) + curTicks[0]), 540,1000, tickPaint);
			}
			
		}
		
		private void resetVariables() {
			 gameStarted = false;
			drawRetry = false;
			drawFail = false;
			
			 index = 0;
			
			yOffset = 0;
			yOffsetVelocity = 0;
			tickSpacing = 100;
			vanishedTicks = 0;
			for (int i = 1; i<=10; i++) {
				curTicks[i] = i*10;
			}
			
			leftPos[0] = 0;
			leftPos[1] = 0;
			leftBound[0] = 0;
			leftBound[1] = 0;
			rightPos[0] = 0;
			rightPos[1] = 0;
			rightBound[0] = 0;
			rightBound[1] = 0;
			startPos[0] = 0;
			startPos[1] = 0;
			startBound[0] = 0;
			startBound[1] = 0;
			vanishingPos[0] = 0;
			vanishingPos[1] = 0;
			vanishingBound[0] = 0;
			vanishingBound[1] = 0;
			leftCorrect = true;
			
			gravity = 0.15f;
			
			 rightSpeed = 12;
			 rightAngle = 65;
			rightVelocityX = (float) Math.cos(Math.toRadians(rightAngle)) * rightSpeed;
			 rightVelocityY = (float) Math.sin(Math.toRadians(rightAngle)) * rightSpeed;
			
			leftSpeed = 12;
			 leftAngle = 115;
			 leftVelocityX = (float) Math.cos(Math.toRadians(leftAngle)) * leftSpeed;
			 leftVelocityY = (float) Math.sin(Math.toRadians(leftAngle)) * leftSpeed;
			
			r = new Random();
		}

		public void updateBounds() {
			Rect textBounds = new Rect();
			blackPaint.getTextBounds(levelTextArray[index], 0 ,  levelTextArray[index].length(), textBounds);
			if (leftCorrect) {
				leftBound[0] = leftPos[0] + textBounds.right;
				leftBound[1] = leftPos[1] - textBounds.top;
				
			}
			else {
				rightBound[0] = rightPos[0] + textBounds.right;
				rightBound[1] = rightPos[1] - textBounds.top;
			}
			
			blackPaint.getTextBounds(spoofTextArray[index], 0 ,  spoofTextArray[index].length(), textBounds);
			if (leftCorrect) {
				rightBound[0] = rightPos[0] + textBounds.right;
				rightBound[1] = rightPos[1] - textBounds.top;
			}
			else {
				leftBound[0] = leftPos[0] + textBounds.right;
				leftBound[1] = leftPos[1] - textBounds.top;
			}
			
		}
		
		public void updateVanish(boolean firstUpdate) {
			//adds vanishing effect to inactive words
			if (firstUpdate) {
				
				vanishingPos[0] = startPos[0];
				vanishingPos[1] = startPos[1];
				vanishingBound[0] = startBound[0];
				vanishingBound[1] = startBound[1];
				int xLen = vanishingBound[0] - vanishingPos[0];
				int yLen = vanishingBound[1] - vanishingPos[1];
				vanishingXVals = new float[(xLen * yLen)/ 20];
				vanishingYVals = new float[(xLen * yLen)/ 20];
				vanishingXVelocities = new float[(xLen * yLen)/ 20];
				vanishingYVelocities = new float[(xLen * yLen)/ 20];
				
				for (int i = 0; i<vanishingXVals.length; i++) {
					vanishingXVals[i] = leftPos[0] + i;
					vanishingYVals[i] = leftPos[1] + ((int) (yLen * Math.random()));
					vanishingXVelocities[i] = (float) ((6 * Math.random()) - 3);
					vanishingYVelocities[i] = (float) ((6 * Math.random()) - 3);
				}
				
				
				return;
			}
			if (leftCorrect) {
				vanishingPos[0] = rightPos[0];
				vanishingPos[1] = rightPos[1];
				vanishingBound[0] = rightBound[0];
				vanishingBound[1] = rightBound[1];
				int xLen = vanishingBound[0] - vanishingPos[0];
				int yLen = vanishingBound[1] - vanishingPos[1];
				vanishingXVals = new float[(xLen * yLen)/ 20];
				vanishingYVals = new float[(xLen * yLen)/ 20];
				vanishingXVelocities = new float[(xLen * yLen)/ 20];
				vanishingYVelocities = new float[(xLen * yLen)/ 20];
				
				for (int i = 0; i<vanishingXVals.length; i++) {
					vanishingXVals[i] = rightPos[0] + i;
					vanishingYVals[i] = rightPos[1] + ((int) (yLen * Math.random()));
					vanishingXVelocities[i] = (float) ((6 * Math.random()) - 3);
					vanishingYVelocities[i] = (float) ((6 * Math.random()) - 3);
				}
				
				
			}
			else {
				vanishingPos[0] = leftPos[0];
				vanishingPos[1] = leftPos[1];
				vanishingBound[0] = leftBound[0];
				vanishingBound[1] = leftBound[1];
				int xLen = vanishingBound[0] - vanishingPos[0];
				int yLen = vanishingBound[1] - vanishingPos[1];
				vanishingXVals = new float[(xLen * yLen)/ 20];
				vanishingYVals = new float[(xLen * yLen)/ 20];
				vanishingXVelocities = new float[(xLen * yLen)/ 20];
				vanishingYVelocities = new float[(xLen * yLen)/ 20];
				
				for (int i = 0; i<vanishingXVals.length; i++) {
					vanishingXVals[i] = leftPos[0] + i;
					vanishingYVals[i] = leftPos[1] + ((int) (yLen * Math.random()));
					vanishingXVelocities[i] = (float) ((6 * Math.random()) - 3);
					vanishingYVelocities[i] = (float) ((6 * Math.random()) - 3);
				}
				
			}
		}
		
		@Override
		public void onDraw(Canvas canvas) {
			
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			                           //onDraw()
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			setWillNotDraw(false); //Allows us to use invalidate() to call onDraw()


		     PanelThread _thread = new PanelThread(getHolder(), this); //Start the thread that
		        _thread.setRunning(true);                     //will make calls to 
		        _thread.start();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			try {
	            _thread.setRunning(false);                //Tells thread to stop
	     _thread.join();                           //Removes thread from mem.
	 } catch (InterruptedException e) {}
			
		}
		class PanelThread extends Thread {
	        private SurfaceHolder _surfaceHolder;
	        private LevelView _panel;
	        private boolean _run = false;


	        public PanelThread(SurfaceHolder surfaceHolder, LevelView panel) {
	            _surfaceHolder = surfaceHolder;
	            _panel = panel;
	        }


	        public void setRunning(boolean run) { //Allow us to stop the thread
	            _run = run;
	        }


	        @Override
	        public void run() {
	            Canvas c;
	            while (_run) {     //When setRunning(false) occurs, _run is 
	                c = null;      //set to false and loop ends, stopping thread


	                try {


	                    c = _surfaceHolder.lockCanvas(null);
	                    synchronized (_surfaceHolder) {


	                     //Insert methods to modify positions of items in onDraw()
	                     updatePositions(c);


	                    }
	  } finally {
	                    if (c != null) {
	                        _surfaceHolder.unlockCanvasAndPost(c);
	                    }
	                }
	            }
	        }
	    }
		
	}

	
}
