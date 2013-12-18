package ug3.selp.timetable.adapters;

import java.util.List;

import ug3.selp.timetable.R;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Lecture;
import ug3.selp.timetable.models.Venue;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailLectureAdapter extends ArrayAdapter<Lecture> {
	
	private final Context context;
	private final List<Lecture> lectures;
	private DatabaseHelper db;

	public DetailLectureAdapter(Context context, int resource, List<Lecture> objects) {
		super(context, resource, objects);
		
		this.context = context;
		this.lectures = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.detail_lecture_row, parent, false);		
			
		// Integrity check
		final Lecture lecture = lectures.get(position);
		if (lecture == null)
			return rowView;
		
		// Get db access
		db = new DatabaseHelper(getContext());
		
		// Find Venue
		final Venue venue = db.getVenueByName(lecture.getVenue().getBuilding());
		String venueName = (venue != null) ? venue.getDescription() : lecture.getVenue().getBuilding();
		
		
		// Find views to assign to them
		TextView startTime = (TextView) rowView.findViewById(R.id.lectureRowStartTime);
		TextView finishTime = (TextView) rowView.findViewById(R.id.lectureRowFinishTime);
		TextView location = (TextView) rowView.findViewById(R.id.lectureRowLocation);
		TextView description = (TextView) rowView.findViewById(R.id.lectureRowDescription);
		
		// Assign text to the views
		startTime.setText(lecture.getTime().getStart());
		finishTime.setText(lecture.getTime().getFinish());
		location.setText(lecture.getVenue().getRoom() + " " + venueName);
		description.setText(lecture.getComment());
		
		ImageView directions = (ImageView) rowView.findViewById(R.id.detailRowDirections);
		// click on directions button
		directions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (venue != null)
					openMap(venue.getDescription());
			}
		});
		
		// Click on row
		rowView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openURL(venue.getMap());
			}
		});
		db.close();
		return rowView;
	}
	
	// Attempt to open a map as an intent
	// If Google Maps installed, it is possible to open with them
	// Otherwise gets opened by the browser
	private void openMap(String building) {
		String[] tokens = building.split(" ");
		String location = android.text.TextUtils.join("+", tokens);
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, 
			Uri.parse("http://maps.google.com/maps?q=" + location));
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	// Auxiliary to open a link in the browser
	private void openURL(String url) {
		Uri uri;
		try {
			uri = Uri.parse(url);
		} catch (Exception e) {
			return;
		}
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		getContext().startActivity(browserIntent);
	}

}
