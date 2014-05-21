package edu.bard.wordgame;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditSingleLevelActivity extends Activity{

	ArrayList<LevelItem> levels;
	LevelAdapter levelAdapter;
	private DBAdapter dbAdapter;
	EditText titleHolder, levelHolder;
	private String title;
	Button deleteButton;
	protected Cursor m_cursor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_level_item);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		titleHolder = (EditText) findViewById(R.id.editLevelTitle);
		levelHolder = (EditText) findViewById(R.id.editLevelPreview);
		deleteButton = (Button) findViewById(R.id.deleteButton);
		Bundle extra = getIntent().getExtras();
		title = extra.getString("title");
		
		deleteButton.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View view) {
				
				try{
					dbAdapter.removeTask(title);
					finish();
				}catch (SQLException e){
					titleHolder.setText("Cannot remove Activity!");
				}
			}			
		});
		

		//if this causes problems on new level, just check boolean extra
		if(!extra.getBoolean("newGamePressed")){
			
			CharSequence levelText = (CharSequence) extra.get("level");
			CharSequence title = (CharSequence) extra.get("title");
			
			titleHolder.setText(title);
			levelHolder.setText(levelText);
			
		}
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		Bundle extra = getIntent().getExtras();
		if(extra.getBoolean("newGamePressed") == true){
			//TODO: make spooftext viable
			dbAdapter.insertItem(new LevelItem(	titleHolder.getText().toString(), 
												levelHolder.getText().toString(),
												levelHolder.getText().toString()));
		}
		
		else{
			//TODO: make spooftext viable
			dbAdapter.replaceItem(
					new LevelItem(	extra.getString("title"), 
									extra.getString("level"), 
									extra.getString("level")),
									
					new LevelItem(  titleHolder.getText().toString(), 
									levelHolder.getText().toString(),
									levelHolder.getText().toString()));
		}
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
