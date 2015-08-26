package com.gomeltrans.ui.favoutites.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.BusInfoActivity;

import java.util.List;

/**
 * Created by yahor on 25.08.15.
 */
public class FavouriteBusAdapter extends RecyclerView.Adapter<FavouriteBusAdapter.ViewHolder> {
    private Context ctx;
    private List<Transport> buses;

    public FavouriteBusAdapter(Context context, List<Transport> buses) {
        this.ctx = context;
        this.buses = buses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_bus, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Transport transport = buses.get(position);

        vh.numberName.setText(transport.getNumberName());
        vh.routeName.setText(transport.getRouteName());

        vh.blockItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, BusInfoActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView numberName;
        public TextView routeName;
        public ViewGroup blockItem;

        public ViewHolder(View v) {
            super(v);

            numberName = (TextView) v.findViewById(R.id.numberName);
            routeName = (TextView) v.findViewById(R.id.routeName);
            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
        }
    }
}