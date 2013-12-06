package com.ug3.selp.timetableapp;

import java.util.List;

import com.ug3.selp.timetableapp.models.Course;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DrawerArrayAdapter extends ArrayAdapter<Course> {
	
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
		
		
		return rowView;
	}
	

}
