package edu.bard.wordgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EditSingleLevelActivity extends Activity{

	private DBAdapter dbAdapter;
	EditText titleHolder, levelHolder;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_level_item);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		titleHolder = (EditText) findViewById(R.id.editLevelTitle);
		levelHolder = (EditText) findViewById(R.id.editLevelPreview);
		
		//if this causes problems on new level, just check boolean extra
		Bundle extra = getIntent().getExtras();
		if(!extra.getBoolean("newGamePressed")){
			
			CharSequence levelText = (CharSequence) extra.get("level");
			CharSequence title = (CharSequence) extra.get("title");
			
			titleHolder.setText(title);
			levelHolder.setText(levelText);
			
		}

	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		//check for duplicates
		
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
}
