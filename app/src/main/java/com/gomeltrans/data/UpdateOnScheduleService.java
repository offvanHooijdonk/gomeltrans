package com.gomeltrans.data;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by Yahor_Fralou on 9/8/2015.
 */
public class UpdateOnScheduleService extends IntentService implements ReloadDataBean.OnReloadFinishedListener {

    private static final String SERVICE_NAME = "UpdateOnScheduleService";

    public UpdateOnScheduleService() {
        super(SERVICE_NAME);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpdateOnScheduleService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ReloadDataBean reloadDataBean = new ReloadDataBean(this, this);
        reloadDataBean.reloadData();
    }

    @Override
    public void onReloadDataFinished() {
        ScheduleHelper.scheduleUpdate(this);
        this.stopSelf();
    }
}
