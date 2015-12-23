package de.grundid.drinker.storage;

import android.database.sqlite.SQLiteDatabase;
import de.grundid.android.db.SqlTemplate;
import de.grundid.drinker.LocationModel;

import java.util.List;

public class SelectLocationQuery implements SqlTemplate<Location> {

	private String placeId;

	public SelectLocationQuery(String placeId) {
		this.placeId = placeId;
	}

	public Location doWithDb(SQLiteDatabase db) {
		StringBuilder selectColumns = new StringBuilder();
		DaoManager.appendSelectColumns(selectColumns, LocationTableHandler.SELECT_COLUMNS);
		String whereStatement = " WHERE " + LocationTableHandler.COL_PLACE_ID + " = ?";
		String[] params = new String[] { placeId };
		List<Location> locations = DaoManager.mapRowsFromCursor(
				db.rawQuery("SELECT " + selectColumns.toString() + " FROM " + LocationTableHandler.TABLE_NAME
						+ whereStatement, params), new LocationTableHandler());
		if (locations.isEmpty()) {
			return null;
		}
		else
			return locations.get(0);
	}
}
