package com.gomeltrans.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.ui.view.TransportBadgeView;

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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_stop_table, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StopTableAdapter.ViewHolder vh, int position) {
        StopTable st = stopTableList.get(position);
        vh.badgeView.setTransportType(st.getTransport().getTypeNumber());
        vh.badgeView.setNumberName(st.getTransport().getNumberName());

        vh.textRouteName.setText(st.getTransport().getRouteName());
        vh.textNextTime.setText(st.getTimeUpcoming());
    }

    @Override
    public int getItemCount() {
        return stopTableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TransportBadgeView badgeView;
        TextView textRouteName;
        TextView textNextTime;

        public ViewHolder(View v) {
            super(v);

            badgeView = (TransportBadgeView) v.findViewById(R.id.transportBadge);
            textRouteName = (TextView) v.findViewById(R.id.routeName);
            textNextTime = (TextView) v.findViewById(R.id.textNextTime);
        }
    }
}
