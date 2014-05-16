/*
 * Main activity. Menu from which you select to start a new game, create a new level, or change the settings.
 */

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
	Button editLevel;
	Button settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_menu);
		
		newGame = (Button) findViewById(R.id.newGameButton);
		editLevel = (Button) findViewById(R.id.editLevelButton);
		settings = (Button) findViewById(R.id.settingsButton);

		newGame.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent =  new Intent(v.getContext(), StartGameActivity.class);
				startActivity(intent);
				
			}
			
		});

		editLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent =  new Intent(v.getContext(), EditLevelsActivity.class);
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
