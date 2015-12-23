package de.grundid.drinker.storage;

import android.database.sqlite.SQLiteDatabase;
import de.grundid.android.db.SqlTemplate;
import de.grundid.drinker.LocationModel;

public class InsertLocation implements SqlTemplate<Void> {

	private Location location;

	public InsertLocation(Location location) {
		this.location = location;
	}

	public Void doWithDb(SQLiteDatabase db) {
		LocationTableHandler tableHandler = new LocationTableHandler();
		db.insert(LocationTableHandler.TABLE_NAME, null,
				tableHandler.createContentValues(location));
		return null;
	}
}
