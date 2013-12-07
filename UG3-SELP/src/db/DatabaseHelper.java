package db;

import com.ug3.selp.timetableapp.models.Venue;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int DATABASE_VERSION = 1;
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
	
	
	public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_VENUES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_VENUES));
		
		onCreate(db);
	}
	
	public long insertVenue(Venue venue) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(VENUE_NAME, venue.getName());
		values.put(VENUE_DESC, venue.getDescription());
		values.put(VENUE_MAP, venue.getMap());
		
		long rowId = db.insert(TABLE_VENUES, null, values);
		return rowId;
	}

}
