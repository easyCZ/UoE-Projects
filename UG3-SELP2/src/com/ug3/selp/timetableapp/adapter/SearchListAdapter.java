package com.ug3.selp.timetableapp.adapter;

import java.util.List;
import java.util.Locale;

import com.ug3.selp.timetableapp.R;
import com.ug3.selp.timetableapp.Resources;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchListAdapter extends ArrayAdapter<Course> {
	
	private final String TAG = "SearchListAdapter";
	
	private Context context;
	private List<Course> courses;
	private int resource;
	private DatabaseHelper db;

	public SearchListAdapter(Context context, int resource, List<Course> objects) {
		super(context, resource, objects);
		this.context = context;
		this.courses = objects;
		this.resource = resource;
		this.db = new DatabaseHelper(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = 
			(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		try {
			View rowView = inflater.inflate(resource, parent, false);
			final Course c = courses.get(position);
			String desc = String.format(Locale.ENGLISH,
				"%s | %s | Year %d",
				c.getLecturer(), c.getSemester(), c.getYear());
			
			setText(rowView, R.id.searchListRowTitle, c.getAcronym() +" - " + c.getName());
			setText(rowView, R.id.searchListRowDescription, desc);
			
			rowView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Resources.FRAGMENT_BUNDLE_KEY);
					intent.putExtra(
						Resources.FRAGMENT_ACTION_KEY, Resources.FRAGMENT_ACTION_DETAIL);
					intent.putExtra(Resources.FRAGMENT_ACRONYM_KEY, c.getAcronym());
					context.sendBroadcast(intent);
				}
			});
			db.close();
			return rowView;
		} catch(Exception e) {
			Log.d(TAG, "Fields could not be set. Returning null");
			return null;
		}
		
	}
	
	// Aux to set field
	private void setText(View view, int id, String text) {
		((TextView) view.findViewById(id)).setText(text);
	}
	
}
