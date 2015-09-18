package com.gomeltrans.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.helper.AppHelper;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.ui.view.TransportBadgeView;

import java.util.List;

/**
 * Created by yahor on 12.09.15.
 */
public class StopTableAdapter extends RecyclerView.Adapter<StopTableAdapter.ViewHolder> {

    private Context ctx;
    private List<StopTable> stopTableList;
    private OnItemSelected listener;

    public StopTableAdapter(Context context, List<StopTable> stopTableList) {
        this.ctx = context;
        this.stopTableList = stopTableList;
    }

    public void setListener(OnItemSelected listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_stop_table, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StopTableAdapter.ViewHolder vh, int position) {
        final StopTable st = stopTableList.get(position);
        vh.badgeView.setTransportType(st.getTransport().getTypeNumber());
        vh.badgeView.setNumberName(st.getTransport().getNumberName());

        vh.textRouteName.setText(st.getTransport().getRouteName());
        vh.textNextTime.setText(st.getTimeUpcoming());

        if (st.getTransport().isFavourite()) {
            vh.blockBackground.setBackgroundColor(AppHelper.applyAlphaToColor(ctx.getResources().getColor(R.color.fav_item_bckgr), AppHelper.FAV_BACKGR_ALPHA));
        } else {
            vh.blockBackground.setBackgroundColor(ctx.getResources().getColor(android.R.color.transparent));
        }

        vh.blockItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onStopScheduleSelected(st.getTransport().getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stopTableList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup blockItem;
        ViewGroup blockBackground;
        TransportBadgeView badgeView;
        TextView textRouteName;
        TextView textNextTime;

        public ViewHolder(View v) {
            super(v);

            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
            blockBackground = (ViewGroup) v.findViewById(R.id.blockBackground);
            badgeView = (TransportBadgeView) v.findViewById(R.id.transportBadge);
            textRouteName = (TextView) v.findViewById(R.id.routeName);
            textNextTime = (TextView) v.findViewById(R.id.textNextTime);
        }
    }

    public interface OnItemSelected {
        void onStopScheduleSelected(Long transportId);
    }
}
