package com.ug3.selp.timetableapp.adapter;

import java.util.List;

import com.ug3.selp.timetableapp.R;
import com.ug3.selp.timetableapp.models.Lecture;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TimetableArrayAdapter extends ArrayAdapter<Lecture> {
	
	private final Context context;
	private List<Lecture> lectures;

	public TimetableArrayAdapter(Context context, int resource, List<Lecture> objects) {
		super(context, resource, objects);
		this.context = context;
		this.lectures = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) 
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.timetable_row, parent, false);
		
		return rowView;
	}
	

}
