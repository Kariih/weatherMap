package com.example.weathermap;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class StoreWeatherAdapter {
	FragmentMap myAsyncData = new FragmentMap();
	
	public static final String KEY_ID = "_id";
	public static final String KEY_SYMBOL = "symbol";
	public static final String KEY_TEMPERATURE = "temperature";
	public static final String KEY_TIME_DATE = "date_time";

	public static final String KEY_LAT = "latitude";
	public static final String KEY_LON = "longitude";
	public static final String KEY_TIME = "date";

	public static final String[] ALL_KEYS_PLACE = new String[] { KEY_ID,
			KEY_LAT, KEY_LON, KEY_TIME };
	public static final String[] ALL_KEYS_WEATHER = new String[] { KEY_ID,
		KEY_TEMPERATURE, KEY_TIME_DATE, KEY_SYMBOL };

	public static final String DATABASE_NAME = "weatherDb";
	public static final String DATABASE_TABLE_WEATHER = "weatherTable";
	public static final String DATABASE_TABLE_PLACE = "placeTable";
	public static final int DATABASE_VERSION = 1;

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public long id;
	
	public StoreWeatherAdapter(Context context) {
		this.context = context;
		DBHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();

	}

	public void close() {
		DBHelper.close();
	}

	public long insertPlace(Weather place) {

		ContentValues value = new ContentValues();
		value.put(KEY_LAT, place.getLat());
		value.put(KEY_LON, place.getLon());
		value.put(KEY_TIME, place.getDate());

		id = db.insert(DATABASE_TABLE_PLACE, null, value);
		
		return id;
	}
	public long insertWeather(ArrayList<Weather> weather) {
		
		for(Weather data : weather){
		ContentValues value = new ContentValues();
		value.put(KEY_ID, id);
		value.put(KEY_TEMPERATURE, data.getTemperature());
		value.put(KEY_TIME_DATE, data.getDate());
		value.put(KEY_SYMBOL, data.getWeatherType());
		db.insert(DATABASE_TABLE_WEATHER, null, value);
		}
		
		return 0;
	}
	public void deleteAll() {
		Cursor c1 = getAllRowsPlace();
		Cursor c2 = getAllRowsPlace();
		int rowId1 = c1.getColumnIndexOrThrow(KEY_ID);
		int rowId2 = c1.getColumnIndexOrThrow(KEY_ID);
		if (c1.moveToFirst()) {
			do {
				deleteRow(c1.getInt(rowId1), DATABASE_TABLE_PLACE);
			} while (c1.moveToNext());
		}
		c1.close();
		if (c2.moveToFirst()) {
			do {
				deleteRow(c2.getInt(rowId2), DATABASE_TABLE_WEATHER);
			} while (c2.moveToNext());
		}
		c1.close();
	}
	public boolean deleteRow(int rowId, String table) {
		String where = KEY_ID + "=" + rowId;
		return db.delete(DATABASE_TABLE_PLACE, where, null) != 0;
	}

	public Cursor getAllRowsPlace() {
		String where = null;
		Cursor c = db.query(true, DATABASE_TABLE_PLACE, ALL_KEYS_PLACE, where,
				null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}
	public Cursor getAllRowsWeather() {
		String where = null;
		Cursor c = db.query(true, DATABASE_TABLE_WEATHER, ALL_KEYS_WEATHER, where,
				null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor getRow(int rowId) {
		String where = KEY_ID + "=" + rowId;
		Cursor c = db.query(true, DATABASE_TABLE_WEATHER, ALL_KEYS_WEATHER, where,
				null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		private static final String DATABASE_CREATE_SQL_WEATHER = "CREATE TABLE "
				+ DATABASE_TABLE_WEATHER
				+ " ("
				+ KEY_ID + " NUMBER NOT NULL,"
				+ KEY_TEMPERATURE + " NUMBER NOT NULL,"
				+ KEY_SYMBOL + " NUMBER NOT NULL,"
				+ KEY_TIME_DATE + " NUMBER NOT NULL);";

		private static final String DATABASE_CREATE_SQL_PLACE = "CREATE TABLE "
				+ DATABASE_TABLE_PLACE + " (" 
				+ KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_LAT + " REAL NOT NULL," 
				+ KEY_LON + " REAL NOT NULL,"
				+ KEY_TIME + " TEXT NOT NULL);";

		public DatabaseHelper(Context context, String databaseName,
				Object object, int databaseVersion) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE_SQL_WEATHER);
			db.execSQL(DATABASE_CREATE_SQL_PLACE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
		}
	}

}
