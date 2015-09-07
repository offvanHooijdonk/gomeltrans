package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gomeltrans.Constants;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;

import java.util.Date;

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
                    saveTransportStopTime(db, tsId, time, table.getDayTypeCode());
                }
            } else {
                // TODO implement custom exception and rollback system
            }
        }
    }

    /**
     * @param transportStopId
     * @param timeFrom
     * @return next time this transport comes to this stop, or null if it will not today
     */
    public String getNextTimeThisDay(Long transportStopId, Date timeFrom, int dayType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nextTime;

        String timeFromShifted = shiftTimeForDB(Constants.getDBTimeFormat().format(timeFrom));

        Cursor cursor = db.query(true, StopTable.TABLE, new String[]{"MIN(" + StopTable.TIME + ")"}, StopTable.TRANSPORT_STOP_ID + " =? AND " +
                        " CAST(" + StopTable.TIME + " as integer) >=CAST(? as integer) " +
                        " AND " + StopTable.DAY_TYPE_CODE + " =? ",
                new String[]{String.valueOf(transportStopId), shiftTimeForDB(timeFromShifted), String.valueOf(dayType)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            nextTime = unshiftTimeFromDB(cursor.getString(0));
        } else {
            nextTime = null;
        }
        cursor.close();

        return nextTime;
    }

    public String getFirstTime(Long transportStopId, int dayTypeCode) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nextTime;

        Cursor cursor = db.query(true, StopTable.TABLE, new String[]{StopTable.TIME}, StopTable.TRANSPORT_STOP_ID + " =? AND " +
                        StopTable.DAY_TYPE_CODE + " =? ",
                new String[]{String.valueOf(transportStopId), String.valueOf(dayTypeCode)}, null, null, "CAST(" + StopTable.TIME + " as integer)", "1");

        if (cursor.moveToFirst()) {
            nextTime = unshiftTimeFromDB(cursor.getString(0));
        } else {
            nextTime = null;
        }
        cursor.close();

        return nextTime;
    }

    private void saveTransportStopTime(SQLiteDatabase db, Long transportStopId, String time, int dayType) {
        ContentValues cv = new ContentValues();
        cv.put(StopTable.TRANSPORT_STOP_ID, transportStopId);
        cv.put(StopTable.TIME, shiftTimeForDB(time));
        cv.put(StopTable.DAY_TYPE_CODE, dayType);

        db.insert(StopTable.TABLE, null, cv);
    }

    private String shiftTimeForDB(String timeString) {
        String res;
        if (!TextUtils.isEmpty(timeString)) {
            String[] timeSplit = timeString.split(StopTable.TIME_DELIMITER);
            String hourString = timeSplit[0];
            int hour = Integer.valueOf(hourString);
            if (hour >= StopTable.HOUR_SHIFT_START_WITH && hour <= StopTable.HOUR_SHIFT_END_WITH) {
                hour += StopTable.HOUR_SHIFT_BY;

                res = timeString.replaceFirst(timeSplit[0], String.valueOf(hour));
                //res = String.valueOf(hour) + TIME_DELIMITER + timeSplit[1];
            } else {
                res = timeString;
            }
        } else {
            res = null;
        }
        return res;
    }

    private String unshiftTimeFromDB(String timeDB) {
        String res;
        if (!TextUtils.isEmpty(timeDB)) {
            String[] timeSplit = timeDB.split(StopTable.TIME_DELIMITER);
            String hourString = timeSplit[0];
            int hour = Integer.valueOf(hourString);

            if (hour >= (StopTable.HOUR_SHIFT_START_WITH + StopTable.HOUR_SHIFT_BY)) {
                hour = hour - StopTable.HOUR_SHIFT_BY;

                res = timeDB.replaceFirst(timeSplit[0], String.valueOf(hour));
            } else {
                res = timeDB;
            }
        } else {
            res = null;
        }
        return res;
    }
}
