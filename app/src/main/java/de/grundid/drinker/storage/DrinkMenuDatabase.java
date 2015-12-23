package de.grundid.drinker.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import de.grundid.android.db.DbHelper;
import de.grundid.android.db.TableHandler;

public class DrinkMenuDatabase extends DbHelper {

	private static final int DB_VERSION = 1;
	private static final String DB_NAME = "drinkmenu.db";
	private static final TableHandler[] TABLE_HANDLERS = {new LocationTableHandler()};

	public DrinkMenuDatabase(Context context) {
		super(context, DB_NAME, DB_VERSION, TABLE_HANDLERS);
	}
}
