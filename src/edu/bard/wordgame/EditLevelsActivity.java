/*
 * Opens up listview from database of all levels, allows you to edit them.
 */
package edu.bard.wordgame;

import java.text.ParseException;
import java.util.ArrayList;

import edu.bard.wordgame.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class EditLevelsActivity extends Activity{

	ArrayList<LevelItem> levels;
	LevelAdapter levelAdapter;
	Button newGame; 
	DBAdapter dbAdapter;
	protected Cursor m_cursor;
	ListView myListView;
	
	@Override
	protected void onResume() {
		super.onResume();
		levels.clear();
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		populateFromDB();	
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_edit_level_list);
		
		levels = new ArrayList<LevelItem>();
		levelAdapter = new LevelAdapter(this, R.layout.activity_level, levels);
		levels.add(new LevelItem("Sample Level 1", "Sample Level Text Numero Uno", "Sumple Loovel Tuxt Nummer Una"));
		levels.add(new LevelItem("Sample Level 2", "Sample Level Text Numero Dos", "Sumple Loovel Tuxt Nummer Dna"));
		levels.add(new LevelItem("Sgt Peppers", "It was twenty years ago today Sgt. Pepper told the band to play They've been going in and out of style But they're guaranteed to raise a smile So may I introduce to you The act you've known for all these years Sgt. Pepper's Lonely Hearts Club Band", "At were thirty days ego ahora Col. Piper showed thee guys ta jam They're bin getting up or in a smile Bat they've guarantee too rise it style And might we introduce too ewe Tha ace you'll got four many those days Col. Pipper's Worried Heats Crab Bond"));
		
		myListView = (ListView)findViewById(R.id.editTextListView);
		myListView.setOnItemClickListener(new ListView.OnItemClickListener() { 
			@Override 
			public void onItemClick( 
					AdapterView <? > parent, View view, int position, long duration) { 
					LevelItem item = (LevelItem) EditLevelsActivity.this.levelAdapter.getItem( position);
					Intent intent = new Intent(view.getContext() , EditSingleLevelActivity.class);
					
					intent.putExtra("ITEM_POSITION", levels.indexOf(item));
					intent.putExtra("level", item.getLevelText());
					intent.putExtra("title", item.getTitle());
					intent.putExtra("newGamePressed", false);
					startActivity(intent);

				}
			});
		
		myListView.setAdapter(levelAdapter);
		
		newGame = (Button)findViewById(R.id.newLevelButton);
		newGame.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View view) {
				
				Intent intent = new Intent(view.getContext() , EditSingleLevelActivity.class);
				
				intent.putExtra("newGamePressed", true);
				startActivity(intent);
			}
			
		});
		
	}
	
	private void populateFromDB(){
		m_cursor = dbAdapter.getAllItems();
		m_cursor.moveToFirst();
		if(m_cursor.getCount()!=0){
			do {
				levels.add(new LevelItem(
						m_cursor.getString(DBAdapter.COL_TITLE), 
						m_cursor.getString(DBAdapter.COL_LEVELTEXT), 			
						m_cursor.getString(DBAdapter.COL_SPOOFTEXT)));
			}while (m_cursor.moveToNext());
		}
		levelAdapter.notifyDataSetChanged();
		m_cursor.close();
	}
}
