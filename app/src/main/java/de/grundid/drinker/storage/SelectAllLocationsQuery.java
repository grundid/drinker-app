package de.grundid.drinker.storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.grundid.android.db.SqlTemplate;

import java.util.List;

public class SelectAllLocationsQuery implements SqlTemplate<List<Location>> {

	public List<Location> doWithDb(SQLiteDatabase db) {
		StringBuilder selectColumns = new StringBuilder();
		DaoManager.appendSelectColumns(selectColumns, LocationTableHandler.SELECT_COLUMNS);
		String orderStatement =
				" ORDER BY " + LocationTableHandler.COL_VISITS + " DESC, " + LocationTableHandler.COL_NAME;
		String[] params = new String[] {};
		return DaoManager.mapRowsFromCursor(
				db.rawQuery("SELECT " + selectColumns.toString() + " FROM " + LocationTableHandler.TABLE_NAME
						+ orderStatement, params), new LocationTableHandler());
	}
}
