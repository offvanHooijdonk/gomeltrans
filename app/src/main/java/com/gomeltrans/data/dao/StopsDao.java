package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.model.Stop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class StopsDao {
    private Context ctx;
    private DBHelper dbHelper;

    public StopsDao(Context context) {
        this.ctx = context;
        dbHelper = new DBHelper(ctx);
    }

    public void storeList(List<Stop> list) {
        for (Stop bean : list) {
            save(bean);
        }
    }

    public void clearAllBut(List<Stop> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(Stop.TABLE, Stop.ID + " IN (" + dbHelper.generatePlaceholders(list.size()) + ")", dbHelper.toIdsStringArray(list));
    }

    public void save(Stop bean) {
        if (getById(bean.getId()) != null) {
            update(bean);
        } else {
            insert(bean);
        }
    }

    public void insert(Stop bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Stop.ID, bean.getId());
        cv.put(Stop.NAME, bean.getName());
        cv.put(Stop.COMMENT, bean.getComment());

        db.insert(Stop.TABLE, null, cv);
    }

    public void update(Stop bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Stop.NAME, bean.getName());
        cv.put(Stop.COMMENT, bean.getComment());

        db.update(Stop.TABLE, cv, Stop.ID + "=?", new String[]{String.valueOf(bean.getId())});
    }

    public Stop getById(Long id) {
        Stop bean = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Stop.TABLE, null, Stop.ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            bean = cursorToBean(cursor);
        }
        cursor.close();
        return bean;
    }

    public List<Stop> getList(boolean favouritesOnly) {
        List<Stop> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (favouritesOnly) {
            cursor = db.query(Stop.TABLE, null, Stop.ACTIVE + "=?", new String[]{String.valueOf(1), String.valueOf(1)}, null, null, null);
        } else {
            cursor = db.query(Stop.TABLE, null, Stop.ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, null);
        }

        while (cursor.moveToNext()) {
            list.add(cursorToBean(cursor));
        }
        cursor.close();

        return list;
    }

    public void setFavourite(Long id, boolean favourite) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Stop.FAVOURITE, favourite);
        db.update(Stop.TABLE, cv, Stop.ID + "=?", new String[]{String.valueOf(id)});
    }

    private Stop cursorToBean(Cursor c) {
        Stop bean = new Stop();
        bean.setId(c.getLong(c.getColumnIndex(Stop.ID)));
        bean.setName(c.getString(c.getColumnIndex(Stop.NAME)));
        bean.setComment(c.getString(c.getColumnIndex(Stop.COMMENT)));
        bean.setFavourite(c.getInt(c.getColumnIndex(Stop.FAVOURITE)) > 0);
        bean.setActive(c.getInt(c.getColumnIndex(Stop.ACTIVE)) > 0);

        return bean;
    }
}
