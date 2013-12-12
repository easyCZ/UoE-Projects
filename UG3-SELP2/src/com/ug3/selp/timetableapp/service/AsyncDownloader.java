package com.ug3.selp.timetableapp.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ug3.selp.timetableapp.adapter.DrawerArrayAdapter;
import com.ug3.selp.timetableapp.db.DatabaseHelper;
import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.Lecture;
import com.ug3.selp.timetableapp.models.Venue;
import com.ug3.selp.timetableapp.parser.CourseParser;
import com.ug3.selp.timetableapp.parser.Parser;
import com.ug3.selp.timetableapp.parser.TimetableParser;
import com.ug3.selp.timetableapp.parser.VenueParser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author s1115104
 *
 * Download and parse XML files asynchronously.
 */
public class AsyncDownloader {
	
	private final String TAG = "AsyncDownloader";
	
	private Context context;
	private TextView textView;
	private ProgressBar progressBar;
	private LinearLayout downloadAndParseDesc;
	private boolean finished = false;
	private DrawerArrayAdapter adapter;
	private Preferences preferences;
	private RelativeLayout layout;
	
	private DatabaseHelper db;
	
	public AsyncDownloader(
			Context applicationContext, 
			Preferences preferences,
			TextView context, 
			ProgressBar progressBar, 
			LinearLayout downloadAndParseDesc,
			RelativeLayout layout,
			DrawerArrayAdapter adapter) {
		this.textView = context;
		this.progressBar = progressBar;
		this.downloadAndParseDesc = downloadAndParseDesc;
		this.context = applicationContext;
		this.adapter = adapter;
		this.preferences = preferences;
		this.layout = layout;
		
		db = new DatabaseHelper(this.context);
	}
	
	public void execute(String...strings) throws Exception {
		if (strings.length != 3)
			throw new Exception(TAG + ": AsyncDownloader requires 3 URLs to be passed in.");
		if (!finished)
			new AsyncDownloaderTask().execute(strings);
	}
	
	private class AsyncDownloaderTask extends AsyncTask<String, Integer, Document> {
		
		private String TAG = "AsyncDownloader";
		private Parser<Venue> venueParser = new VenueParser();
		private Parser<Course> courseParser = new CourseParser();
		private Parser<Lecture> timetableParser = new TimetableParser();


		@Override
		protected Document doInBackground(String... params) {
			
			venueParser.extract(this.getDocument(params[0]));
			for (Venue v: venueParser.get()) {
				db.insert(v);
			}
			publishProgress(1);
			
			courseParser.extract(this.getDocument(params[1]));
			for (Course c: courseParser.get()) {
				db.insert(c);
			}
			publishProgress(2);

			timetableParser.extract(this.getDocument(params[2]));
			for (Lecture t: timetableParser.get()) {
				db.insert(t);
			}
			publishProgress(3);
			db.close();
			
			return null;
		}	
		
		protected Document getDocument(String address) {
			try {
				URL url = new URL(address);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
					return null;

				InputStream stream = connection.getInputStream();
				
				DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = factoryBuilder.newDocumentBuilder();
				
				Document document = docBuilder.parse(stream);
				stream.close();
				
				return document;
				
			} catch (MalformedURLException e) {
				Log.e("MalformedURLException", e.toString());
			} catch (IOException e) {
				Log.e("IOException", e.toString());
			} catch (ParserConfigurationException e) {
				Log.e("ParserConfigurationException", e.toString());
			} catch (SAXException e) {
				Log.e("SAXException", e.toString());
			}
			return null;
		}
		
		@Override
		protected void onPreExecute() {
			// Update visibility for elements
			downloadAndParseDesc.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			CharSequence text = "0";
			if (progress.length > 0) text = Integer.toString(progress[0]);
			textView.setText(text);
	    }
		
		@Override
		protected void onPostExecute(Document result) {
			// Udpate preferences
			preferences.set("dataAvailable", true);
			// Hide Download and Parse XML button
			layout.setVisibility(View.GONE);
			
			Log.i(TAG, "AsyncDownloader finished.");
			finished = true;
			// Remove spinner
			((RelativeLayout)progressBar.getParent()).removeView(progressBar);
			List<Course> courses = db.getCourses(null);
			adapter.clear();
			for (Course c : courses)
				adapter.add(c);
			adapter.notifyDataSetChanged();
			
			db.close();
			
	    }
		
	}


}
