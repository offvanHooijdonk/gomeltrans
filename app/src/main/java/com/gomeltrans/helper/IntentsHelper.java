package com.gomeltrans.helper;

import android.content.Context;
import android.content.Intent;

import com.gomeltrans.model.StopTable;
import com.gomeltrans.ui.StopInfoActivity;
import com.gomeltrans.ui.TransportInfoActivity;
import com.gomeltrans.ui.preferences.PreferenceActivity;
import com.gomeltrans.ui.StopTransportScheduleActivity;

import java.util.Date;

/**
 * Created by Yahor_Fralou on 9/4/2015.
 */
public class IntentsHelper {
    public static void startStopInfo(Context ctx, Long id) {
        Intent intent = new Intent(ctx, StopInfoActivity.class);
        intent.putExtra(StopInfoActivity.EXTRA_STOP_ID, id);
        ctx.startActivity(intent);
    }

    public static void startTransportInfo(Context ctx, Long id) {
        Intent intent = new Intent(ctx, TransportInfoActivity.class);
        intent.putExtra(TransportInfoActivity.EXTRA_TRANSPORT_ID, id);
        ctx.startActivity(intent);
    }

    public static void startPreference(Context ctx) {
        Intent intent = new Intent(ctx, PreferenceActivity.class);
        ctx.startActivity(intent);
    }

    public static void startStopTransportSchedule(Context ctx, Long stopId, Long transportId, Date datePicked, StopTable.DAY_TYPE dayType) {
        Intent intent = new Intent(ctx, StopTransportScheduleActivity.class);
        intent.putExtra(StopTransportScheduleActivity.EXTRA_TRANSPORT_ID, transportId);
        intent.putExtra(StopTransportScheduleActivity.EXTRA_STOP_ID, stopId);
        if (datePicked != null) {
            intent.putExtra(StopTransportScheduleActivity.EXTRA_DATE_PICKED, datePicked.getTime());
        }
        if (dayType != null) {
            intent.putExtra(StopTransportScheduleActivity.EXTRA_DAY_TYPE_PICKED, dayType.getCode());
        }
        ctx.startActivity(intent);
    }
}
