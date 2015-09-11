package com.gomeltrans.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.gomeltrans.Constants;
import com.gomeltrans.model.BaseBean;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.model.Transport;
import com.gomeltrans.model.TransportStops;

import java.util.List;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String WILDCARD_MULT = "%";

    private static final String DB_NAME = "gomeltrans";
    private static final int DB_VERSION = 7;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE TABLE " + Transport.TABLE + " (" + Transport.ID + " integer primary key, " +
                Transport.NUMBER + " text not null, " + Transport.ROUTE + " text not null, " +
                Transport.TYPE + " integer not null, " + Transport.FAVOURITE + " integer default 0, " + Transport.ACTIVE + " integer default 1 " + ");" +
                " CREATE TABLE " + Stop.TABLE + " (" + Stop.ID + " integer primary key, " + Stop.NAME + " text not null, " + Stop.COMMENT + " text, " +
                Stop.FAVOURITE + " integer default 0, " + Stop.ACTIVE + " integer default 1 " + ");" +
                " CREATE TABLE " + TransportStops.TABLE + " (" + TransportStops.ID + " integer primary key autoincrement, " +
                TransportStops.TRANSPORT_ID + " integer not null, " + TransportStops.STOP_ID + " integer not null, " +
                TransportStops.DIRECTION_INDEX + " integer not null, " + TransportStops.ORDER_NUMBER + " integer not null, " +
                TransportStops.ACTIVE + " integer default 1 " + ");" +
                " CREATE TABLE " + StopTable.TABLE + " (" + StopTable.ID + " integer primary key autoincrement, " +
                StopTable.TRANSPORT_ID + " integer not null, " + StopTable.STOP_ID + " integer not null, " +
                StopTable.DAY_TYPE_CODE + " integer not null, " + StopTable.TIME + " text not null, " +
                StopTable.ACTIVE + " integer default 1 " + ");";
        String[] createQueries = createStatement.split(";");
        for (String q : createQueries) {
            try {
                db.execSQL(q);

            } catch (Exception e) {
                Log.e(Constants.LOG_TAG, "Error creating DB.", e);
            }
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTables = "DROP TABLE " + StopTable.TABLE + "; " +
                "DROP TABLE " + TransportStops.TABLE + "; " +
                "DROP TABLE " + Transport.TABLE + "; " +
                "DROP TABLE " + Stop.TABLE + "; ";
        String[] dropQueries = dropTables.split(";");
        for (String q : dropQueries) {
            try {
                db.execSQL(q);

            } catch (Exception e) {
                Log.w(Constants.LOG_TAG, e);
            }
        }

        onCreate(db);
    }

    public String generatePlaceholders(int size) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                str.append(",");
            }
            str.append("?");
        }

        return str.toString();
    }

    public String[] toIdsStringArray(List<? extends BaseBean> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = String.valueOf(list.get(i).getId().longValue());
        }

        return array;
    }

    public static String shiftTimeForDB(String timeString) {
        String res;
        if (!TextUtils.isEmpty(timeString)) {
            String[] timeSplit = timeString.split(StopTable.DELIMITER_TIME);
            String hourString = timeSplit[0];
            int hour = Integer.valueOf(hourString);
            if (hour >= StopTable.HOUR_SHIFT_START_WITH && hour <= StopTable.HOUR_SHIFT_END_WITH) {
                hour += StopTable.HOUR_SHIFT_BY;

                res = timeString.replaceFirst(timeSplit[0], String.valueOf(hour));
                //res = String.valueOf(hour) + DELIMITER_TIME + timeSplit[1];
            } else {
                res = timeString;
            }
        } else {
            res = null;
        }
        return res;
    }

    public static String unshiftTimeFromDB(String timeDB) {
        String res;
        if (!TextUtils.isEmpty(timeDB)) {
            String[] timeSplit = timeDB.split(StopTable.DELIMITER_TIME);
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
