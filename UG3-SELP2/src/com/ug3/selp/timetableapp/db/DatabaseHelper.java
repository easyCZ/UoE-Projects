package com.ug3.selp.timetableapp.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.Lecture;
import com.ug3.selp.timetableapp.models.TimeSlot;
import com.ug3.selp.timetableapp.models.Venue;
import com.ug3.selp.timetableapp.models.VenueSimple;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * @author easy
 * 
 * Database interactions with database table/name specifics
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	
	private final String TAG = "DatabaseHelper";
	
	private static final int DATABASE_VERSION = 10;
	private static final String DATABASE_NAME = "informaticsTimetable";
	
	private static final String TABLE_VENUES = "venues";
	private static final String TABLE_COURSES = "courses";
	private static final String TABLE_LECTURES = "lectures";
	
	// Venue columns
	private static final String VENUE_NAME = "name";
	private static final String VENUE_DESC = "description";
	private static final String VENUE_MAP = "map";
	
	private static final String CREATE_TABLE_VENUES = 
			String.format("CREATE TABLE %s(%s TEXT, %s TEXT, %s TEXT)",
				TABLE_VENUES, VENUE_NAME, VENUE_DESC, VENUE_MAP);
	
	// Course Columns
	private static final String COURSE_NAME = "name";
	private static final String COURSE_ACRONYM = "acronym";
	private static final String COURSE_LECTURER = "lecturer";
	private static final String COURSE_DRPS = "drps";
	private static final String COURSE_URL = "url";
	private static final String COURSE_EUCLID = "euclid";
	private static final String COURSE_LEVEL = "level";
	private static final String COURSE_YEAR = "year";
	private static final String COURSE_CREDIT = "credit";
	private static final String COURSE_SEMESTER = "semester";
	private static final String COURSE_DEGREE = "degree";
	private static final String COURSE_TRACKED = "tracked";

	private static final String CREATE_TABLE_COURSES = 
		String.format(Locale.ENGLISH,
			"CREATE TABLE %s" +
			"(%s TEXT, " + 		// name
			"%s TEXT, " + 		// acronym 
			"%s TEXT, " +		// lecturer
			"%s TEXT, " +		// drps
			"%s TEXT, " +		// url
			"%s TEXT, " +		// euclid
			"%s INTEGER, " +	// level
			"%s INTEGER, " +	// year
			"%s INTEGER, " +	// credit
			"%s TEXT, " +		// semester
			"%s TEXT, " +		// degree - CSV
			"%s INTEGER)",		// tracked
			TABLE_COURSES, COURSE_NAME, COURSE_ACRONYM, COURSE_LECTURER, COURSE_DRPS, COURSE_URL,
			COURSE_EUCLID, COURSE_LEVEL, COURSE_YEAR, COURSE_CREDIT, COURSE_SEMESTER,
			COURSE_DEGREE, COURSE_TRACKED);	
	
	// Lecture columns
	private static final String LECTURE_COURSE_NAME = "name";
	private static final String LECTURE_YEARS = "years";
	private static final String LECTURE_VENUE_ROOM = "room";
	private static final String LECTURE_VENUE_BUILDING = "building";
	private static final String LECTURE_COMMENT = "comment";
	private static final String LECTURE_DAY = "day";
	private static final String LECTURE_TIME_START = "start";
	private static final String LECTURE_TIME_FINISH = "finish";
	private static final String LECTURE_SEMESTER = "semester";
	
	private static final String CREATE_TABLE_LECTURES = 
		String.format(Locale.ENGLISH,
			"CREATE TABLE %s(" +		// table name
			"%s TEXT, " +				// name
			"%s TEXT, " +				// years
			"%s TEXT, " +				// room
			"%s TEXT, " +				// building
			"%s TEXT, " +				// day
			"%s TEXT, " +				// start
			"%s TEXT, " +				// finish
			"%s TEXT, " +				// semester
			"%s TEXT)",					// comment
			TABLE_LECTURES, LECTURE_COURSE_NAME, LECTURE_YEARS, LECTURE_VENUE_ROOM, LECTURE_VENUE_BUILDING,
			LECTURE_DAY, LECTURE_TIME_START, LECTURE_TIME_FINISH, LECTURE_SEMESTER, LECTURE_COMMENT);
	
	
	
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VENUES);
		db.execSQL(CREATE_TABLE_COURSES);
		db.execSQL(CREATE_TABLE_LECTURES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_VENUES));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_COURSES));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_LECTURES));
		
		onCreate(db);
	}
	
	public long insert(Venue venue) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(VENUE_NAME, venue.getName());
		values.put(VENUE_DESC, venue.getDescription());
		values.put(VENUE_MAP, venue.getMap());
		
		long rowId = db.insert(TABLE_VENUES, null, values);
		return rowId;
	}
	
	public long insert(Course course) {
		
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(COURSE_NAME, course.getName());
		values.put(COURSE_ACRONYM, course.getAcronym());
		values.put(COURSE_LECTURER, course.getLecturer());
		values.put(COURSE_DRPS, course.getDrps());
		values.put(COURSE_URL, course.getUrl());
		values.put(COURSE_EUCLID, course.getEuclid());
		values.put(COURSE_LEVEL, course.getLevel());
		values.put(COURSE_YEAR, course.getYear());
		values.put(COURSE_CREDIT, course.getCredit());
		values.put(COURSE_SEMESTER, course.getSemester());
		values.put(COURSE_DEGREE, listToString(course.getDegree()));
		if (course.isTracked())	values.put(COURSE_TRACKED, 1);
		else values.put(COURSE_TRACKED, 0);
		
		long rowId = db.insert(TABLE_COURSES, null, values);
//		Log.d(TAG, Long.toString(rowId) + "added.");
		return rowId;
	}
		
	public long insert(Lecture l) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(LECTURE_COURSE_NAME, l.getCourse());
		values.put(LECTURE_YEARS, listToString(l.getYears()));
		values.put(LECTURE_VENUE_ROOM, l.getVenue().getRoom());
		values.put(LECTURE_VENUE_BUILDING, l.getVenue().getBuilding());
		values.put(LECTURE_COMMENT, l.getComment());
		values.put(LECTURE_DAY, l.getDay());
		values.put(LECTURE_TIME_START, l.getTime().getStart());
		values.put(LECTURE_TIME_FINISH, l.getTime().getFinish());
		values.put(LECTURE_SEMESTER, l.getSemester());
		
		long rowId = db.insert(TABLE_LECTURES, null, values);
		return rowId;
	}
	
	public boolean updateCourse(Course course) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			if (course.isTracked())	values.put(COURSE_TRACKED, 1);
			else values.put(COURSE_TRACKED, 0);
			
			db.update(TABLE_COURSES, values, COURSE_ACRONYM + "=?", new String[] {course.getAcronym()});;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<Course> getCoursesFiltered(String year) {
		int yearInt = Integer.parseInt(year.substring(1));
		Log.d(TAG, "YearInput: " + year);
		SQLiteDatabase db = getWritableDatabase();
		List<Course> courses = new ArrayList<Course>();
		String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE %s=%d", TABLE_COURSES, COURSE_YEAR, yearInt);
		Cursor cursor = null;
		
		try {
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst())
				do {
					courses.add(castToCourse(cursor));
				} while (cursor.moveToNext());
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return courses;
	}
	
	public List<Course> getTrackedCourses() {
		SQLiteDatabase db = getWritableDatabase();
		List<Course> courses = new ArrayList<Course>();
		String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE %s=1", TABLE_COURSES, COURSE_TRACKED);
		
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst())
				do {
					courses.add(castToCourse(cursor));
				} while (cursor.moveToNext());			
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return courses;
	}
	
	public List<Course> getCourses(String filter) {
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s", TABLE_COURSES);
			
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					courses.add(castToCourse(cursor));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return courses;
	}
	
	public List<Lecture> getLecturesByAcronym(String acr) {
		List<Lecture> lectures = new ArrayList<Lecture>();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			String query = "select * from lectures where name=\"" + acr + "\"";
			
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					lectures.add(castToLecture(cursor));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return lectures;
	}
	
	public List<Lecture> getLecturesByDay(String day, String semester) {
		Log.d(TAG, "Semester: " + semester + "   Day:" + day);
		
		List<Lecture> lectures = new ArrayList<Lecture>();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT DISTINCT " +
				"L.%s, L.%s, L.%s, " +
				"L.%s, L.%s, L.%s, " +
				"L.%s, L.%s, L.%s " +
				"FROM %s %s, %s %s " +
				"WHERE %s.%s=\"%s\" AND " +
				"%s.%s=%d AND " +
				"%s.%s = %s.%s AND " +
				"(%s.%s = '%s' OR C.semester = 'YR' OR C.semester = 'FLEX')", 
				LECTURE_COURSE_NAME, LECTURE_YEARS, LECTURE_VENUE_ROOM,
				LECTURE_VENUE_BUILDING, LECTURE_COMMENT, LECTURE_DAY,
				LECTURE_TIME_START, LECTURE_TIME_FINISH, LECTURE_SEMESTER,
				TABLE_LECTURES, "L", TABLE_COURSES, "C",
				"L", LECTURE_DAY, day,
				"C", COURSE_TRACKED, 1,
				"L", LECTURE_COURSE_NAME, "C", COURSE_ACRONYM,
				"C", COURSE_SEMESTER, semester);
				
			
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					lectures.add(castToLecture(cursor));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		
		Log.d(TAG, "Size:" + lectures.size());
		return lectures;
	}
	
	public List<Lecture> getLectures() {
		List<Lecture> lectures = new ArrayList<Lecture>();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s", TABLE_LECTURES);
			
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					lectures.add(castToLecture(cursor));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return lectures;
	}
	
	public Venue getVenueByName(String name) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE %s=\"%s\"", TABLE_VENUES, VENUE_NAME, name);
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				return castToVenue(cursor);
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	public Course getCourseByName(String name) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE %s=\"%s\"", TABLE_COURSES, COURSE_NAME, name);
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				return castToCourse(cursor);
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	public Course getCourseByAcronym(String acr) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE %s=\"%s\"", TABLE_COURSES, COURSE_ACRONYM, acr);
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				return castToCourse(cursor);
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	public Lecture getLectureByName(String name) {
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE %s=\"%s\"", TABLE_LECTURES, LECTURE_COURSE_NAME, name);
			
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				return castToLecture(cursor);
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}
	
	public Course castToCourse(Cursor cursor) {
		Course c = new Course();
		c.setName(cursor.getString(cursor.getColumnIndex(COURSE_NAME)));
		c.setAcronym(cursor.getString(cursor.getColumnIndex(COURSE_ACRONYM)));
		c.setLecturer(cursor.getString(cursor.getColumnIndex(COURSE_LECTURER)));
		c.setDrps(cursor.getString(cursor.getColumnIndex(COURSE_DRPS)));
		c.setUrl(cursor.getString(cursor.getColumnIndex(COURSE_URL)));
		c.setEuclid(cursor.getString(cursor.getColumnIndex(COURSE_EUCLID)));
		c.setLevel(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COURSE_LEVEL))));
		c.setYear(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COURSE_YEAR))));
		c.setCredit(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COURSE_CREDIT))));
		c.setSemester(cursor.getString(cursor.getColumnIndex(COURSE_SEMESTER)));
		c.setDegree(stringToList(cursor.getString(cursor.getColumnIndex(COURSE_DEGREE))));
		c.setTracked((cursor.getInt(cursor.getColumnIndex(COURSE_TRACKED)) == 1)? true : false);
		return c;
	}
	
	public Lecture castToLecture(Cursor c) {
		Lecture l = new Lecture();
		l.setCourse(c.getString(c.getColumnIndex(LECTURE_COURSE_NAME)));
		l.setYears(stringToIntList(c.getString(c.getColumnIndex(LECTURE_YEARS))));
		l.setVenue(new VenueSimple(
			c.getString(c.getColumnIndex(LECTURE_VENUE_ROOM)),
			c.getString(c.getColumnIndex(LECTURE_VENUE_BUILDING))
		));
		l.setComment(c.getString(c.getColumnIndex(LECTURE_COMMENT)));
		l.setDay(c.getString(c.getColumnIndex(LECTURE_DAY)));
		l.setTime(new TimeSlot(
			c.getString(c.getColumnIndex(LECTURE_TIME_START)), 
			c.getString(c.getColumnIndex(LECTURE_TIME_FINISH))
		));
		l.setSemester(c.getString(c.getColumnIndex(LECTURE_SEMESTER)));
		return l;
	}
	
	public Venue castToVenue(Cursor c) {
		Venue v = new Venue();
		v.setName(c.getString(c.getColumnIndex(VENUE_NAME)));
		v.setDescription(c.getString(c.getColumnIndex(VENUE_DESC)));
		v.setMap(c.getString(c.getColumnIndex(VENUE_MAP)));
		return v;
	}
	
	public static <E> String listToString(List<E> list) {
		StringBuilder result = new StringBuilder();
		for(E string : list) {
		    result.append(string);
		    result.append(",");
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1): "";
	}
	
	public static List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		String[] split = s.split(",");
		for (int i = 0; i < split.length; i++)
			list.add(split[i]);
		return list;
	}
	
	public static List<Integer> stringToIntList(String s) {
		List<Integer> list = new ArrayList<Integer>();
		String[] split = s.split(",");
		for (int i = 0; i < split.length; i++)
			list.add(Integer.parseInt(split[i]));
		return list;
	}
	
	public static List<String> wrapInSingleQuotes(List<String> list) {
		List<String> wrapped = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			wrapped.add("'" + Integer.parseInt(list.get(i).substring(1)) + "'");
		}
		return wrapped;
	}

	public List<Course> getCoursesByFilter(String q) {
		String wrappedQ = "%" + q + "%";
		List<Course> courses = new ArrayList<Course>();
		Cursor cursor = null;
		try {
			SQLiteDatabase db = getWritableDatabase();
			
			String query = String.format(Locale.ENGLISH,
				"SELECT * FROM %s WHERE " +
				"lower(name) LIKE lower('%s') OR lower(acronym) LIKE lower('%s')", 
				TABLE_COURSES, wrappedQ, wrappedQ);
			Log.d(TAG, "Input: " + q);
			Log.d(TAG, query);
			
			cursor = db.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				do {
					courses.add(castToCourse(cursor));
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			Log.d(TAG, e.toString());
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return courses;
	}
	

}