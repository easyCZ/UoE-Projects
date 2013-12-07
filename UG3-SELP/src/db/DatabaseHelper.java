package db;

import java.util.List;
import java.util.Locale;

import com.ug3.selp.timetableapp.models.Course;
import com.ug3.selp.timetableapp.models.Timetable;
import com.ug3.selp.timetableapp.models.Venue;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private final String TAG = "DatabaseHelper";
	
	private static final int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "informaticsTimetable";
	
	private static final String TABLE_VENUES = "venues";
	private static final String TABLE_COURSES = "courses";
	private static final String TABLE_TIMETABLE = "timetable";
	
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
			"%s TEXT)",			// degree - CSV
			TABLE_COURSES, COURSE_NAME, COURSE_ACRONYM, COURSE_LECTURER, COURSE_DRPS, COURSE_URL,
			COURSE_EUCLID, COURSE_LEVEL, COURSE_YEAR, COURSE_CREDIT, COURSE_SEMESTER, COURSE_DEGREE);			
	
	
	
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VENUES);
		db.execSQL(CREATE_TABLE_COURSES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_VENUES));
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_COURSES));
		
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
		
		long rowId = db.insert(TABLE_COURSES, null, values);
		Log.d(TAG, Long.toString(rowId) + "added.");
		return rowId;
	}
	
	public long insert(Timetable timetable) {
		return 0;
	}
	
	public static String listToString(List<String> list) {
		StringBuilder result = new StringBuilder();
		for(String string : list) {
		    result.append(string);
		    result.append(",");
		}
		return result.length() > 0 ? result.substring(0, result.length() - 1): "";
	}

}
