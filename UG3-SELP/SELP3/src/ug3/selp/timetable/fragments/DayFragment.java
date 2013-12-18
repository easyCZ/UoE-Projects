package ug3.selp.timetable.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ug3.selp.timetable.R;
import ug3.selp.timetable.adapters.LectureListAdapter;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Lecture;
import ug3.selp.timetable.service.Preferences;
import ug3.selp.timetable.service.Resources;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

// Abstract fragment for listing courses on a specific day
public class DayFragment extends Fragment {

	private final String TAG = this.getClass().getName();
	private String day;

	private DatabaseHelper db;
	private LectureListAdapter adapter;
	private Preferences preferences;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Day argument is passed along in the bundle
		// Retrieve it
		day = getArguments().getString(Resources.BUNDLE_DAY_KEY);
		if (day == null)
			return null;

		// Initialize db and preferences
		db = new DatabaseHelper(getActivity());
		preferences = new Preferences(
				getActivity(), Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);

		// Inflate view
		view = inflater.inflate(R.layout.course_day, container, false);

		// Get listView pointer
		ListView listView = (ListView) view.findViewById(R.id.dayFragmentListView);
		// Attach adapter
		List<Lecture> lectures = new ArrayList<Lecture>();
		adapter = new LectureListAdapter(
				getActivity(), 0, lectures);
		listView.setAdapter(adapter);

		// Refersh the entries, taking care of semester and day setting
		updateAdapterEntries(day, _getSemester());

		return view;
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
		// Refresh entries in case the user added new courses as tracked
		updateAdapterEntries(day, _getSemester());
	}

	// 
	private void updateAdapterEntries(String day, String semester) {
		// get entries
		List<Lecture> lectures = db.getLecturesByDay(day, semester);
		adapter.clear();

		Log.d(TAG, "There are " + lectures.size() + " lectures currently showing.");

		// If no entries, show a text view with a message
		TextView tv = (TextView) view.findViewById(R.id.courseDayNoLectures);
		String text = getActivity().getString(R.string.there_are_no_lectures_for_this_day_);
		if (!preferences.getBoolean(Resources.PREFERENCES_DATA))
			text = text + " Have you downloaded the data?";
		tv.setText(text);
		// Take care of text view visibility
		if (lectures.size() == 0) {
			tv.setVisibility(View.VISIBLE);
		} else {
			tv.setVisibility(View.GONE);
		}
		// Update the entries, if empty - no effect
		for (Lecture l: lectures)
			adapter.add(l);
	}

	// Determine the semester, if semester set in preferences then use that
	// Else determine the month and then the semester
	private String _getSemester() {
		String sem = preferences.get(Resources.PREFERENCES_SEMESTER_KEY);
		// Determine the right semester 
		// sem is empty if it has not yet been set by another Activity
		if (sem.equals("")) {
			Calendar calendar = Calendar.getInstance();
			int month = calendar.get(Calendar.MONTH) + 1;
			if (month >= 7 && month <= 12) {        // semester 1
				return Resources.PREFERENCES_SEMESTER_1;

			} else if (month >= 1 && month <= 6) {        // semester 2
				return Resources.PREFERENCES_SEMESTER_2;
			}
		}
		return sem;
	}

}
