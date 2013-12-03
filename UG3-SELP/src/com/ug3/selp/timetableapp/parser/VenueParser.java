package com.ug3.selp.timetableapp.parser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ug3.selp.timetableapp.models.Venue;

import android.util.Log;

public class VenueParser {

	private List<Venue> venues;
	private String TAG = "VenueParser";

	public VenueParser() {
		venues = new ArrayList<Venue>();
	}

	/*
	 * Extracts venues out of Venues XML Document
	 * @Document document	Converted XML Document
	 */
	public void extractVenues(Document document) {
		
		NodeList buildings = document.getElementsByTagName("building");
		
		if (buildings != null && buildings.getLength() > 0) {
			
			for (int i=0; i < buildings.getLength(); i++) {
				Element building = (Element) buildings.item(i);
				NodeList buildingElems = building.getChildNodes();
				Venue venue = new Venue();
				
				for (int j = 0; j < buildingElems.getLength(); j++) {
					Node tag = buildingElems.item(j);
					if (tag.getNodeType() != Node.TEXT_NODE) {
						String nodeName = tag.getNodeName();
						
					
						if (nodeName == "name")
							venue.setName(tag.getTextContent());
						else if (nodeName == "description")
							venue.setDescription(tag.getTextContent());
						else if (nodeName == "map")
							try {
								venue.setMap(new URI(tag.getTextContent()));
							} catch (URISyntaxException e) {
								Log.e(TAG, "URISyntaxException - bad url");
							}
					}
				}
				
				venues.add(venue);
				
			}
			
		}
		Log.i(TAG, "Parsing venues has suceeded.");
	}

	public List<Venue> getVenues() {
		return venues;
	}
	
	
}