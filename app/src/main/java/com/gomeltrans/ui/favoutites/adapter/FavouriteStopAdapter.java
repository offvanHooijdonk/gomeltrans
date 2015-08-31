package com.gomeltrans.ui.favoutites.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.model.Stop;
import com.gomeltrans.ui.StopInfoActivity;

import java.util.List;

/**
 * Created by Yahor_Fralou on 8/31/2015.
 */
public class FavouriteStopAdapter extends RecyclerView.Adapter<FavouriteStopAdapter.ViewHolder> {
    private Context ctx;
    private List<Stop> stops;

    public FavouriteStopAdapter(Context context, List<Stop> stops) {
        this.ctx = context;
        this.stops = stops;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_stop, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        Stop stop = stops.get(position);

        vh.name.setText(stop.getName());
        vh.comment.setText(stop.getComment());

        vh.blockItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, StopInfoActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView name;
        public TextView comment;
        public ViewGroup blockItem;

        public ViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.stopName);
            comment = (TextView) v.findViewById(R.id.stopComment);
            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
        }
    }
}
