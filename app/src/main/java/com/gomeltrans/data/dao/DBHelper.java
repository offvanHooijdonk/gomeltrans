package com.gomeltrans.data.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gomeltrans.Constants;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "gomeltrans";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createStatement = "CREATE TABLE " + Transport.TABLE + " (" + Transport.ID +
                " integer not null, " + Transport.NUMBER + " text not null, " + Transport.ROUTE + " text not null, " +
                Transport.TYPE + " integer not null, " + Transport.FAVOURITE + " integer default 0, " + Transport.ACTIVE + " integer default 1 " + ");" +
                " CREATE TABLE " + Stop.TABLE + " (" + Stop.ID + " integer not null, " + Stop.NAME + " text not null, " + Stop.COMMENT + " text, " +
                Stop.FAVOURITE + " integer default 0, " + Stop.ACTIVE + " integer default 1 " + ");";
        try {
            String[] createQueries = createStatement.split(";");
            for (String q : createQueries) {
                db.execSQL(q);
            }
        } catch (Exception e) {
            Log.e(Constants.LOG_TAG, "Error creating DB.", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTables = "DROP TABLE reminders;";
        try {
            String[] dropQueries = dropTables.split(";");
            for (String q : dropQueries) {
                db.execSQL(q);
            }
        } catch(Exception e) {
            Log.w(Constants.LOG_TAG, e);
        }
        onCreate(db);

    }
}
