package edu.bard.wordgame;

import android.content.Intent;

public class LevelItem {
	protected String title;
	protected String levelText;
	protected String fakeLevelText;
	
	
	public LevelItem(String t, String l, String fl) {
		title = t;
		levelText = l;
		fakeLevelText = fl;
	}
	
	public void setTitle(String t) {
		title = t;
	}
	public void setLevelText(String l) {
		levelText = l;
	}
	public void setFakeLevelText(String l) {
		fakeLevelText = l;
	}
	public String getTitle() {
		return title;
	}
	public String getLevelText() {
		return levelText;
	}
	public void addDataToIntent(Intent intent) {
		//dataToIntent(intent,task,status);
	}

	public String getFakeLevelText() {
		return fakeLevelText;
	}
	
}
