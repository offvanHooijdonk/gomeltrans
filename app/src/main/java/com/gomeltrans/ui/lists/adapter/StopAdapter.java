package com.gomeltrans.ui.lists.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.helper.AppHelper;
import com.gomeltrans.helper.IntentsHelper;
import com.gomeltrans.model.Stop;

import java.util.List;

/**
 * Created by Yahor_Fralou on 8/31/2015.
 */
public class StopAdapter extends RecyclerView.Adapter<StopAdapter.ViewHolder> {
    private Context ctx;
    private List<Stop> stops;
    private String searchText;

    public StopAdapter(Context context, List<Stop> stops) {
        this.ctx = context;
        this.stops = stops;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_stop_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder vh, int position) {
        final Stop stop = stops.get(position);

        if (!TextUtils.isEmpty(searchText)) {
            // TODO implement for all text entries?
            String text = stop.getName();
            /*SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            int start = text.toLowerCase().indexOf(searchText.toLowerCase());
            int end = start + searchText.length();
            ssb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);*/
            vh.name.setText(AppHelper.styleStringWithSearch(text, searchText));
        } else {
            vh.name.setText(stop.getName());
        }
        vh.comment.setText(stop.getComment());

        vh.imageFavFalse.setVisibility(stop.isFavourite() ? View.GONE : View.VISIBLE);
        vh.imageFavTrue.setVisibility(stop.isFavourite() ? View.VISIBLE : View.GONE);

        vh.imageFavFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemFavourite(stop, true, vh);
            }
        });
        vh.imageFavTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setItemFavourite(stop, false, vh);
            }
        });

        vh.blockFav.setVisibility(View.VISIBLE);

        vh.blockItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentsHelper.startStopInfo(ctx, stop.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return stops.size();
    }

    private void setItemFavourite(Stop stop, boolean favourite, ViewHolder vh) {
        StopsDao dao = new StopsDao(ctx);
        if (favourite) {
            dao.setFavourite(stop.getId(), true);
            vh.imageFavFalse.setVisibility(View.GONE);
            vh.imageFavTrue.setVisibility(View.VISIBLE);
        } else {
            dao.setFavourite(stop.getId(), false);
            vh.imageFavFalse.setVisibility(View.VISIBLE);
            vh.imageFavTrue.setVisibility(View.GONE);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView comment;
        public ViewGroup blockItem;
        public ViewGroup blockFav;
        public ImageView imageFavFalse;
        public ImageView imageFavTrue;

        public ViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.stopName);
            comment = (TextView) v.findViewById(R.id.stopComment);
            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
            blockFav = (ViewGroup) v.findViewById(R.id.blockFav);
            imageFavFalse = (ImageView) v.findViewById(R.id.imageFavFalse);
            imageFavTrue = (ImageView) v.findViewById(R.id.imageFavTrue);
        }
    }
}
