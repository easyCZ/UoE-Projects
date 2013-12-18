package ug3.selp.timetable.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ug3.selp.timetable.models.Venue;

import android.util.Log;

/**
 * @author s1115104
 *
 * Venue parser. Traverses the courses.xml document and extracts data.
 * Return a a list of Venues.
 */
public class VenueParser implements Parser<Venue>{

	private List<Venue> venues;
	private String TAG = "VenueParser";

	public VenueParser() {
		venues = new ArrayList<Venue>();
	}

	@Override
	public void extract(Document document) {
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
						if (nodeName.equals("name"))
							venue.setName(tag.getTextContent());
						else if (nodeName.equals("description"))
							venue.setDescription(tag.getTextContent());
						else if (nodeName.equals("map"))
							venue.setMap(tag.getTextContent());
					}
				}
				
				venues.add(venue);
				
			}
			
		}
		Log.i(TAG, "Parsing venues has suceeded.");
	}

	@Override
	public List<Venue> get() {
		return venues;
	}

	@Override
	public Class<Venue> getMyType() {
		// TODO Auto-generated method stub
		return Venue.class;
	}
	
	
}