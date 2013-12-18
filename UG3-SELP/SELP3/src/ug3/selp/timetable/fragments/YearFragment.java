package ug3.selp.timetable.fragments;

import java.util.ArrayList;
import java.util.List;

import ug3.selp.timetable.R;
import ug3.selp.timetable.adapters.SelectCourseArrayAdapter;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Course;
import ug3.selp.timetable.service.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class YearFragment extends Fragment {

	private String year;

	private DatabaseHelper db;
	private SelectCourseArrayAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		// Year argumetns are passed along in the Bundle, retrieve them.
		year = getArguments().getString(Resources.BUNDLE_YEAR_KEY);
		if (year == null)
			return null;
		
		// db init
		init();

		View view = inflater.inflate(R.layout.select_course_fragment, container, false);
		
		setUpListView(R.id.selectFragmentListView, view);		
		updateAdapterEntries(year);

		return view;
	}

	private void init() {
		db = new DatabaseHelper(getActivity());
	}

	// Auxiliary - attach adapter
	private void setUpListView(int id, View view) {
		ListView listView = (ListView) view.findViewById(R.id.selectFragmentListView);

		List<Course> courses = new ArrayList<Course>();
		adapter = new SelectCourseArrayAdapter(
				getActivity(), 0, courses);
		listView.setAdapter(adapter);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null) {
			db.close();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		// Data might have changed, update it.
		updateAdapterEntries(year);
	}

	// Auxiliary - update the entries of the adapter
	private void updateAdapterEntries(String year) {
		List<Course> courses = db.getCoursesFiltered(year);
		adapter.clear();
		for (Course c: courses)
			adapter.add(c);
	}

}
