package com.gomeltrans.ui.actionbar;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gomeltrans.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 9/4/2015.
 */
public class FavouriteActionProvider extends ActionProvider {
    private List<ToggleListener> listeners = new ArrayList<>();
    private Context ctx;
    private boolean isFavourite;
    private ImageView imageFavTrue;
    private ImageView imageFavFalse;

    // TODO make reverse listener for activity allows state change
    public FavouriteActionProvider(Context context) {
        super(context);

        this.ctx = context;
    }

    public void setFavourite(boolean favourite) {
        this.isFavourite = favourite;
        setImagesVisibility();
    }

    public void addToggleListener(ToggleListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public View onCreateActionView() {
        View v = LayoutInflater.from(ctx).inflate(R.layout.action_favourite_toggle, null, false);

        imageFavTrue = (ImageView) v.findViewById(R.id.imageFavTrue);
        imageFavFalse = (ImageView) v.findViewById(R.id.imageFavFalse);

        setImagesVisibility();

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFavourite = !isFavourite;
                for (ToggleListener l : listeners) {
                    if (l != null) {
                        l.onFavTogglerStateChanged(isFavourite);
                    }
                }
                setImagesVisibility();
            }
        });

        return v;
    }

    private void setImagesVisibility() {
        if (imageFavFalse != null && imageFavTrue != null) {
            imageFavTrue.setVisibility(isFavourite ? View.VISIBLE : View.GONE);
            imageFavFalse.setVisibility(isFavourite ? View.GONE : View.VISIBLE);
        }
    }

    public interface ToggleListener {
        /**
         * @param newValue New value that should be applied: favourite id <code>true</code>, non-favourite id <code>false</code>
         */
        void onFavTogglerStateChanged(boolean newValue);
    }
}
