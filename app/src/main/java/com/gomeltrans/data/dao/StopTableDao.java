package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;

/**
 * Created by Yahor_Fralou on 8/28/2015.
 */
public class StopTableDao {
    private Context ctx;
    private DBHelper dbHelper;

    public StopTableDao(Context context) {
        this.ctx = context;
        dbHelper = new DBHelper(ctx);
    }

    public void clearAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(StopTable.TABLE, null, null);
    }

    public void saveStopTable(Stop stop) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        TransportStopDao tsDao = new TransportStopDao(ctx);

        for (StopTable table : stop.getStopTables()) {
            Long tsId = tsDao.findTransportStopId(table.getTransport().getId(), stop.getId());
            if (tsId != null) {
                for (String time : table.getTimes()) {
                    saveTransportStopTime(db, tsId, time);
                }
            } else {
                // TODO implement custom exception and rollback system
            }
        }
    }

    private void saveTransportStopTime(SQLiteDatabase db, Long transportStopId, String time) {
        ContentValues cv = new ContentValues();
        cv.put(StopTable.TRANSPORT_STOP_ID, transportStopId);
        cv.put(StopTable.TIME, time);

        db.insert(StopTable.TABLE, null, cv);
    }
}
