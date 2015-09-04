package com.gomeltrans.util;

import android.content.Context;
import android.content.Intent;

import com.gomeltrans.ui.StopInfoActivity;
import com.gomeltrans.ui.TransportInfoActivity;

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
}
