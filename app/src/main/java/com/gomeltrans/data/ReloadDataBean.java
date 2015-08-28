package com.gomeltrans.data;

import android.content.Context;
import android.os.AsyncTask;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopTableDao;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.data.dao.TransportStopDao;
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
    private OnReloadFinishedListener listener;

    public  ReloadDataBean(Context context, OnReloadFinishedListener l) {
        this.ctx = context;
        this.listener = l;

        loadedTransports = false;
        loadedStops = false;
    }

    public void reloadData() {
        LoadDataTask<Transport> transportLoadDataTask = new LoadDataTask<>(ctx, R.raw.transport, GsonHelper.getTransportGson(), new TransportUpdater(), Transport[].class);
        transportLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        LoadDataTask<Stop> stopLoadDataTask = new LoadDataTask<>(ctx, R.raw.stops, GsonHelper.getStopsGson(), new StopsUpdater(), Stop[].class);
        stopLoadDataTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void storeData() {
        DBStoreTask storeTask = new DBStoreTask();
        storeTask.execute();
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

    private void onDBUpdateFinished() {
        if (listener != null) {
            listener.onReloadDataFinished();
        }
    }

    public interface OnReloadFinishedListener {
        public void onReloadDataFinished();
    }

    private class DBStoreTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            TransportDao transportDao = new TransportDao(ctx);
            StopsDao stopsDao = new StopsDao(ctx);
            TransportStopDao trStDao = new TransportStopDao(ctx);
            StopTableDao stopTableDao = new StopTableDao(ctx);

            transportDao.storeList(transports);
            transportDao.clearAllBut(transports);
            stopsDao.storeList(stops);
            stopsDao.clearAllBut(stops);

            trStDao.clearAll();
            for (Transport t : transports) {
                trStDao.saveTransportStops(t);
            }

            stopTableDao.clearAll();
            for (Stop s : stops) {
                stopTableDao.saveStopTable(s);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            onDBUpdateFinished();
        }
    }
}
