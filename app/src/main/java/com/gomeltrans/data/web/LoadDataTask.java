package com.gomeltrans.data.web;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class LoadDataTask<T> extends AsyncTask<Void, Void, List<T>> {
    private String url;
    private Gson gson;
    private OnResponseListener<T> listener;
    private Context ctx;

    public LoadDataTask(Context context, String url, Gson gson, OnResponseListener<T> responseListener) {
        this.ctx = context;
        this.url = url;
        this.gson = gson;
        this.listener = responseListener;
    }

    @Override
    protected List<T> doInBackground(Void... params) {
        List<T> list = gson.fromJson(new InputStreamReader(ctx.getResources().openRawResource(Integer.valueOf(url))), new TypeToken<List<T>>(){}.getType());
        return list;
    }

    @Override
    protected void onPostExecute(List<T> ts) {
        if (listener != null) {
            listener.onSuccess(ts);
        }
    }

    public interface OnResponseListener<T> {
        void onSuccess(List<T> result);
    }
}
