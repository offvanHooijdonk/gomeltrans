package com.gomeltrans.startup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gomeltrans.data.ScheduleHelper;

/**
 * Created by Yahor_Fralou on 9/8/2015.
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context ctx, Intent intent) {
        ScheduleHelper.scheduleUpdate(ctx);
    }
}
