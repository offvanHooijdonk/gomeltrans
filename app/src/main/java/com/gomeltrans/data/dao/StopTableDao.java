package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.Constants;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;

import java.util.Calendar;
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
            for (String time : table.getTimes()) {
                saveTransportStopTime(db, table.getTransport().getId(), stop.getId(), time, table.getDayTypeCode());
            }
        }
    }

    /**
     * @param
     * @param timeFrom
     * @return next time this transport comes to this stop, or null if it will not today
     */
    public String getNextTimeThisDay(Long transportId, Long stopId, Date timeFrom, Integer minutes, int dayType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nextTime;

        String[] params;
        String timeFromShifted = DBHelper.shiftTimeForDB(Constants.getDBTimeFormat().format(timeFrom));
        if (minutes != null) {
            Calendar calendarTo = Calendar.getInstance();
            calendarTo.setTime(timeFrom);
            calendarTo.add(Calendar.MINUTE, minutes);

            params = new String[]{String.valueOf(transportId), String.valueOf(stopId), DBHelper.shiftTimeForDB(timeFromShifted),
                    DBHelper.shiftTimeForDB(Constants.getDBTimeFormat().format(calendarTo.getTime())), String.valueOf(dayType)};
        } else {
            params = new String[]{String.valueOf(transportId), String.valueOf(stopId), DBHelper.shiftTimeForDB(timeFromShifted), String.valueOf(dayType)};
        }
        // FIXME possible bug with transport day roll on 5 AM
        Cursor cursor = db.query(true, StopTable.TABLE, new String[]{"MIN(" + StopTable.TIME + ")"},
                StopTable.TRANSPORT_ID + " =? AND " + StopTable.STOP_ID + " =? AND " +
                        " CAST(" + StopTable.TIME + " as integer) >=CAST(? as integer) " +
                        (minutes != null ? " AND CAST(" + StopTable.TIME + " as integer) <=CAST(? as integer) " : "") +
                        " AND " + StopTable.DAY_TYPE_CODE + " =? ",
                params, null, null, null, null);

        if (cursor.moveToFirst()) {
            nextTime = DBHelper.unshiftTimeFromDB(cursor.getString(0));
        } else {
            nextTime = null;
        }
        cursor.close();

        return nextTime;
    }


    public String getFirstTime(Long transportId, Long stopId, int dayTypeCode) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String nextTime;

        Cursor cursor = db.query(true, StopTable.TABLE, new String[]{StopTable.TIME}, StopTable.TRANSPORT_ID + " =? AND " +
                        StopTable.STOP_ID + " =? AND " + StopTable.DAY_TYPE_CODE + " =? ",
                new String[]{String.valueOf(transportId), String.valueOf(stopId), String.valueOf(dayTypeCode)}, null, null, "CAST(" + StopTable.TIME + " as integer)", "1");

        if (cursor.moveToFirst()) {
            nextTime = DBHelper.unshiftTimeFromDB(cursor.getString(0));
        } else {
            nextTime = null;
        }
        cursor.close();

        return nextTime;
    }

    private void saveTransportStopTime(SQLiteDatabase db, Long transportId, Long stopId, String time, int dayType) {
        ContentValues cv = new ContentValues();
        cv.put(StopTable.TRANSPORT_ID, transportId);
        cv.put(StopTable.STOP_ID, stopId);
        cv.put(StopTable.TIME, DBHelper.shiftTimeForDB(time));
        cv.put(StopTable.DAY_TYPE_CODE, dayType);

        db.insert(StopTable.TABLE, null, cv);
    }

}
