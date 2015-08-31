package com.gomeltrans.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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


    private static final String DB_NAME = "gomeltrans";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 5);
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
                StopTable.TRANSPORT_STOP_ID + " integer not null, " +
                StopTable.TIME + " text not null, " + StopTable.ACTIVE + " integer default 1 " + ");";
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
}
