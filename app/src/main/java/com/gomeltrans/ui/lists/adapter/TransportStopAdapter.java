package com.gomeltrans.ui.lists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.model.TransportStops;
import com.gomeltrans.helper.IntentsHelper;

import java.util.List;

/**
 * Created by Yahor_Fralou on 9/4/2015.
 */
public class TransportStopAdapter extends RecyclerView.Adapter<TransportStopAdapter.ViewHolder> {
    private Context ctx;
    private List<TransportStops> transportStopsList;

    public TransportStopAdapter(Context context, List<TransportStops> list) {
        this.ctx = context;
        this.transportStopsList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_transport_stop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        final TransportStops ts = transportStopsList.get(position);

        vh.textName.setText(ts.getStop().getName());
        vh.textComment.setText(ts.getStop().getComment());
        if (!TextUtils.isEmpty(ts.getNextTime())) {
            vh.textNextTime.setText(ts.getNextTime());
            vh.textNextTime.setVisibility(View.VISIBLE);
            vh.textNextDay.setVisibility(View.GONE);
        } else if (!TextUtils.isEmpty(ts.getFirstTime())) {
            vh.textNextDay.setText(ts.getFirstTime());
            vh.textNextTime.setVisibility(View.GONE);
            vh.textNextDay.setVisibility(View.VISIBLE);
        } else {
            vh.textNextTime.setText(R.string.time_none);
            vh.textNextTime.setVisibility(View.VISIBLE);
            vh.textNextDay.setVisibility(View.GONE);
        }

        if (ts.getStop().isFavourite()) {
            vh.blockBackground.setBackgroundColor(0x3fffffff & ctx.getResources().getColor(R.color.fav_item_bckgr));
        } else {
            vh.blockBackground.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
        }

        vh.blockItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentsHelper.startStopInfo(ctx, ts.getStop().getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return transportStopsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup blockItem;
        ViewGroup blockBackground;
        TextView textName;
        TextView textComment;
        TextView textNextTime;
        TextView textNextDay;

        public ViewHolder(View v) {
            super(v);

            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
            blockBackground = (ViewGroup) v.findViewById(R.id.blockBackground);
            textName = (TextView) v.findViewById(R.id.textName);
            textComment = (TextView) v.findViewById(R.id.textComment);
            textNextTime = (TextView) v.findViewById(R.id.textNextTime);
            textNextDay = (TextView) v.findViewById(R.id.textNextDay);
        }
    }
}
