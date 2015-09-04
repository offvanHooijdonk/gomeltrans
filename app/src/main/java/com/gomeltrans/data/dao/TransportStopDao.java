package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.gomeltrans.model.TransportStops;

import java.util.List;

/**
 * Created by Yahor_Fralou on 8/28/2015.
 */
public class TransportStopDao {
    private Context ctx;
    private DBHelper dbHelper;

    public TransportStopDao(Context context) {
        this.ctx = context;
        dbHelper = new DBHelper(ctx);
    }

    public void clearAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(TransportStops.TABLE, null, null);
    }

    public void saveTransportStops(Transport bean) {
        saveTransportStops(bean.getId(), bean.getStopsForward(), TransportStops.DIRECTION.FORWARD);
        saveTransportStops(bean.getId(), bean.getStopsBackward(), TransportStops.DIRECTION.BACKWARD);
    }

    private void saveTransportStops(Long transportId, List<Stop> stops, TransportStops.DIRECTION dir) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int index = 1;
        for (Stop stop: stops) {
            ContentValues cv = new ContentValues();
            cv.put(TransportStops.STOP_ID, stop.getId());
            cv.put(TransportStops.TRANSPORT_ID, transportId);
            cv.put(TransportStops.DIRECTION_INDEX, dir.getCode());
            cv.put(TransportStops.ORDER_NUMBER, index);

            db.insert(TransportStops.TABLE, null, cv);
            index++;
        }
    }

    public Long findTransportStopId(Long transportId, Long stopId) {
        Long res = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(TransportStops.TABLE, new String[]{TransportStops.ID}, TransportStops.TRANSPORT_ID + " =? AND " + TransportStops.STOP_ID + " =? ",
                new String[]{String.valueOf(transportId), String.valueOf(stopId)}, null, null, null);

        if (cursor.moveToFirst()) {
            res = cursor.getLong(cursor.getColumnIndex(TransportStops.ID));
        }

        return res;
    }

}
