package com.ug3.selp.timetableapp;

import java.util.List;

import com.ug3.selp.timetableapp.adapter.SearchListAdapter;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author s1115104
 *
 * Fragment for search handling. Contains a ListView. Rows are dynamically added
 * based on search query.
 * 
 * Dynamic list view would a possible optimization.
 */
public class SearchFragment extends Fragment {
	
	private final String TAG = "SearchFragment";
	
	private View view;
	private SearchListAdapter searchAdapter;
	private Context context;
	private List<Course> courses;
	private DatabaseHelper db;
	private ListView searchList;
	private SearchReceiver searchReceiver;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = getActivity();
		
		// Register receiver for information passing
		searchReceiver = new SearchReceiver();
		context.registerReceiver(
			searchReceiver, new IntentFilter(Resources.FRAGMENT_BUNDLE_KEY));
		
		// Init db
		db = new DatabaseHelper(context);
		
		view = inflater.inflate(R.layout.search_list, container, false);
		
		searchList = (ListView) view.findViewById(R.id.searchListView);
		// Get courses, null for no query filter
		courses = db.getCourses(null);
		// Attach to adapter
		searchAdapter = new SearchListAdapter(
			context, R.layout.search_list_row, courses);
		searchList.setAdapter(searchAdapter);
		// Release db pointer
		db.close();
		return view;
	}
	
	
	// Custom Receiver for Fragment communication
	// Used to pass search query around.
	// A better solution would be to tie together with a fragment
	public class SearchReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Broadcast received in the fragment.");
			
			int id = intent.getIntExtra(Resources.FRAGMENT_SEARCH_KEY, -1);
			if (id == Resources.FRAGMENT_SEARCH_ACTION) {
				String query = intent.getStringExtra(Resources.FRAGMENT_SEARCH_QUERY);
				if (query != null && !query.equals("")) {
					// Refresh set of entries
					List<Course> filteredCourses = db.getCoursesByFilter(query);
					// Re-attach entries
					searchAdapter.clear();
					for (Course c: filteredCourses) 
						searchAdapter.add(c);
					courses = filteredCourses;
				}
			}
		}
	}
	
	// Release resources
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null)
			db.close();
		context.unregisterReceiver(searchReceiver);
		Log.d(TAG, "Resources released, leaving.");
	}		

}
