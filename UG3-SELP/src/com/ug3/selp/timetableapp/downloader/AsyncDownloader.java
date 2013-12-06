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

import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AsyncDownloader {
	
	private TextView activityContext;
	
	public AsyncDownloader(TextView context) {
		this.activityContext = context;
	}
	
	public void execute(String...strings) {
		new AsyncDownloaderTask().execute(strings);
	}
	
	private class AsyncDownloaderTask extends AsyncTask<String, Integer, Document> {
		
		private int MAX_TOTAL = 3;
		private List<Document> documents = new ArrayList<Document>();
		private String TAG = "AsyncDownloader";

		@Override
		protected Document doInBackground(String... params) {
			Log.d(TAG, "doInBackground called with " + params.length + " params.");
			
			for (int i = 0; i < MAX_TOTAL; i++) {
				Document doc = this.getDocument(params[i]);
				documents.add(doc);
				// Test for success
				if (doc != null) {
					Log.d(TAG, "Getting document at " + params[i] + " failed");
					cancel(true);
				}
				publishProgress(i + 1);
			}
			
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
				
				// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//				document.getDocumentElement().normalize();
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
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			Log.d(TAG, "onProgress args: " + progress.length);
			CharSequence text = "Length is 0";
			if (progress.length > 0) text = Integer.toString(progress[0]);
				
//			Log.d(TAG, "Progress: " + progress[0]);
			
			activityContext.setText(text);
	    }
		
		@Override
		protected void onPostExecute(Document result) {
//	        showDialog("Downloaded " + result + " bytes");
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
