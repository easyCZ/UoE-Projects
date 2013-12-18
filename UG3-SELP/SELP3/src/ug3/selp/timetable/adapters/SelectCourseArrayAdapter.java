package ug3.selp.timetable.adapters;

import java.util.List;

import ug3.selp.timetable.R;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Course;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class SelectCourseArrayAdapter extends ArrayAdapter<Course> {
	
	private List<Course> courses;
	private Context context;
	private DatabaseHelper db;

	public SelectCourseArrayAdapter(Context context, int resource, List<Course> objects) {
		super(context, resource, objects);
		init();
		
		this.courses = objects;
		this.context = context;
	}
	
	// Auxiliary
	private void init() {
		db = new DatabaseHelper(getContext());		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.course_day_row, parent, false);
		
		// Get course
		final Course course = courses.get(position);
		if (course == null)
			return rowView;
		
		// Set checkbox
		CheckBox check = (CheckBox) rowView.findViewById(R.id.courseRowCheckBox);
		check.setChecked(course.isTracked());
		setRowBG(rowView, check.isChecked());
		
		// Modify values
		TextView semester = (TextView)rowView.findViewById(R.id.courseRowSemester);
		TextView courseName = (TextView)rowView.findViewById(R.id.courseRowTitle);
		TextView lecturer = (TextView) rowView.findViewById(R.id.courseRowLecturer);
		
		semester.setText(course.getSemester());
		courseName.setText(course.getAcronym() + " - " + course.getName());
		lecturer.setText(course.getLecturer());
		
		// Update database as selected on checkbox click
		check.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox check = (CheckBox) v;
				rowView.setBackgroundColor(context.getResources().getColor(R.color.white));
				
				setRowBG(rowView, check.isChecked());
				
				// Write change to db
				course.setTracked(check.isChecked());
				if (db == null)
					init();
				db.updateCourse(course);
				db.close();
			}
		});
		
		// Modified listener, updates checkbox too.
		rowView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CheckBox check = (CheckBox) v.findViewById(R.id.courseRowCheckBox);
				v.setBackgroundColor(context.getResources().getColor(R.color.white));
				
				if (check.isChecked()) 
					check.setChecked(false);	// Uncheck - set unTracked
				else 
					check.setChecked(true);	// Check - set Tracked
					
				setRowBG(rowView, check.isChecked());	// Change row bg
				
				// Write change to db
				course.setTracked(check.isChecked());
				if (db == null)
					init();
				
				db.updateCourse(course);
				db.close();
			}
		});
		db.close();
		return rowView;
	}
	
	// Auxiliary to change the background
	private void setRowBG(View v, boolean isChecked) {
		if (isChecked)
			v.setBackgroundColor(context.getResources().getColor(R.color.selectCourseSelected));
		else
			v.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
	}

}
