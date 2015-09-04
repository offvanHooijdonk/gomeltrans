package com.gomeltrans;

import android.annotation.SuppressLint;
import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class Constants {
    public static final String LOG_TAG = "gomeltrans";
    private static final String PREF_UPDATE_DATE = "pref_update_date";
    private static final String FILE_PREF_LOCAL = "local.xml";

    @SuppressLint("SimpleDateFormat")
    public static DateFormat getTimeFormat(Context ctx) {
        return new SimpleDateFormat(android.text.format.DateFormat.is24HourFormat(ctx) ? "HH:mm" : "hh:mm a");
    }

    @SuppressLint("SimpleDateFormat")
    public static DateFormat getDBTimeFormat() {
        return new SimpleDateFormat("HH:mm");
    }

    public static String getUpdateDate(Context ctx) {
        String dateString = ctx.getSharedPreferences(FILE_PREF_LOCAL, Context.MODE_PRIVATE).getString(PREF_UPDATE_DATE, null);
        return dateString != null ? dateString : "?";
    }

    public static void saveUpdateDate(Context ctx, Date date) {
        ctx.getSharedPreferences(FILE_PREF_LOCAL, Context.MODE_PRIVATE).edit().putString(PREF_UPDATE_DATE, formatUpdateDate(ctx, date)).commit();
    }

    private static String formatUpdateDate(Context ctx, Date date) {
        String dateString = SimpleDateFormat.getDateInstance().format(date);
        String timeString = getTimeFormat(ctx).format(date);

        return String.format("%s %s", dateString, timeString);
    }
}
