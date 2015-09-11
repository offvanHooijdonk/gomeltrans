package com.gomeltrans.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.gomeltrans.R;
import com.gomeltrans.model.StopTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Yahor_Fralou on 9/8/2015.
 */
public class AppHelper {
    public static final int SEARCH_STOP_START_LETTERS_COUNT = 2;
    public static final float FAV_BACKGR_ALPHA = 0.2f;
    public static final int TABLE_UPCOMING_MINUTES =240;

    private static final String DAY_INFO_DELIMITER = ", ";

    /**
     * Creates info on that day: is it Today, Date, Type of day: working/weekend
     * @param ctx
     * @param dayType If specific type of day is chosen. If <code>null</code>, then will be calculated based on <code>datePicked</code>
     * @param datePicked specific date for which to show info. Not used if <code>dayType</code> not <code>null</code>. If the value is <code>null</code>,
     *                   count as today date and show "Today" message.
     * @return
     */
    public static String createDayInfo(Context ctx, @Nullable StopTable.DAY_TYPE dayType, @Nullable Date datePicked) {
        StringBuilder str = new StringBuilder();
        StopTable.DAY_TYPE dt;
        if (dayType == null) {
            if (datePicked == null) {
                Calendar now = Calendar.getInstance();
                Date nowDate = StopTable.getDateWithShift(now);
                str.append(ctx.getString(R.string.info_today)).append(DAY_INFO_DELIMITER).append(SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(nowDate));
                dt = StopTable.getDayType(nowDate);
            } else {
                str.append(SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(datePicked));
                dt = StopTable.getDayType(datePicked);
            }
            str.append(DAY_INFO_DELIMITER);
        } else {
            dt = dayType;
        }

        if (dt == StopTable.DAY_TYPE.WEEKEND) {
            str.append(ctx.getString(R.string.info_weekend_day));
        } else {
            str.append(ctx.getString(R.string.info_working_day));
        }

        return str.toString();
    }

    public static CharSequence colorizeTextForSnackbar(Context ctx, String text) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);
        ssb.setSpan(new ForegroundColorSpan(ctx.getResources().getColor(R.color.snackbar_text)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    public static void applyLocale(Context baseContext) {
        Locale locale;
        if (AppHelper.Pref.getForceBelarus(baseContext)) {
            String languageToLoad = "be";
            locale = new Locale(languageToLoad);
        } else {
            locale = Locale.getDefault();
        }

        Configuration config = new Configuration();
        config.locale = locale;
        baseContext.getResources().updateConfiguration(config,
                baseContext.getResources().getDisplayMetrics());
    }

    public static Bitmap bitmapFromResource(Context ctx, int resId) {
        return BitmapFactory.decodeResource(ctx.getResources(), resId);
    }

    public static int applyAlphaToColor(int color, float alpha) {
        int alphaHex =  (int)(0xff * alpha) * 0x1000000;
        alphaHex |= 0xffffff;
        return alphaHex & color;
    }

    public static CharSequence styleStringWithSearch(String text, String search) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        int lastFound = 0;
        while((lastFound = text.toLowerCase().indexOf(search.toLowerCase(), lastFound)) != -1) {
            ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), lastFound, lastFound + search.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            lastFound += search.length();
        }

        return ssb;
    }

    public static class Pref {
        public static String getUpdateFrequency(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx).getString(ctx.getString(R.string.pref_check_update_rate_key), ctx.getString(R.string.pref_check_update_rate_default));
        }

        public static boolean getForceBelarus(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(ctx.getString(R.string.pref_bel_key), Boolean.FALSE);
        }

        public static boolean getNotifyUpdated(Context ctx) {
            return PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean(ctx.getString(R.string.pref_show_update_notification_key), Boolean.FALSE);
        }
    }
}
