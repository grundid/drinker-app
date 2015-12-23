package de.grundid.drinker.storage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.grundid.android.db.AbstractTableHandler;
import de.grundid.android.db.RowMapper;
import de.grundid.android.db.TableHandler;
import de.grundid.drinker.LocationModel;

public class LocationTableHandler extends AbstractTableHandler implements RowMapper<Location> {

	public static final String TABLE_NAME = "location";
	public static final String COL_PLACE_ID = "place_id";
	public static final String COL_NAME = "name";
	public static final String COL_ADDRESS = "address";
	public static final String COL_LAT = "lat";
	public static final String COL_LON = "lon";
	public static final String COL_VISITS = "visits";
	public static final String COL_LAST_VISIT = "last_visit";
	public static final String[] SELECT_COLUMNS = { TABLE_NAME + "." + COL_PLACE_ID,
			TABLE_NAME + "." + COL_NAME, TABLE_NAME + "." + COL_ADDRESS, TABLE_NAME + "." + COL_LAT,
			TABLE_NAME + "." + COL_LON, TABLE_NAME + "." + COL_VISITS, TABLE_NAME+"."+COL_LAST_VISIT };

	public LocationTableHandler() {
		super(TABLE_NAME);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
				+ COL_PLACE_ID + " VARCHAR(32) PRIMARY KEY, " + COL_NAME + " VARCHAR(255), " + COL_ADDRESS
				+ " VARCHAR(255), "
				+ COL_LAT + " REAL, " + COL_LON + " REAL, " + COL_VISITS + " INTEGER(4), "+COL_LAST_VISIT +" INTEGER(8))");
	}

	public Location mapRow(Cursor cursor, int c) {
		Location model = new Location();
		model.setPlaceId(cursor.getString(c++));
		model.setName(cursor.getString(c++));
		model.setAddress(cursor.getString(c++));
		model.setLatitude(cursor.getDouble(c++));
		model.setLongitude(cursor.getDouble(c++));
		model.setVisits(cursor.getInt(c++));
		model.setLastVisit(cursor.getLong(c++));
		return model;
	}

	public ContentValues createContentValues(Location location) {
		ContentValues values = new ContentValues();
		values.put(COL_PLACE_ID, location.getPlaceId());
		values.put(COL_NAME, location.getName());
		values.put(COL_ADDRESS, location.getAddress());
		values.put(COL_LAT, location.getLatitude());
		values.put(COL_LON, location.getLongitude());
		values.put(COL_VISITS, location.getVisits());
		values.put(COL_LAST_VISIT, location.getLastVisit());
		return values;
	}

}
