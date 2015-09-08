package com.gomeltrans.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gomeltrans.helper.AppHelper;

import java.util.Calendar;

/**
 * Created by Yahor_Fralou on 9/8/2015.
 */
public class ScheduleHelper {

    public static void scheduleUpdate(Context ctx) {
        cancelSchedule(ctx);

        String updateOption = AppHelper.Pref.getUpdateFrequency(ctx);
        int everyThatHour = Integer.valueOf(updateOption);
        if (everyThatHour != -1) {
            getAlarmManager(ctx).set(AlarmManager.RTC_WAKEUP, calculateNextTime(everyThatHour), createPendingIntent(ctx));
        }
    }

    public static void cancelSchedule(Context ctx) {
        getAlarmManager(ctx).cancel(createPendingIntent(ctx));
    }

    private static long calculateNextTime(int everyThatHour) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int hourToSchedule = 0;
        while (hourToSchedule <= hour) {
            hourToSchedule += everyThatHour;
        }

        if (hourToSchedule >= 24) {
            hourToSchedule -= 24;
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hourToSchedule);
        calendar.set(Calendar.MINUTE, 0);

        return calendar.getTimeInMillis();
    }

    private static PendingIntent createPendingIntent(Context ctx) {
        Intent intent = new Intent(ctx, UpdateOnScheduleService.class);

        return PendingIntent.getService(ctx, 0, intent, 0);
    }

    private static AlarmManager getAlarmManager(Context ctx) {
        return (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
    }

}
