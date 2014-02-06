package ug3.selp.timetable.models;


public class TimeSlot {
	
	private String start, finish;
	
	public TimeSlot(String start, String finish) {
		this.start = start;
		this.finish = finish;
	}
	
	public TimeSlot() {
		this.start = this.finish = "";
	}

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
	
	@Override
	public String toString() {
		return "Start: " + start + ", Finish: " + finish;
	}
	
	
	
}
