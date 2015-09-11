package com.gomeltrans.data.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.Constants;
import com.gomeltrans.data.dao.DBHelper;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Yahor_Fralou on 9/10/2015.
 */
public class StopService {

    private Context ctx;
    private DBHelper dbHelper;

    public StopService(Context context) {
        this.ctx = context;

        dbHelper = new DBHelper(ctx);
    }

    public List<StopTable> getUpcomingTransport(Stop stop, Date dateFrom, int minutes, StopTable.DAY_TYPE dayType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<StopTable> stopTableList = new ArrayList<>();
        TransportDao transportDao = new TransportDao(ctx);

        StopTable.DAY_TYPE queryDayType = dayType != null ? dayType : StopTable.getDayType(dateFrom);
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateFrom);
        calendarTo.add(Calendar.MINUTE, minutes);

        Cursor cursor = db.query(StopTable.TABLE, null, StopTable.STOP_ID + "=? AND " + StopTable.DAY_TYPE_CODE + "=? AND " +
                        "CAST(" + StopTable.TIME + " as integer)>=CAST(? as integer) AND " +
                        "CAST(" + StopTable.TIME + " as integer)<=CAST(? as integer)",
                new String[]{String.valueOf(stop.getId()), String.valueOf(queryDayType.getCode()),
                        DBHelper.shiftTimeForDB(Constants.getDBTimeFormat().format(dateFrom)), DBHelper.shiftTimeForDB(Constants.getDBTimeFormat().format(calendarTo.getTime()))},
                null, null, "CAST(" + StopTable.TIME + " as integer)");

        while (cursor.moveToNext()) {
            StopTable table = new StopTable();

            table.setId(cursor.getLong(cursor.getColumnIndex(StopTable.ID)));
            Long transportId = cursor.getLong(cursor.getColumnIndex(StopTable.TRANSPORT_ID));
            table.setTransport(transportDao.getById(transportId));
            table.setTimeUpcoming(DBHelper.unshiftTimeFromDB(cursor.getString(cursor.getColumnIndex(StopTable.TIME))));

            stopTableList.add(table);
        }
        cursor.close();

        return stopTableList;
    }
}
