package edu.bard.wordgame;

import java.util.Date;
import java.util.List;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LevelAdapter extends ArrayAdapter<LevelItem> {
	
	int resource;
	
	public LevelAdapter(Context context, int resource, List<LevelItem> items) {
		super(context, resource, items);
		this.resource = resource;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		RelativeLayout todoView;
		
		LevelItem item = getItem(position); // get data from the item at position
		String titleString = item.getTitle();
		String textPreviewString = item.getLevelText();

		if (convertView == null) {
			todoView = new RelativeLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li = (LayoutInflater)getContext().getSystemService(inflater);
			li.inflate(R.layout.view_level_item,todoView,true);
		} else {
			todoView = (RelativeLayout) convertView;
		}

		TextView titleView = (TextView)todoView.findViewById(R.id.title);
		TextView textPreviewView = (TextView)todoView.findViewById(R.id.levelPreview);

		titleView.setText(titleString);
		textPreviewView.setText(textPreviewString);
		
		return todoView;
	}

}
