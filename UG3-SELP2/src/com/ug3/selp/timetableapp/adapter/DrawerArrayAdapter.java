package com.ug3.selp.timetableapp.adapter;

import java.util.List;

import com.ug3.selp.timetableapp.R;
import com.ug3.selp.timetableapp.R.drawable;
import com.ug3.selp.timetableapp.R.id;
import com.ug3.selp.timetableapp.R.layout;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerArrayAdapter extends ArrayAdapter<Course> {
	
	private final String TAG = "DrawerArrayAdapter";
	
	private final Context context;
	private final List<Course> courses;

	public DrawerArrayAdapter(Context context, int resource, List<Course> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.courses = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.sidebar_drawer, parent, false);
		
		// Modify data
		TextView headline = (TextView) rowView.findViewById(R.id.sidebarDrawerTitle);
		headline.setText(courses.get(position).getAcronym() + " - " + courses.get(position).getName());
		TextView lecturer = (TextView) rowView.findViewById(R.id.sidebarDrawerLecturer);
		lecturer.setText(courses.get(position).getLecturer());
		
		TextView semester = (TextView) rowView.findViewById(R.id.sidebarDrawerSemester);
		semester.setText(courses.get(position).getSemester());
		
		TextView year = (TextView) rowView.findViewById(R.id.sidebarDrawerYearText);
		year.setText("Y" + courses.get(position).getYear());
		
		TextView degree = (TextView) rowView.findViewById(R.id.sidebarDrawerDegreeText);
		Log.d(TAG, "degree: " + DatabaseHelper.listToString(courses.get(position).getDegree()));
		degree.setText(DatabaseHelper.listToString(courses.get(position).getDegree()));
		
		ImageView img = (ImageView) rowView.findViewById(R.id.sidebarIcon);
		if (courses.get(position).isTracked()) {
			img.setImageResource(android.R.color.transparent);
			img.setBackgroundResource(R.drawable.checkbox_checked);
		} else {
			img.setImageResource(android.R.color.transparent);
    		img.setBackgroundResource(R.drawable.checkbox_unchecked);
		}
		
		return rowView;
	}
	

}
