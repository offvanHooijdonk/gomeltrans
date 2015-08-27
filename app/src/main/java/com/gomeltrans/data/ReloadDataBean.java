package com.gomeltrans.data;

import android.content.Context;
import android.os.AsyncTask;

import com.gomeltrans.R;
import com.gomeltrans.data.web.LoadDataTask;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.gomeltrans.model.gson.GsonHelper;

import java.util.List;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class ReloadDataBean {
    private Context ctx;
    private List<Transport> transports;
    private List<Stop> stops;
    private boolean loadedTransports;
    private boolean loadedStops;

    public  ReloadDataBean(Context context) {
        this.ctx = context;

        loadedTransports = false;
        loadedStops = false;
    }

    public void reloadData() {
        LoadDataTask<Transport> transportLoadDataTask = new LoadDataTask<>(ctx, String.valueOf(R.raw.transport), GsonHelper.getTransportGson(), new TransportUpdater());
        transportLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        LoadDataTask<Stop> stopLoadDataTask = new LoadDataTask<>(ctx, String.valueOf(R.raw.stops), GsonHelper.getStopsGson(), new StopsUpdater());
        stopLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void storeData() {

    }

    private void onDataLoaded() {
        if (loadedStops && loadedTransports) {
            storeData();
        }
    }

    public class TransportUpdater implements LoadDataTask.OnResponseListener<Transport> {
        @Override
        public void onSuccess(List<Transport> result) {
            transports = result;
            loadedTransports = true;

            onDataLoaded();
        }
    }

    public class StopsUpdater implements LoadDataTask.OnResponseListener<Stop> {
        @Override
        public void onSuccess(List<Stop> result) {
            stops = result;
            loadedStops = true;

            onDataLoaded();
        }
    }

}
