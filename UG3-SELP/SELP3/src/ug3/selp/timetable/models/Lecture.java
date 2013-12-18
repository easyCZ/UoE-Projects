package ug3.selp.timetable.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

import ug3.selp.timetable.db.DatabaseHelper;


public class Lecture {
	
	private String course;
	private List<Integer> years;
	private VenueSimple venue;
	private String comment;
	private String day;
	private TimeSlot time;
	private String semester;
	private Venue venueFull;
	private Course courseFull;
	
	public Lecture() {
		this.course = this.comment = this.setDay(this.comment = this.setSemester(""));
		this.years = new ArrayList<Integer>();
		this.setVenue(new VenueSimple());
	}
	
	public Lecture (String course, String years, String building, String room,
			String comment, String day, String start, String finish, String semester) {
		this.course = course;
		this.years = DatabaseHelper.stringToIntList(years);
		this.venue = new VenueSimple(room, building);
		this.comment = comment;
		this.day = day;
		this.time = new TimeSlot(start, finish);
		this.semester = semester;
	}
	
	public Lecture(Parcel in) {
		String[] data = new String[9];

		in.readStringArray(data);
		this.course = data[0];
		this.years = DatabaseHelper.stringToIntList(data[1]);
		this.venue = new VenueSimple(data[3], data[2]);
		this.comment = data[4];
		this.day = data[5];
		this.time = new TimeSlot(data[6], data[7]);
		this.semester = data[8];
	}
		
	
	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public List<Integer> getYears() {
		return years;
	}

	public void setYears(List<Integer> years) {
		this.years = years;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TimeSlot getTime() {
		return time;
	}

	public void setTime(TimeSlot time) {
		this.time = time;
	}


	public String getSemester() {
		return semester;
	}


	public String setSemester(String semester) {
		this.semester = semester;
		return semester;
	}

	public String getDay() {
		return day;
	}


	public String setDay(String day) {
		this.day = day;
		return day;
	}


	public VenueSimple getVenue() {
		return venue;
	}


	public void setVenue(VenueSimple venue) {
		this.venue = venue;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ENGLISH,
			"%s - %s - %s - %s - %s - %s - %s - %s - %s",
			course, DatabaseHelper.listToString(years), venue.getBuilding(), 
			venue.getRoom(), comment, day, time.getStart(), time.getFinish(), semester);
	}
	
	public Venue getVenueFull() {
		return venueFull;
	}

	public void setVenueFull(Venue venueFull) {
		this.venueFull = venueFull;
	}

	public Course getCourseFull() {
		return courseFull;
	}

	public void setCourseFull(Course courseFull) {
		this.courseFull = courseFull;
	}

	public static final Parcelable.Creator<Lecture> CREATOR = new Parcelable.Creator<Lecture>() {
        @Override
		public Lecture createFromParcel(Parcel in) {
            return new Lecture(in); 
        }

        @Override
		public Lecture[] newArray(int size) {
            return new Lecture[size];
        }
    };

}
