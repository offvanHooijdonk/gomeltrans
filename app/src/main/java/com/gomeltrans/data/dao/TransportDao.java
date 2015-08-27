package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.model.Transport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class TransportDao {
    private Context ctx;
    private DBHelper dbHelper;

    public TransportDao(Context context) {
        this.ctx = context;
        dbHelper = new DBHelper(ctx);
    }

    public void storeList(List<Transport> list) {
        for (Transport bean : list) {
            save(bean);
        }
    }

    public void save(Transport bean) {
        if (getById(bean.getId()) != null) {
            update(bean);
        } else {
            insert(bean);
        }
    }

    public void insert(Transport bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Transport.ID, bean.getId());
        cv.put(Transport.NUMBER, bean.getNumberName());
        cv.put(Transport.ROUTE, bean.getRouteName());
        cv.put(Transport.TYPE, bean.getType().getCode());

        db.insert(Transport.TABLE, null, cv);
    }

    public void update(Transport bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Transport.NUMBER, bean.getNumberName());
        cv.put(Transport.ROUTE, bean.getRouteName());
        cv.put(Transport.TYPE, bean.getType().getCode());

        db.update(Transport.TABLE, cv, Transport.ID + "=?", new String[]{String.valueOf(bean.getId())});
    }

    public Transport getById(Long id) {
        Transport bean = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(Transport.TABLE, null, Transport.ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            bean = cursorToBean(cursor);
        }
        cursor.close();
        return bean;
    }

    public List<Transport> getList(boolean favouritesOnly) {
        List<Transport> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (favouritesOnly) {
            cursor = db.query(Transport.TABLE, null, Transport.ACTIVE + "=?", new String[]{String.valueOf(1), String.valueOf(1)}, null, null, null);
        } else {
            cursor = db.query(Transport.TABLE, null, Transport.ACTIVE + "=?", new String[]{String.valueOf(1)}, null, null, null);
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

        cv.put(Transport.FAVOURITE, favourite);
        db.update(Transport.TABLE, cv, Transport.ID + "=?", new String[]{String.valueOf(id)});
    }

    private Transport cursorToBean(Cursor c) {
        Transport bean = new Transport();
        bean.setId(c.getLong(c.getColumnIndex(Transport.ID)));
        bean.setNumberName(c.getString(c.getColumnIndex(Transport.NUMBER)));
        bean.setRouteName(c.getString(c.getColumnIndex(Transport.ROUTE)));
        bean.setType(Transport.TRANSPORT_TYPE.values()[(c.getInt(c.getColumnIndex(Transport.TYPE)))]);
        bean.setFavourite(c.getInt(c.getColumnIndex(Transport.FAVOURITE)) > 0);
        bean.setActive(c.getInt(c.getColumnIndex(Transport.ACTIVE)) > 0);

        return bean;
    }

}
