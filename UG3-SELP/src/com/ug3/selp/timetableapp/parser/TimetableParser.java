package com.ug3.selp.timetableapp.parser;

import java.util.List;

import org.w3c.dom.Document;

import com.ug3.selp.timetableapp.models.Timetable;

public class TimetableParser implements Parser<Timetable> {

	@Override
	public void extract(Document d) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Timetable> get() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<Timetable> getMyType() {
		// TODO Auto-generated method stub
		return Timetable.class;
	}

}
