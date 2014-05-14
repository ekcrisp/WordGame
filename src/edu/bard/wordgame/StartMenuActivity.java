package edu.bard.wordgame;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class StartMenuActivity extends Activity {
	
	Button newGame;
	Button createLevel;
	Button settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_menu);
		
		newGame = (Button) findViewById(R.id.newGameButton);
		createLevel = (Button) findViewById(R.id.createLevelButton);
		settings = (Button) findViewById(R.id.settingsButton);

		newGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent =  new Intent(v.getContext(), StartGameActivity.class);
				startActivity(intent);
				
			}
			
		});

		createLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent =  new Intent(v.getContext(), CreateLevelActivity.class);
				startActivity(intent);
				
			}
			
		});
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				
			}
			
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_game, menu);
		return true;
	}

}
