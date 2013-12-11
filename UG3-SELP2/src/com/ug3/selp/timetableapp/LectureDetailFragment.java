package com.ug3.selp.timetableapp;

import java.util.List;

import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.Lecture;
import com.ug3.selp.timetableapp.models.Venue;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LectureDetailFragment extends Fragment {
	
	private final String TAG = "LectureDetailFragment";
	private DatabaseHelper db;
	
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Bundle bundle = this.getArguments();
		if (bundle == null) {
			super.onDestroy();
			return null;
		}
		String courseAcr = bundle.getString(Resources.FRAGMENT_ACRONYM_KEY);
		db = new DatabaseHelper(getActivity());
		
		final Course course = db.getCourseByAcronym(courseAcr);
		final List<Lecture> lectures = db.getLecturesByAcronym(courseAcr);
		
//		for (Lecture c : lectures)
			Log.d(TAG, lectures.size() + "");
		
		view = inflater.inflate(R.layout.lecture_detail, container, false);
		
		setText(view, R.id.lectureDetailTitle, course.getName());
		setText(view, R.id.lectureDetailYears, course.getYear()+"");
		setText(view, R.id.lectureDetailSemester, course.getSemester());
		setText(view, R.id.lectureDetailLevel, course.getLevel()+"");
		setText(view, R.id.lectureDetailCredit, course.getCredit()+"");
		setText(view, R.id.lectureDetailLecturer, course.getLecturer());
		setText(view,  R.id.lectureDetailAcronym, course.getAcronym());
		
		setListenerURL(view, R.id.lectureDetailCourseWeb, course.getUrl());
		setListenerURL(view, R.id.lectureDetailEuclid, course.getEuclid());
		setListenerURL(view, R.id.lectureDetailDrps, course.getDrps());
		
		// Dynamically add lecture rows
		LinearLayout lecturesContainer = 
				(LinearLayout) view.findViewById(R.id.lecturesDetailLecturesContainer);
		for (final Lecture l: lectures) {
			LinearLayout newLayout = 
					(LinearLayout) inflater.inflate(R.layout.timetable_row_detail, lecturesContainer, false);
			setLayoutValues(newLayout, l);
			newLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					final Venue v = db.getVenueByName(l.getVenue().getBuilding());
					openURL(v.getMap());
				}
			});
			lecturesContainer.addView(newLayout);
		}
		
		

		return view;
	}
	
	private void setLayoutValues(LinearLayout newLayout, Lecture l) {
		setText(newLayout, R.id.timetableRowTimeStart, l.getTime().getStart());
		setText(newLayout, R.id.timetableRowTimeFinish, l.getTime().getFinish());
		Venue v = db.getVenueByName(l.getVenue().getBuilding());
		
		setText(newLayout, R.id.timetableRowDetailsLocation,
			l.getVenue().getRoom() + " " + v.getDescription());
		setText(newLayout, R.id.timetableRowDay, l.getDay());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		db.close();
	}
	
	private void setListenerURL(View view, int resource, String url) {
		TextView tv = (TextView) view.findViewById(resource);
		final String finalUrl = new String(url);
		tv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openURL(finalUrl);
			}
		});
	}
	
	private void openURL(String url) {
		Uri uri = Uri.parse(url);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
	
	private void setText(View view, int resource, String value) {
		TextView tv = (TextView) view.findViewById(resource);
		tv.setText(value);
	}

}
