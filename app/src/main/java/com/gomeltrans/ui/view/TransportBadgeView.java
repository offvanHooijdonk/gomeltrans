package com.gomeltrans.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gomeltrans.R;

/**
 * Created by Yahor_Fralou on 9/11/2015.
 */
public class TransportBadgeView extends FrameLayout {

    public static final int DIMENSION_UNSET = -1;

    private String numberName;
    private int transportType;
    private int iconSize;
    private float textSizeSP;

    private ImageView imageBadgeBus;
    private ImageView imageBadgeTrolley;
    private TextView textNumberName;
    private ViewGroup container;

    public TransportBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TransportBadgeView, 0, 0);

        try {
            numberName = a.getString(R.styleable.TransportBadgeView_numberName);
            transportType = a.getInt(R.styleable.TransportBadgeView_type, 0);
            iconSize = a.getDimensionPixelOffset(R.styleable.TransportBadgeView_iconSize, DIMENSION_UNSET);
            textSizeSP = a.getDimension(R.styleable.TransportBadgeView_textSize, DIMENSION_UNSET);
        } finally {
            a.recycle();
        }

        init();
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;

        if (numberName != null) {
            textNumberName.setText(numberName);
            invalidate();
            requestLayout();
        }
    }

    public void setTransportType(int transportType) {
        this.transportType = transportType;

        if (transportType == 1) { // trolley
            imageBadgeBus.setVisibility(GONE);
            imageBadgeTrolley.setVisibility(VISIBLE);
        } else { // bus
            imageBadgeBus.setVisibility(VISIBLE);
            imageBadgeTrolley.setVisibility(GONE);
        }

        invalidate();
        requestLayout();
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;

        if (iconSize != DIMENSION_UNSET) {
            ViewGroup.LayoutParams params = container.getLayoutParams();
            params.width = iconSize;
            params.height = iconSize;
            container.setLayoutParams(params);

            invalidate();
            requestLayout();
        }
    }

    public void setTextSizeSP(float textSizeSP) {
        this.textSizeSP = textSizeSP;

        if (textSizeSP != DIMENSION_UNSET) {
            textNumberName.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizeSP);

            invalidate();
            requestLayout();
        }
    }

    private void init() {
        inflate(getContext(), R.layout.view_transport_badge, this);

        container = (ViewGroup) findViewById(R.id.blockBadgeContainer);
        setIconSize(iconSize);

        imageBadgeBus = (ImageView) findViewById(R.id.imageBadge_Bus);
        imageBadgeTrolley = (ImageView) findViewById(R.id.imageBadge_Trolley);

        setTransportType(transportType);

        textNumberName = (TextView) findViewById(R.id.textNumberName);
        setNumberName(numberName);

        setTextSizeSP(textSizeSP);
    }

}
