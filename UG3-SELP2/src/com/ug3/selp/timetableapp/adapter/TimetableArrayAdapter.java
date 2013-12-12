package com.ug3.selp.timetableapp.adapter;

import java.util.List;

import com.ug3.selp.timetableapp.R;
import com.ug3.selp.timetableapp.Resources;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.Lecture;
import com.ug3.selp.timetableapp.models.Venue;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author s1115104
 *
 * Adapter for Lectures by day
 */
public class TimetableArrayAdapter extends ArrayAdapter<Lecture> {
	
	private final String TAG = "TimetableArrayAdapter";
	
	private Context context;
	private List<Lecture> lectures;
	private DatabaseHelper db;
	private int resource;

	public TimetableArrayAdapter(Context context, int resource, List<Lecture> objects) {
		super(context, resource, objects);
		this.context = context;
		this.lectures = objects;
		this.resource = resource;
		this.db = new DatabaseHelper(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(resource, parent, false);
		
		// db lookup & error handling
		final Course course = db.getCourseByAcronym(lectures.get(position).getCourse());
		if (course == null) {
			Log.d(TAG, "Course could not be retrieved from the DB. Lookup param were Acronym = " +
					lectures.get(position).getCourse());
		}
			String courseName = (course != null) ? course.getName() : "";
	
		Venue venue = db.getVenueByName(lectures.get(position).getVenue().getBuilding());
		if (venue == null) {
			Log.d(TAG, "Venue could not be found. Lookup were Building = " + lectures.get(position).getVenue().getBuilding());
		}
		String venueName = (venue != null) ? venue.getDescription() : lectures.get(position).getVenue().getBuilding();
		
		// Set view values
		setText(rowView, R.id.timetableRowTimeStart, lectures.get(position).getTime().getStart());
		setText(rowView, R.id.timetableRowTimeFinish, lectures.get(position).getTime().getFinish());
		setText(rowView, R.id.timetableRowDetailsTitle, 
			lectures.get(position).getCourse() + " - " + courseName);
		setText(
			rowView, R.id.timetableRowDetailsLocation, 
			lectures.get(position).getVenue().getRoom() + " " + venueName);
		setText(rowView, R.id.timetableRowDescription, 
				lectures.get(position).getComment());
		
		final int finalPos = position;
		
		// Attach listener
		rowView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {	
				Log.d(TAG, lectures.get(finalPos).toString());
				// Broadcast click event
				Intent intent = new Intent(Resources.FRAGMENT_BUNDLE_KEY);
				intent.putExtra(
					Resources.FRAGMENT_ACTION_KEY, Resources.FRAGMENT_ACTION_DETAIL);
				intent.putExtra(Resources.FRAGMENT_ACRONYM_KEY, lectures.get(finalPos).getCourse());
				context.sendBroadcast(intent);
			}
		});
		db.close();
		return rowView;
	}
	
	// Convenience setter
	private void setText(View view, int resource, String value) {
		try {
			TextView tv = (TextView) view.findViewById(resource);
			tv.setText(value);
		} catch(ClassCastException e) {
			Log.d(TAG, "TextView cast unable.");
		}
	}
	
	
	
	
	

}
