package ug3.selp.timetable;

import java.util.List;

import ug3.selp.timetable.adapters.SearchArrayAdapter;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Course;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;


public class SearchActivity extends SherlockActivity {
	
	private List<Course> courses;
	private DatabaseHelper db;
	private SearchArrayAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		init();
		
		// Initially get all courses
		courses = db.getCourses(null);
		
		// Set adapter for the list view
		ListView listView = (ListView) findViewById(R.id.searchListView);
		adapter = new SearchArrayAdapter(
			getApplicationContext(), 0, courses);
		listView.setAdapter(adapter);
		
		// Attach listener to the search field
		EditText editText = (EditText) findViewById(R.id.searchInput);
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				query(s.toString(), count);				
			}
			
			private void query(String s, int length) {
				if (length > 0)
					courses = db.getCoursesByFilter(s);		// Filter by input
				else 
					courses = db.getCourses(null);	// Get all courses
				if (courses != null) {
					adapter.clear();
					for (Course c: courses)
						adapter.add(c);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override
			public void afterTextChanged(Editable s) {}
		});
	}
	
	// Auxiliary
	private void init() {
		db = new DatabaseHelper(getApplicationContext());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.search, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_clear:
			clearSearchField();			// clicking cross in action bar
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	// Auxiliary for clicking the cross in the action bar
	private void clearSearchField() {
		((EditText) findViewById(R.id.searchInput)).setText("");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null)
			db.close();
	}

}
