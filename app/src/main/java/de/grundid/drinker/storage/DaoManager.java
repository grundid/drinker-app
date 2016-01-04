package de.grundid.drinker.storage;

import android.content.Context;
import android.database.Cursor;
import de.grundid.android.db.RowMapper;
import de.grundid.drinker.utils.PlaceWrapper;

import java.util.ArrayList;
import java.util.List;

public class DaoManager {

	private DrinkMenuDatabase helper;

	private DaoManager(DrinkMenuDatabase helper) {
		this.helper = helper;
	}

	private DaoManager(Context context) {
		this(new DrinkMenuDatabase(context));
	}

	public DrinkMenuDatabase getHelper() {
		return helper;
	}

	public static DaoManager with(Context context) {
		return new DaoManager(context);
	}

	public static <T> List<T> mapRowsFromCursor(Cursor cursor, RowMapper<T> rowMapper) {
		List<T> items = new ArrayList<T>();
		try {
			while (cursor.moveToNext()) {
				items.add(rowMapper.mapRow(cursor, 0));
			}
		}
		finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return items;
	}

	public static void appendSelectColumns(StringBuilder sb, String[] selectColumns) {
		for (String column : selectColumns) {
			if (sb.length() > 0)
				sb.append(", ");
			sb.append(column);
		}
	}

	public void incLocationVisitCount(String placeId) {
		helper.writeSqlTemplate(new IncLocationVisitCountQuery(placeId));
	}

	public Location selectLocation(String placeId) {
		return helper.readSqlTemplate(new SelectLocationQuery(placeId));
	}

	public List<Location> selectAllLocations() {
		return helper.readSqlTemplate(new SelectAllLocationsQuery());
	}

	public void insertLocation(Location location) {
		helper.writeSqlTemplate(new InsertLocation(location));
	}

	public void deleteLocation(String placeId){
		helper.writeSqlTemplate(new DeleteLocation(placeId));
	}

	public void savePlace(PlaceWrapper place) {
		Location location = selectLocation(place.getId());
		if (location == null) {
			location = new Location();
			location.setPlaceId(place.getId());
			location.setName(place.getName());
			location.setAddress(place.getAddress());
			location.setLatitude(place.getLat());
			location.setLongitude(place.getLon());
			location.setVisits(0);
			insertLocation(location);
		}
	}
}
