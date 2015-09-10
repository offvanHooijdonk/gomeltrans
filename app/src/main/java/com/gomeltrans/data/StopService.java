package com.gomeltrans.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.gomeltrans.Constants;
import com.gomeltrans.data.dao.DBHelper;
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

    public List<StopTable> getUpcomingTransport(Long stopId, Date dateFrom, int minutes, StopTable.DAY_TYPE dayType) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<StopTable> stopTableList = new ArrayList<>();

        StopTable.DAY_TYPE queryDayType = dayType != null ? dayType : StopTable.getDayType(dateFrom);
        Calendar calendarTo = Calendar.getInstance();
        calendarTo.setTime(dateFrom);
        calendarTo.add(Calendar.MINUTE, minutes);

        db.query(StopTable.TABLE, null, StopTable.STOP_ID + "=? AND " + StopTable.DAY_TYPE_CODE + "=? AND " +
                        "CAST(" + StopTable.TIME + " as integer)>=CAST(? as integer) AND " +
                "CAST(" + StopTable.TIME + " as integer)<=CAST(? as integer)",
                new String[]{String.valueOf(stopId), String.valueOf(queryDayType.getCode()),
                        DBHelper.shiftTimeForDB(Constants.getDBTimeFormat().format(dateFrom)), DBHelper.shiftTimeForDB(Constants.getDBTimeFormat().format(calendarTo.getTime()))},
                null, null, "CAST(" + StopTable.TIME + " as integer)");

        return stopTableList;
    }
}
