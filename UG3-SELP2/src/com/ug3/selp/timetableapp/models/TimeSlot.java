package com.ug3.selp.timetableapp.models;

public class TimeSlot {
	
	private String start, finish;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getFinish() {
		return finish;
	}

	public void setFinish(String finish) {
		this.finish = finish;
	}
	
	public String toString() {
		return "Start: " + start + ", Finish: " + finish;
	}
	
	
	
}
