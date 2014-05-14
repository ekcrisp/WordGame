package edu.bard.wordgame;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class EditSingleLevelActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_level_item);
		
		Bundle extra = getIntent().getExtras();
		CharSequence levelText = (String) extra.get("level");
		CharSequence title = (String) extra.get("title");
		
		EditText titleHolder = (EditText) findViewById(R.id.editLevelTitle);
		EditText levelHolder = (EditText) findViewById(R.id.editLevelPreview);
		
		titleHolder.setText(title);
		levelHolder.setText(levelText);
	}
}
