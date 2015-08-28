package com.gomeltrans.data.web;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class LoadDataTask<T> extends AsyncTask<Void, Void, List<T>> {
    private int resId;
    private Gson gson;
    private OnResponseListener<T> listener;
    private Context ctx;
    private Class clazz;

    public LoadDataTask(Context context, int resId, Gson gson, OnResponseListener<T> responseListener, Class clazzArray) {
        this.ctx = context;
        this.resId = resId;
        this.gson = gson;
        this.listener = responseListener;
    }

    @Override
    protected List<T> doInBackground(Void... params) {
        Object obj = gson.fromJson(new InputStreamReader(ctx.getResources().openRawResource(resId)), clazz/*new TypeToken<ArrayList<T>>(){}.getType()*/ /*List.class*/);
        T[] array = (T[]) obj;
        return Arrays.asList(array);
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
