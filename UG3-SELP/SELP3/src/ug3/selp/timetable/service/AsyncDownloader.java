package ug3.selp.timetable.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import ug3.selp.timetable.R;
import ug3.selp.timetable.db.DatabaseHelper;
import ug3.selp.timetable.models.Course;
import ug3.selp.timetable.models.Lecture;
import ug3.selp.timetable.models.Venue;
import ug3.selp.timetable.parser.CourseParser;
import ug3.selp.timetable.parser.Parser;
import ug3.selp.timetable.parser.TimetableParser;
import ug3.selp.timetable.parser.VenueParser;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * @author s1115104
 *
 * Download and parse XML files asynchronously.
 */
public class AsyncDownloader {

	private final String TAG = "AsyncDownloader";

	private Context context;
	private boolean finished = false;
	private TextView progressDesc;
	private TextView progressCount;
	private ProgressBar progressBar;

	private DatabaseHelper db;

	public AsyncDownloader(Context applicationContext, LinearLayout container) {
		this.context = applicationContext;
		this.progressDesc = (TextView) container.findViewById(R.id.downloadProgressDesc);
		this.progressCount = (TextView) container.findViewById(R.id.downloadProgressCount);
		this.progressBar = (ProgressBar) container.findViewById(R.id.downloadProgressBar);

		db = new DatabaseHelper(this.context);
	}

	public void execute(String...strings) throws Exception {
		if (strings.length != 3)
			throw new Exception(TAG + ": AsyncDownloader requires 3 URLs to be passed in.");
		if (!finished)
			new AsyncDownloaderTask().execute(strings);
	}

	private class AsyncDownloaderTask extends AsyncTask<String, Integer, Document> {

		//		private String TAG = "AsyncDownloader";
		private Parser<Venue> venueParser = new VenueParser();
		private Parser<Course> courseParser = new CourseParser();
		private Parser<Lecture> timetableParser = new TimetableParser();


		@Override
		protected Document doInBackground(String... params) {

			// Parse venues
			venueParser.extract(this.getDocument(params[0]));
			for (Venue v: venueParser.get()) {
				db.insert(v);
			}
			publishProgress(1);

			// Parse Courses
			courseParser.extract(this.getDocument(params[1]));
			for (Course c: courseParser.get()) {
				db.insert(c);
			}
			publishProgress(2);

			// Parse timetable
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

				// Check Internet connection is okay
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
					return null;

				InputStream stream = connection.getInputStream();

				// Build document
				DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = factoryBuilder.newDocumentBuilder();

				// Parse document
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
			progressDesc.setVisibility(View.VISIBLE);
			progressCount.setVisibility(View.VISIBLE);
			progressCount.setText("0 of 3");
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// Set progress to the integer passed in
			CharSequence text = "0";
			if (progress.length > 0) text = Integer.toString(progress[0]);
			progressCount.setText(text + " of 3");
		}

		@Override
		protected void onPostExecute(Document result) {
			// Update text in the view
			progressDesc.setText("Download complete, please return to the main screen.");
			progressCount.setVisibility(View.GONE);
			progressBar.setVisibility(View.GONE);

			// Udpate preferences
			Preferences prefs = new Preferences(
					context, Resources.PREFERENCES_KEY, Context.MODE_PRIVATE);
			prefs.set(Resources.PREFERENCES_DATA, true);

			Calendar c = Calendar.getInstance(); 
			String date = String.format(Locale.ENGLISH, 
					"%d/%d/%d",c.get(Calendar.DAY_OF_MONTH), 
					c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR));
			// Update preferences tos how the download date
			prefs.set(
					Resources.PREFERENCES_DATA_DATE_DOWNLOADED, date);
		}

	}


}