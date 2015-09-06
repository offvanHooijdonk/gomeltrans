package com.gomeltrans.ui.actionbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gomeltrans.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yahor on 06.09.15.
 */
public class FavouriteFilterActionProvider extends ActionProvider {
    public enum SHOW_MODE {SHOW_ALL, FAV_FIRST, FAV_ONLY};

    private Context ctx;
    private SHOW_MODE mode = SHOW_MODE.SHOW_ALL;
    private List<OnFavFilterChangeListener> listeners = new ArrayList<>();

    private ImageView imageAll;
    private ImageView imageFavFirst;
    private ImageView imageFavOnly;
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public FavouriteFilterActionProvider(Context context) {
        super(context);

        this.ctx = context;
    }

    public void setShowMode(SHOW_MODE showMode) {
        this.mode = showMode;
    }

    public void addListener(OnFavFilterChangeListener l) {
        if (l != null) {
            listeners.add(l);
        }
    }

    @Override
    public View onCreateActionView() {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(ctx).inflate(R.layout.action_favourite_filter_toggle, null, false);

        imageAll = (ImageView) v.findViewById(R.id.imageShowAll);
        imageFavFirst = (ImageView) v.findViewById(R.id.imageShowFavFirst);
        imageFavOnly = (ImageView) v.findViewById(R.id.imageShowFavOnly);

        setImagesVisibility();

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollMode();
                setImagesVisibility();
                for (OnFavFilterChangeListener l : listeners) {
                    if (l != null) {
                        l.onFavFilterChanged(mode);
                    }
                }
            }
        });

        return v;
    }

    private void setImagesVisibility() {
        if (imageAll != null && imageFavFirst != null && imageFavOnly != null) {
            imageAll.setVisibility(mode == SHOW_MODE.SHOW_ALL ? View.VISIBLE : View.GONE);
            imageFavFirst.setVisibility(mode == SHOW_MODE.FAV_FIRST ? View.VISIBLE : View.GONE);
            imageFavOnly.setVisibility(mode == SHOW_MODE.FAV_ONLY ? View.VISIBLE : View.GONE);
        }
    }

    private void rollMode() {
        int pos = mode.ordinal();
        pos++;
        pos = (pos == SHOW_MODE.values().length) ? 0 : pos;
        mode = SHOW_MODE.values()[pos];
    }

    public interface OnFavFilterChangeListener {
        void onFavFilterChanged(SHOW_MODE newValue);
    }
}
