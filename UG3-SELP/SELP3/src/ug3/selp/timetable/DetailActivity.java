package ug3.selp.timetable;

import java.util.List;
import ug3.selp.timetable.adapters.DetailLectureAdapter;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Course;
import ug3.selp.timetable.models.Lecture;
import ug3.selp.timetable.service.Resources;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

public class DetailActivity extends SherlockActivity {
	
	private Course course;
	private DatabaseHelper db;
	private List<Lecture> lectures;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);
		
		ActionBar ab = getSupportActionBar();
	    
		init();
		
		// Determine course acronym for the course to display
		Intent intent = getIntent();
		String courseAcr = intent.getStringExtra(Resources.INTENT_EXTRA_COURSE_NAME);
		
		// Pull course data from the db
		course = getCourse(courseAcr);
		ab.setTitle(course.getAcronym());
	    ab.setSubtitle(course.getName());
	    // Get lectures list for the course
		lectures = db.getLecturesByAcronym(courseAcr);
		
		if (course != null) {
			// Set values
			setValue(R.id.detailTitle, course.getName());
			setValue(R.id.detailLecturer, course.getLecturer());
			setValue(R.id.detailEuclid, course.getEuclid());
			setValue(R.id.detailYears, course.getYear() + "");
			setValue(R.id.detailDegrees, DatabaseHelper.listToString(course.getDegree()));
			setValue(R.id.detailSemester, course.getSemester());
			setValue(R.id.detailCredit, course.getCredit()+"");
		}
		
		// Set-up list view for the lectures
		if (lectures != null && lectures.size() > 0) {
			DetailLectureAdapter adapter = new DetailLectureAdapter(
				getApplicationContext(), 0, lectures);
			ListView listView = (ListView) findViewById(R.id.detailLectures);
			listView.setAdapter(adapter);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (db != null)
			db.close();
	}
	
	private void init() {
		db = new DatabaseHelper(getApplicationContext());
	}
	
	// Auxiliary - set value of a text view
	private void setValue(int res, String text) {
		TextView tv = (TextView) findViewById(res);
		tv.setText(text);
	}
	
	// Function wrapper for the db call
	private Course getCourse(String acr) {
		return db.getCourseByAcronym(acr);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.detail, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	    	case R.id.action_web:
	    		openURL(course.getUrl());
	    		return true;
	    	case R.id.action_drps:
	    		openURL(course.getDrps());
	    		return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	// If valid, open the given url in the browser
	private void openURL(String url) {
		Uri uri;
		try {
			uri = Uri.parse(url);
		} catch (Exception e) {
			return;
		}
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(browserIntent);
	}

	

}
