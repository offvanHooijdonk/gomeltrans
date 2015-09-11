package com.gomeltrans.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gomeltrans.R;

/**
 * Created by Yahor_Fralou on 9/11/2015.
 */
public class TransportBadgeView extends View {

    public static final int DIMENSION_UNSET = -1;

    private String numberName;
    private int transportType;
    private int iconSize;
    private float textSize;

    public TransportBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TransportBadgeView, 0, 0);

        try {
            numberName = a.getString(R.styleable.TransportBadgeView_numberName);
            transportType = a.getInt(R.styleable.TransportBadgeView_type, 0);
            iconSize = a.getDimensionPixelOffset(R.styleable.TransportBadgeView_iconSize, DIMENSION_UNSET);
            textSize = a.getDimension(R.styleable.TransportBadgeView_textSize, DIMENSION_UNSET);
        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_transport_badge, null);

        ImageView imageBadgeBus = (ImageView) findViewById(R.id.imageBadge_Bus);
        ImageView imageBadgeTrolley = (ImageView) findViewById(R.id.imageBadge_Trolley);
        if (transportType == 1) { // trolley
            imageBadgeBus.setVisibility(GONE);
            imageBadgeTrolley.setVisibility(VISIBLE);
        } else { // bus
            imageBadgeBus.setVisibility(VISIBLE);
            imageBadgeTrolley.setVisibility(GONE);
        }

        if (iconSize != DIMENSION_UNSET) {
            ViewGroup container = (ViewGroup) findViewById(R.id.blockBadgeContainer);
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.width = iconSize;
            params.height = iconSize;
            container.setLayoutParams(params);
        }

        TextView textNumberName = (TextView) findViewById(R.id.textNumberName);
        textNumberName.setText(numberName);

        if (textSize != DIMENSION_UNSET) {
            textNumberName.setTextSize(textSize);
        }
    }

    /*@Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.draw(canvas);
    }*/
}
