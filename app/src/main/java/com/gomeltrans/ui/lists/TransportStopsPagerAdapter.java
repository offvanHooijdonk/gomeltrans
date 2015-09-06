package com.gomeltrans.ui.lists;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gomeltrans.R;

/**
 * Created by yahor on 06.09.15.
 */
public class TransportStopsPagerAdapter extends FragmentPagerAdapter {
    private static final int TABS_COUNT = 2;

    private Context ctx;
    private long transportId;

    public TransportStopsPagerAdapter(FragmentManager fm, Context context, long transportId) {
        super(fm);

        this.ctx = context;
        this.transportId = transportId;
    }

    @Override
    public Fragment getItem(int position) {
        return TransportStopsListFragment.getInstance(position, transportId);
    }

    @Override
    public int getCount() {
        return TABS_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TransportStopsListFragment.getTitle(ctx, position);
    }
}
