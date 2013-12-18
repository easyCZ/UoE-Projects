package ug3.selp.timetable.adapters;

import java.util.List;

import ug3.selp.timetable.DetailActivity;
import ug3.selp.timetable.R;
import ug3.selp.timetable.models.Course;
import ug3.selp.timetable.service.Resources;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchArrayAdapter extends ArrayAdapter<Course> {

	private final String TAG = "SearchArrayAdapter";

	private List<Course> courses;
	private Context context;

	public SearchArrayAdapter(Context context, int resource, List<Course> objects) {
		super(context, resource, objects);

		this.courses = objects;
		this.context = context;
		Log.d(TAG, "There are " + objects.size() + " course objects initially.");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View rowView = inflater.inflate(R.layout.search_row, parent, false);

		// Retrieve the right course
		final Course course = courses.get(position);
		if (course == null)
			return rowView;

		// Get View objects to assign to
		TextView semester = (TextView)rowView.findViewById(R.id.courseRowSemester);
		TextView courseName = (TextView)rowView.findViewById(R.id.courseRowTitle);
		TextView lecturer = (TextView) rowView.findViewById(R.id.courseRowLecturer);

		// Assign text
		semester.setText(course.getSemester());
		courseName.setText(course.getAcronym() + " - " + course.getName());
		lecturer.setText(course.getLecturer());

		// Clicking each should bring up the detail view
		rowView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), DetailActivity.class);
				intent.putExtra(Resources.INTENT_EXTRA_COURSE_NAME, course.getAcronym());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);				
			}
		});

		return rowView;
	}



}
