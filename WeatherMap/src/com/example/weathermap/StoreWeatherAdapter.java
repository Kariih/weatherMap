package com.example.weathermap;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StoreWeatherAdapter {

	public static final String KEY_ID = "_id";
	public static final String KEY_WEATHER_ARRAY = "date";

	public static final String KEY_LAT = "lat";
	public static final String KEY_LON = "lon";
	public static final String KEY_TIME = "date";

	public static final String[] ALL_KEYS = new String[] { KEY_ID,
			KEY_LAT, KEY_LON, KEY_TIME };

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

	}

	public StoreWeatherAdapter open() {
		DBHelper = new DatabaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
		db = DBHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		DBHelper.close();
	}

	public long insert(Weather weather) {

		ContentValues value = new ContentValues();
		value.put(KEY_LAT, weather.getLat());
		value.put(KEY_LON, weather.getLon());
		value.put(KEY_TIME, weather.getDate());

		id = db.insert(DATABASE_TABLE_PLACE, null, value);
		
		value.put(KEY_ID, id);
		value.put(KEY_WEATHER_ARRAY, weather.getWeatherString());
		db.insert(DATABASE_TABLE_WEATHER, null, value);
		
		
		
		return id;
	}

	public boolean deleteRow(int rowId) {
		String where = KEY_ID + "=" + rowId;
		return db.delete(DATABASE_TABLE_PLACE, where, null) != 0;
	}

	public void deleteAll() {
		Cursor c = getAllRows();
		int rowId = c.getColumnIndexOrThrow(KEY_ID);
		if (c.moveToFirst()) {
			do {
				deleteRow(c.getInt(rowId));
			} while (c.moveToNext());
		}
		c.close();
	}

	public Cursor getAllRows() {
		String where = null;
		Cursor c = db.query(true, DATABASE_TABLE_PLACE, ALL_KEYS, where,
				null, null, null, null, null);
		if (c != null) {
			c.moveToFirst();
		}
		return c;
	}

	public Cursor getRow(int rowId) {
		String where = KEY_ID + "=" + rowId;
		Cursor c = db.query(true, DATABASE_TABLE_PLACE, ALL_KEYS, where,
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
				+ KEY_ID + " NUMBER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_WEATHER_ARRAY + " NUMBER NOT NULL);";

		private static final String DATABASE_CREATE_SQL_PLACE = "CREATE TABLE "
				+ DATABASE_TABLE_PLACE + " (" + KEY_ID
				+ " NUMBER PRIMARY KEY AUTOINCREMENT," + KEY_LAT
				+ " REAL NOT NULL," + KEY_LON + " REAL NOT NULL,"
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
