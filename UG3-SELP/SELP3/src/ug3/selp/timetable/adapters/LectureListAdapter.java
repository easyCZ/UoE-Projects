package ug3.selp.timetable.adapters;

import java.util.List;

import ug3.selp.timetable.DetailActivity;
import ug3.selp.timetable.R;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Course;
import ug3.selp.timetable.models.Lecture;
import ug3.selp.timetable.models.Venue;
import ug3.selp.timetable.service.Resources;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LectureListAdapter extends ArrayAdapter<Lecture> {

	private final String TAG = "LectureListAdapter";

	private final Context context;
	private final List<Lecture> lectures;
	private DatabaseHelper db;

	public LectureListAdapter(Context context, int resource, List<Lecture> objects) {
		super(context, resource, objects);

		this.context = context;
		this.lectures = objects;
		Log.d(TAG, "There are " + objects.size() + " lecture objects.");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.lecture_row_odd, parent, false);		

		// Change background for every other
		if (position % 2 == 0) {
			LinearLayout container = 
					(LinearLayout) rowView.findViewById(R.id.lectureRowContainer);
			container.setBackgroundColor(
					container.getResources().getColor(R.color.lectureRowEvenBG));
		}

		// Integrity check
		final Lecture lecture = lectures.get(position);
		if (lecture == null)
			return rowView;

		// Get db access
		db = new DatabaseHelper(getContext());
		// Find course
		Course course = db.getCourseByAcronym(lecture.getCourse());
		String courseName = (course != null) ? course.getName() : "";

		// Find Venue
		Venue venue = db.getVenueByName(lecture.getVenue().getBuilding());
		String venueName = (venue != null) ? venue.getDescription() : lecture.getVenue().getBuilding();


		// Find views to assign to them
		TextView startTime = (TextView) rowView.findViewById(R.id.lectureRowStartTime);
		TextView finishTime = (TextView) rowView.findViewById(R.id.lectureRowFinishTime);
		TextView courseNameTV = (TextView) rowView.findViewById(R.id.lectureRowTitle);
		TextView location = (TextView) rowView.findViewById(R.id.lectureRowLocation);
		TextView description = (TextView) rowView.findViewById(R.id.lectureRowDescription);

		// Set text for views
		startTime.setText(lecture.getTime().getStart());
		finishTime.setText(lecture.getTime().getFinish());
		courseNameTV.setText(lecture.getCourse() + " - " + courseName);
		location.setText(lecture.getVenue().getRoom() + " " + venueName);
		description.setText(lecture.getComment());

		// Clicking row should redirect to the detail view
		rowView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Start Detail view and pass in Course name
				Intent intent = new Intent(getContext(), DetailActivity.class);
				intent.putExtra(Resources.INTENT_EXTRA_COURSE_NAME, lecture.getCourse());
				context.startActivity(intent);	
			}
		});
		db.close();
		return rowView;
	}

}
