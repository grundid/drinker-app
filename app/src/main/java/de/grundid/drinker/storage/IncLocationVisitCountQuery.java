package de.grundid.drinker.storage;

import android.database.sqlite.SQLiteDatabase;
import de.grundid.android.db.SqlTemplate;

public class IncLocationVisitCountQuery implements SqlTemplate<Void> {

	private String placeId;

	public IncLocationVisitCountQuery(String placeId) {
		this.placeId = placeId;
	}

	public Void doWithDb(SQLiteDatabase db) {
		Object[] args = { System.currentTimeMillis(), placeId, System.currentTimeMillis() - 12 * 60 * 60 * 1000 };
		String sql = "UPDATE " + LocationTableHandler.TABLE_NAME + " SET " + LocationTableHandler.COL_LAST_VISIT + " =?, "
				+ LocationTableHandler.COL_VISITS + " = 1 + " + LocationTableHandler.COL_VISITS + " WHERE "
				+ LocationTableHandler.COL_PLACE_ID + " =? AND " +
				LocationTableHandler.COL_LAST_VISIT + " < ?";
		db.execSQL(sql, args);
		return null;
	}
}
