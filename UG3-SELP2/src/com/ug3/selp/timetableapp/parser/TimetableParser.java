package com.ug3.selp.timetableapp.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.util.Log;

import com.ug3.selp.timetableapp.models.Lecture;
import com.ug3.selp.timetableapp.models.TimeSlot;
import com.ug3.selp.timetableapp.models.VenueSimple;

/**
 * @author s1115104
 *
 * Timetable parser. Traverses the timetable.xml document and extracts data.
 * Return a a list of Lectures.
 */
public class TimetableParser implements Parser<Lecture> {
	
	private final String TAG = "TimetableParser";
	
	private List<Lecture> timetable;
	
	public TimetableParser() {
		timetable = new ArrayList<Lecture>();
	}

	@Override
	public void extract(Document document) {		
		NodeList _semesters = document.getElementsByTagName("semester");
		
		if (_semesters != null && _semesters.getLength() > 0) {
			for (int sem = 0; sem < _semesters.getLength(); sem++) {
				Node node = _semesters.item(sem);
				NamedNodeMap _attrs =  node.getAttributes();
				
				for (int attr = 0; attr < _attrs.getLength(); attr++) {
					Node attrNode = _attrs.item(attr);
					if (attrNode.getNodeName().equals("number") && attrNode.getTextContent().equals("1"))
						parseSemester(node.getChildNodes(), 1);
					else
						parseSemester(node.getChildNodes(), 2);
				}
			}
		}
		Log.d(TAG, "Parsing Timetable comepleted. There are " + timetable.size() + " elements.");
	}
	
	private void parseSemester(NodeList nodes, int semester) {
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("week"))
				parseWeek(nodes.item(i).getChildNodes(), 1);
		}
	}

	private void parseWeek(NodeList nodes, int semester) {
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("day")) {
				NamedNodeMap _attrs = nodes.item(i).getAttributes();
				for (int attr = 0; attr < _attrs.getLength(); attr++) {
					if (_attrs.item(attr).getNodeName().equals("name"))
						parseDay(nodes.item(i).getChildNodes(), semester, _attrs.item(attr).getTextContent());
				}
			}
		}
		
	}
	
	private void parseDay(NodeList nodes, int semester, String day) {
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("time")) {
				NamedNodeMap _attrs = nodes.item(i).getAttributes();
				TimeSlot time = new TimeSlot();
				for (int j = 0; j < _attrs.getLength(); j++) {
					if (_attrs.item(j).getNodeName().equals("start"))
						time.setStart(_attrs.item(j).getTextContent());
					else
						time.setFinish(_attrs.item(j).getTextContent());
				}
				parseTime(nodes.item(i).getChildNodes(), semester, day, time);
			}
		}
	}
	
	private void parseTime(NodeList nodes, int semester, String day, TimeSlot time) {
		for (int i = 0; i < nodes.getLength(); i++) {
			if (nodes.item(i).getNodeName().equals("lecture")) {
				parseLecture(nodes.item(i).getChildNodes(), semester, day, time);
			}
		}
	}
	
	private void parseLecture(NodeList nodes, int semester, String day, TimeSlot time) {
		Lecture l = new Lecture();
		for (int i = 0; i < nodes.getLength(); i++) {
			
			l.setSemester(Integer.toString(semester));
			l.setDay(day);
			l.setTime(time);
			String nodeName = nodes.item(i).getNodeName();
			NodeList childNodes = nodes.item(i).getChildNodes();

			if (nodeName.equals("course")) {
				l.setCourse(parseCourse(nodes.item(i)));
			} else if (nodeName.equals("years")) {
				l.setYears(parseYears(childNodes));
			} else if (nodeName.equals("venue")) {
				l.setVenue(parseVenue(childNodes));
			} else if (nodeName.equals("comment")) {
				l.setComment(parseComment(childNodes));
			}
		}
		
		timetable.add(l);
	}


	private String parseComment(NodeList childNodes) {
		String comment = "";
		for (int i = 0; i < childNodes.getLength(); i++) {
			comment = childNodes.item(i).getTextContent();
		}
		return comment;
	}

	private VenueSimple parseVenue(NodeList childNodes) {
		VenueSimple venue = new VenueSimple();
		for (int i = 0; i < childNodes.getLength(); i++) {
			if (childNodes.item(i).getNodeName().equals("room"))
				venue.setRoom(childNodes.item(i).getTextContent());
			else if (childNodes.item(i).getNodeName().equals("building"))
				venue.setBuilding(childNodes.item(i).getTextContent());
		}
		return venue;
	}

	private List<Integer> parseYears(NodeList childNodes) {
		List<Integer> years = new ArrayList<Integer>();
		for (int i = 0; i < childNodes.getLength(); i++) 
			if (childNodes.item(i).getNodeName().equals("year")) 
				years.add(Integer.parseInt(childNodes.item(i).getTextContent()));
		return years;
	}

	private String parseCourse(Node node) {
		return node.getTextContent();
	}

	@Override
	public List<Lecture> get() {
		return timetable;
	}

	@Override
	public Class<Lecture> getMyType() {
		return Lecture.class;
	}

}
