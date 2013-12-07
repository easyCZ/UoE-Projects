package com.ug3.selp.timetableapp;

import java.util.ArrayList;
import java.util.List;

import com.ug3.selp.timetableapp.downloader.AsyncDownloader;
import com.ug3.selp.timetableapp.models.Course;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements DrawerListener {
	
	public static final String PREFS_NAME = "UoEInfTimetable";	
	private SharedPreferences preferences;
	
	private final String TAG = "MainActivity";
	
	private DrawerLayout drawer;
	private AutoCompleteTextView autoComplete;
	private int progressField = R.id.downloadAndParseProgressValue;
	private int progressBar = R.id.downloadAndParseSpinner;
	private int downloadAndParseDesc = R.id.downloadAndParseDescSection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        preferences = this.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        
        boolean dataAvailable = preferences.getBoolean("dataAvailable", false);
        
        if (dataAvailable) {
        	RelativeLayout layout = (RelativeLayout) findViewById(R.id.downloadAndParse);
        	layout.setVisibility(View.GONE);
        }
        	
        
        
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        // Attach Drawer Listener		
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerListener(this);
        
        
        
        final ListView listview = (ListView) findViewById(R.id.listview);
        
        final List<Course> courses = generateCourses();
        final DrawerArrayAdapter adapter = new DrawerArrayAdapter(this, R.layout.sidebar_drawer, courses);
        
        listview.setAdapter(adapter);
        
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            	ImageView icon = (ImageView) view.findViewById(R.id.sidebarIcon);
            	Log.i(TAG, "Element has drawable: " + icon.getDrawable().toString());
            	icon.setImageResource(R.drawable.checkbox_checked);
            	Log.i(TAG, "Element " + position + " clicked.");
            	Toast.makeText(view.getContext(), "Showing toast", Toast.LENGTH_SHORT).show();
            }

          });
        
        RelativeLayout dlAndParse = (RelativeLayout) findViewById(R.id.downloadAndParse);
        dlAndParse.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.d(TAG, "Starting AsyncDownloader");
				AsyncDownloader aDownloader = new AsyncDownloader(
					getApplicationContext(),
					(TextView) findViewById(progressField),
					(ProgressBar) findViewById(progressBar),
					(LinearLayout) findViewById(downloadAndParseDesc));
				aDownloader.execute(
//					"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/timetable.xml",
//					"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/courses.xml",
					"http://www.inf.ed.ac.uk/teaching/courses/selp/xml/venues.xml");
			}
		});
        
        
    }
    

	private List<Course> generateCourses() {
    	List<Course> list = new ArrayList<Course>();
    	Course course = new Course();
    		   course.setName("CourseName");
    	for (int i = 0; i < 10; i++) list.add(course);
    	return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

	@Override
	public void onDrawerClosed(View arg0) {}
	
	@Override
	public void onDrawerOpened(View arg0) {}
	
	@Override
	public void onDrawerStateChanged(int arg0) {}
	
	@Override
	public void onDrawerSlide(View arg0, float arg1) {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);
	}
	
	
	
    
}
