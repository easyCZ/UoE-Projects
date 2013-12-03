package com.ug3.selp.timetableapp.downloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import android.util.Log;

public class Downloader {
	
	public Downloader() {}
	
	public Document getDocument(String address) {
		try {
			URL url = new URL(address);
			Log.i("URL", url.toString());
			InputStream stream = url.openStream();
			DocumentBuilderFactory factoryBuilder = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factoryBuilder.newDocumentBuilder();
			Document document = docBuilder.parse(stream);
			
			System.out.println(document.toString());
			stream.close();
			
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//			document.getDocumentElement().normalize();
			return document;
			
		} catch (MalformedURLException e) {
			Log.e("MalformedURLException", e.toString());
			return null;
		} catch (IOException e) {
			Log.e("IOException", e.toString());
			return null;
		} catch (ParserConfigurationException e) {
			Log.e("ParserConfigurationException", e.toString());
			return null;
		} catch (SAXException e) {
			Log.e("SAXException", e.toString());
			return null;
		}
	}

}
