package com.ug3.selp.timetableapp.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.Timetable;
import com.ug3.selp.timetableapp.models.Venue;
import com.ug3.selp.timetableapp.parser.CourseParser;
import com.ug3.selp.timetableapp.parser.Parser;
import com.ug3.selp.timetableapp.parser.TimetableParser;
import com.ug3.selp.timetableapp.parser.VenueParser;

import db.DatabaseHelper;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AsyncDownloader {
	
	private final String TAG = "AsyncDownloader";
	
	private Context context;
	private TextView textView;
	private ProgressBar progressBar;
	private LinearLayout downloadAndParseDesc;
	private boolean finished = false;
	
	private DatabaseHelper db;
	
	public AsyncDownloader(Context applicationContext, TextView context, ProgressBar progressBar, LinearLayout downloadAndParseDesc) {
		this.textView = context;
		this.progressBar = progressBar;
		this.downloadAndParseDesc = downloadAndParseDesc;
		this.context = applicationContext;
		
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
		private Parser<Timetable> timetableParser = new TimetableParser();


		@Override
		protected Document doInBackground(String... params) {
			Log.d(TAG, "doInBackground called with " + params.length + " params.");
//			long id;
			
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
//			
//			timetableParser.extract(this.getDocument(params[2]));
//			for (Timetable t: timetableParser.get()) {
//				id = db.insert(t);
//			}
//			publishProgress(3);
			
			return null;
		}	
		
		protected Document getDocument(String address) {
			try {
				URL url = new URL(address);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.connect();
				
				if (connection.getResponseCode() != HttpURLConnection.HTTP_OK)
					return null;
				int fileLength = connection.getContentLength();
				Log.d(TAG, Integer.toString(fileLength));
				
				Log.i("URL", url.toString());
				InputStream stream = connection.getInputStream();
				
				DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
				DocumentBuilder docBuilder = factoryBuilder.newDocumentBuilder();
				
				Document document = docBuilder.parse(stream);
				
				Log.i(TAG, document.toString());

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
			
			downloadAndParseDesc.setVisibility(LinearLayout.VISIBLE);
			progressBar.setVisibility(ProgressBar.VISIBLE);
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			Log.d(TAG, "onProgress args: " + progress.length);
			CharSequence text = "0";
			if (progress.length > 0) text = Integer.toString(progress[0]);
			textView.setText(text);
	    }
		
		private void addSuccessIcon() {
			ImageView img = new ImageView(textView.getContext());
//			img.setBackground(R.id.)
		}
		
		@Override
		protected void onPostExecute(Document result) {
			finished = true;
			// Remove spinner
			((RelativeLayout)progressBar.getParent()).removeView(progressBar);
	    }
		
		// http://stackoverflow.com/questions/2325388/java-shortest-way-to-pretty-print-to-stdout-a-org-w3c-dom-document
		public void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
		    TransformerFactory tf = TransformerFactory.newInstance();
		    Transformer transformer = tf.newTransformer();
		    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		    transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

		    transformer.transform(new DOMSource(doc), 
		         new StreamResult(new OutputStreamWriter(out, "UTF-8")));
		}
	}


}
