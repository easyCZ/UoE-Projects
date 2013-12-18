package ug3.selp.timetable;

import ug3.selp.timetable.service.AsyncDownloader;
import ug3.selp.timetable.service.Preferences;
import ug3.selp.timetable.service.Resources;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuInflater;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DownloadActivity extends SherlockActivity {
	
	private final String TAG = "DownloadActivity";
	private Preferences preferences;
	private boolean isDataAvailable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_activity);
		
		preferences = new Preferences(
			getApplicationContext(), Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
		isDataAvailable = preferences.getBoolean(Resources.PREFERENCES_DATA);
		
		// Show the Up button in the action bar.
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		// Hide title
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		
		// Attach listener
		final LinearLayout container = (LinearLayout) findViewById(R.id.downloadContainer);
		container.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Check we have the data, otherwise refuse the action
				if (isDataAvailable) {
					Toast.makeText(
						getApplicationContext(), "Data already available.", Toast.LENGTH_LONG).show();
					return;
				}
				// Ensure Internet connection available.
				if (isNetworkAvailable()) {
					AsyncDownloader aDownloader = new AsyncDownloader(
						getApplicationContext(), container);
					try {
						aDownloader.execute(
						        "http://www.inf.ed.ac.uk/teaching/courses/selp/xml/venues.xml",
						        "http://www.inf.ed.ac.uk/teaching/courses/selp/xml/courses.xml",
						        "http://www.inf.ed.ac.uk/teaching/courses/selp/xml/timetable.xml");
					} catch (Exception e) {
						Log.w(TAG, "Downloading failed.");
						Toast.makeText(getApplicationContext(), "Downloading failed. Try again later.", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				} else {
					Toast.makeText(getApplicationContext(), "No internet connection. Try again later.", Toast.LENGTH_LONG).show();
                    return;
				}
			}
		});
		
	}
	
	// Auxiliary - Test for connectivity
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager 
              = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

	@Override
	public boolean onCreateOptionsMenu(com.actionbarsherlock.view.Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.download, menu);
	    
	    return super.onCreateOptionsMenu(menu);
	}

}
