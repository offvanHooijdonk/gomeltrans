package com.gomeltrans.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.gomeltrans.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Yahor_Fralou on 9/9/2015.
 */
public class NotificationsUtil {
    private static final int UPDATED_ID = 0;

    private Context ctx;
    private NotificationManager manager;

    public NotificationsUtil(Context context) {
        this.ctx = context;

        manager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void notifyUpdateIfEnabled() {
        if (AppHelper.Pref.getNotifyUpdated(ctx)) {
            showUpdatedSuccess();
        }
    }

    public void showUpdatedSuccess() {
        // TODO date the data actual for will be a parameter
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx).setContentTitle(ctx.getString(R.string.notif_updated_title))
                .setContentText(ctx.getString(R.string.notif_updated_text, SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date())))
                .setTicker(ctx.getString(R.string.notif_updated_title))
                .setSmallIcon(R.drawable.ic_refresh_white_24dp)
                .setLargeIcon(AppHelper.bitmapFromResource(ctx, R.mipmap.ic_launcher))
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            builder.setPriority(Notification.PRIORITY_LOW);
        }

        manager.notify(UPDATED_ID, builder.build());
    }
}
