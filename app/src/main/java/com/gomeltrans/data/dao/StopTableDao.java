package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.Constants;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;

import java.util.Date;

/**
 * Created by Yahor_Fralou on 8/28/2015.
 */
public class StopTableDao {
    private static final int HOUR_SHIFT_START_WITH = 0;
    private static final int HOUR_SHIFT_END_WITH = 4;
    private static final int HOUR_SHIFT_BY = 24;
    private static final String TIME_DELIMITER = ":";

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

    /**
     *
     * @param transportStopId
     * @param timeFrom
     * @return next time this transport comes to this stop, or null if it will not today
     */
    public String getNextTimeToday(Long transportStopId, Date timeFrom) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nextTime;

        String timeFromShifted = shiftTimeForDB(Constants.getDBTimeFormat().format(timeFrom));

        Cursor cursor = db.query(true, StopTable.TABLE, new String[]{"MIN(" + StopTable.TIME + ")"}, StopTable.TRANSPORT_STOP_ID + " =? AND " + StopTable.TIME + " >=? ",
                new String[]{String.valueOf(transportStopId), shiftTimeForDB(timeFromShifted)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            nextTime = unshiftTimeFromDB(cursor.getString(0));
        } else {
            nextTime = null;
        }
        cursor.close();

        return nextTime;
    }

    public String getFirstTime(Long transportStopId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nextTime;

        Cursor cursor = db.query(true, StopTable.TABLE, new String[]{"MIN(" + StopTable.TIME + ")"}, StopTable.TRANSPORT_STOP_ID + " =? ",
                new String[]{String.valueOf(transportStopId)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            nextTime = unshiftTimeFromDB(cursor.getString(0));
        } else {
            nextTime = null;
        }
        cursor.close();

        return nextTime;
    }

    private void saveTransportStopTime(SQLiteDatabase db, Long transportStopId, String time) {
        ContentValues cv = new ContentValues();
        cv.put(StopTable.TRANSPORT_STOP_ID, transportStopId);
        cv.put(StopTable.TIME, shiftTimeForDB(time));

        db.insert(StopTable.TABLE, null, cv);
    }

    private String shiftTimeForDB(String timeString) {
        String res;
        String[] timeSplit = timeString.split(TIME_DELIMITER);
        String hourString = timeSplit[0];
        int hour = Integer.valueOf(hourString);
        if (hour >= HOUR_SHIFT_START_WITH && hour <= HOUR_SHIFT_END_WITH) {
            hour += HOUR_SHIFT_BY;

            res = timeString.replaceFirst(timeSplit[0], String.valueOf(hour));
            //res = String.valueOf(hour) + TIME_DELIMITER + timeSplit[1];
        } else {
            res = timeString;
        }

        return res;
    }

    private String unshiftTimeFromDB(String timeDB) {
        String res;
        String[] timeSplit = timeDB.split(TIME_DELIMITER);
        String hourString = timeSplit[0];
        int hour = Integer.valueOf(hourString);

        if (hour >= (HOUR_SHIFT_START_WITH + HOUR_SHIFT_BY)) {
            hour = hour - HOUR_SHIFT_BY;

            res = timeDB.replaceFirst(timeSplit[0], String.valueOf(hour));
        } else {
            res = timeDB;
        }

        return res;
    }
}
