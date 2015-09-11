package com.gomeltrans.ui.lists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Transport;
import com.gomeltrans.helper.IntentsHelper;
import com.gomeltrans.ui.view.TransportBadgeView;

import java.util.List;

/**
 * Created by yahor on 25.08.15.
 */
public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {
    private Context ctx;
    private List<Transport> buses;

    public TransportAdapter(Context context, List<Transport> buses) {
        this.ctx = context;
        this.buses = buses;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_transport_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        final Transport transport = buses.get(position);

        //vh.numberName.setText(transport.getNumberName());
        vh.badgeView.setNumberName(transport.getNumberName());
        vh.badgeView.setTransportType(transport.getTypeNumber());

        vh.routeName.setText(transport.getRouteName());

        vh.imageFavFalse.setVisibility(transport.isFavourite() ? View.GONE : View.VISIBLE);
        vh.imageFavTrue.setVisibility(transport.isFavourite() ? View.VISIBLE : View.GONE);

        vh.imageFavFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemFavourite(transport, true, vh);
            }
        });
        vh.imageFavTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemFavourite(transport, false, vh);
            }
        });

        vh.blockFav.setVisibility(View.VISIBLE);

        vh.blockItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentsHelper.startTransportInfo(ctx, transport.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    private void setItemFavourite(Transport transport, boolean favourite, ViewHolder vh) {
        TransportDao dao = new TransportDao(ctx);
        if (favourite) {
            dao.setFavourite(transport.getId(), true);
            transport.setFavourite(true);
            vh.imageFavFalse.setVisibility(View.GONE);
            vh.imageFavTrue.setVisibility(View.VISIBLE);
        } else {
            dao.setFavourite(transport.getId(), false);
            transport.setFavourite(false);
            vh.imageFavFalse.setVisibility(View.VISIBLE);
            vh.imageFavTrue.setVisibility(View.GONE);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public TextView numberName;
        public TransportBadgeView badgeView;
        public TextView routeName;
        public ViewGroup blockItem;
        public ViewGroup blockFav;
        public ImageView imageFavFalse;
        public ImageView imageFavTrue;

        public ViewHolder(View v) {
            super(v);

            //numberName = (TextView) v.findViewById(R.id.numberName);
            badgeView = (TransportBadgeView) v.findViewById(R.id.transportBadge);
            routeName = (TextView) v.findViewById(R.id.routeName);
            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
            blockFav = (ViewGroup) v.findViewById(R.id.blockFav);
            imageFavFalse = (ImageView) v.findViewById(R.id.imageFavFalse);
            imageFavTrue = (ImageView) v.findViewById(R.id.imageFavTrue);
        }
    }
}