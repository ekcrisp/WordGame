package edu.bard.wordgame;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class CreateLevelActivity extends Activity {
	
	ArrayList<String> levels;
	LevelAdapter levelAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_game);
		/*
		levels = new ArrayList<String>();
		levelAdapter = new LevelAdapter(this, R.layout.activity_level, levels);
		levels.add("Sample Level 1");
		levels.add("Sample Level 2");
		levelAdapter.notifyDataSetChanged();
		
		ListView myListView = (ListView)findViewById(R.id.myListView);
		myListView.setAdapter(levelAdapter);
		*/
		
	}
}
