package com.gomeltrans.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.gomeltrans.model.StopTable;

import java.util.List;

/**
 * Created by yahor on 12.09.15.
 */
public class StopTableAdapter extends RecyclerView.Adapter<StopTableAdapter.ViewHolder> {

    private Context ctx;
    private List<StopTable> stopTableList;

    public StopTableAdapter(Context context, List<StopTable> stopTableList) {
        this.ctx = context;
        this.stopTableList = stopTableList;
    }

    @Override
    public StopTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(StopTableAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
