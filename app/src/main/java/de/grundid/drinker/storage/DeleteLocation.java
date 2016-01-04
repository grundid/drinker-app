package de.grundid.drinker.storage;

import android.database.sqlite.SQLiteDatabase;

import de.grundid.android.db.SqlTemplate;

/**
 * Created by lukas on 04.01.16.
 */
public class DeleteLocation implements SqlTemplate<Void> {
    private String placeId;

    public DeleteLocation(String placeId) {
        this.placeId = placeId;
    }

    public Void doWithDb(SQLiteDatabase db){
        LocationTableHandler tableHandler = new LocationTableHandler();
        db.delete(LocationTableHandler.TABLE_NAME, LocationTableHandler.COL_PLACE_ID + "=?", new String[]{placeId});
        return null;
    }
}
