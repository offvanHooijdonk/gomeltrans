package com.gomeltrans.helper;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import com.gomeltrans.R;
import com.gomeltrans.model.StopTable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Yahor_Fralou on 9/8/2015.
 */
public class AppHelper {
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
}
