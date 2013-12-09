package com.ug3.selp.timetableapp.parser;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



import android.util.Log;

import com.ug3.selp.timetableapp.models.Course;

public class CourseParser implements Parser<Course> {
	
	private List<Course> courses;
	private final String TAG = "CourseParser";
	
	public CourseParser() {
		courses = new ArrayList<Course>();
	}

	@Override
	public void extract(Document document) {
		NodeList _courses = document.getElementsByTagName("course");
		
		if (_courses != null && _courses.getLength() > 0) {
			
			for (int i = 0; i < _courses.getLength(); i++) {
				Element _course = (Element) _courses.item(i);
				NodeList _courseElems = _course.getChildNodes();
				Course course = new Course();
				
				for (int j = 0; j < _courseElems.getLength(); j++) {
					Node tag = _courseElems.item(j);
					
					if (tag.getNodeType() != Node.TEXT_NODE) {
						String nodeName = tag.getNodeName();
						if (nodeName.equals("url"))
							course.setUrl(tag.getTextContent());
						else if (nodeName.equals("name"))
							course.setName(tag.getTextContent());
						else if (nodeName.equals("drps"))
							course.setDrps(tag.getTextContent());
						else if (nodeName.equals("euclid"))
							course.setEuclid(tag.getTextContent());
						else if (nodeName.equals("acronym"))
							course.setAcronym(tag.getTextContent());
						else if (nodeName.equals("ai") || 
								 nodeName.equals("cg") ||
								 nodeName.equals("cs") ||
								 nodeName.equals("se")) {
							
							String content = tag.getTextContent();
							if (!content.isEmpty())
								course.addDegree(content);
						}
						else if (nodeName.equals("level"))
							course.setLevel(Integer.parseInt(tag.getTextContent()));
						else if (nodeName.equals("points"))
							course.setCredit(Integer.parseInt(tag.getTextContent()));
						else if (nodeName.equals("year"))
							course.setYear(Integer.parseInt(tag.getTextContent()));
						else if (nodeName.equals("deliveryperiod"))
							course.setSemester(tag.getTextContent());
						else if (nodeName.equals("lecturer"))
							course.setLecturer(tag.getTextContent());	
					}
				}
				courses.add(course);
//				Log.d(TAG, course.toString());
				
			}
			
		}
		Log.i(TAG, "Parsing Courses Suceeded");
		
	}

	@Override
	public List<Course> get() {
		return courses;
	}

	@Override
	public Class<Course> getMyType() {
		// TODO Auto-generated method stub
		return Course.class;
	}

}
