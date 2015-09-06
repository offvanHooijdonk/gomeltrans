package com.gomeltrans.data.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.gomeltrans.model.TransportStops;

import java.util.ArrayList;
import java.util.Date;
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

    public void clearAllBut(List<Transport> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(Transport.TABLE, Transport.ID + " NOT IN (" + dbHelper.generatePlaceholders(list.size()) + ")", dbHelper.toIdsStringArray(list));
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
        cv.put(Transport.TYPE, bean.getTypeNumber());

        db.insert(Transport.TABLE, null, cv);
    }

    public void update(Transport bean) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(Transport.NUMBER, bean.getNumberName());
        cv.put(Transport.ROUTE, bean.getRouteName());
        cv.put(Transport.TYPE, bean.getTypeNumber());

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

    public List<Transport> getList(int type, boolean favouritesOnly, boolean sortOnFavourites) {
        List<Transport> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        if (favouritesOnly) {
            cursor = db.query(Transport.TABLE, null, Transport.TYPE + " =? AND " + Transport.ACTIVE + "=? AND " + Transport.FAVOURITE + " =? ",
                    new String[]{String.valueOf(type), String.valueOf(1), String.valueOf(1)}, null, null, "CAST(" + Transport.NUMBER + " as integer)");
        } else {
            cursor = db.query(Transport.TABLE, null, Transport.TYPE + " =? aND " + Transport.ACTIVE + " =? ",
                    new String[]{String.valueOf(type), String.valueOf(1)}, null, null, (sortOnFavourites ? Transport.FAVOURITE + " desc," :
                            "") +
                            "CAST(" + Transport.NUMBER + " as integer)");
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

    public List<TransportStops> getStopsForTransport(Transport transport, TransportStops.DIRECTION direction) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<TransportStops> list = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT " + "s.*, ts." + TransportStops.ID + " as ts_id FROM " + Stop.TABLE + " s," + TransportStops.TABLE + " ts " +
                        " WHERE s." + Stop.ID + " = ts." + TransportStops.STOP_ID + " AND ts." + TransportStops.TRANSPORT_ID + "=? AND ts." +
                        TransportStops.DIRECTION_INDEX + " =? AND ts." + TransportStops.ACTIVE + " = ? ORDER BY " + TransportStops.ORDER_NUMBER,
                new String[]{String.valueOf(transport.getId()), String.valueOf(direction.getCode()), String.valueOf(1)});

        StopsDao stopsDao = new StopsDao(ctx);
        while (cursor.moveToNext()) {
            TransportStops ts = new TransportStops();
            Stop stop = stopsDao.cursorToBean(cursor);
            ts.setStop(stop);
            ts.setTransport(transport);
            ts.setId(cursor.getLong(cursor.getColumnIndex("ts_id")));
            list.add(ts);
        }
        cursor.close();

        return list;
    }

    public List<TransportStops> getTransportStopNextTable(Transport transport, TransportStops.DIRECTION direction, Date date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        List<TransportStops> list = getStopsForTransport(transport, direction);

        StopTableDao stopTableDao = new StopTableDao(ctx);
        for (TransportStops ts : list) {
            String nextTime = stopTableDao.getNextTimeToday(ts.getId(), date);
            if (!TextUtils.isEmpty(nextTime)) {
                ts.setNextTime(nextTime);
            } else {
                String firstTime = stopTableDao.getFirstTime(ts.getId());
                ts.setFirstTime(firstTime);
            }
        }

        return list;
    }

    private Transport cursorToBean(Cursor c) {
        Transport bean = new Transport();
        bean.setId(c.getLong(c.getColumnIndex(Transport.ID)));
        bean.setNumberName(c.getString(c.getColumnIndex(Transport.NUMBER)));
        bean.setRouteName(c.getString(c.getColumnIndex(Transport.ROUTE)));
        bean.setTypeNumber(c.getInt(c.getColumnIndex(Transport.TYPE)));
        bean.setFavourite(c.getInt(c.getColumnIndex(Transport.FAVOURITE)) > 0);
        bean.setActive(c.getInt(c.getColumnIndex(Transport.ACTIVE)) > 0);

        return bean;
    }

}
