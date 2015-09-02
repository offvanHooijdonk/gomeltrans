package com.gomeltrans.ui.lists;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class TabbedFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT_FAVOURITES_ALL = 3;
    final int PAGE_COUNT_TRANSPORT_ONLY = 2;

    public enum MODE {FAVOURITES_ALL, TRANSPORT_ONLY};

    private Context ctx;
    private MODE mode;

    public TabbedFragmentPagerAdapter(FragmentManager fm, Context ctx, MODE mode) {
        super(fm);

        this.ctx = ctx;
        this. mode = mode;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemsListFragment.getInstance(position, mode == MODE.FAVOURITES_ALL);
    }

    @Override
    public int getCount() {
        int count;
        switch (mode) {
            case FAVOURITES_ALL : count = PAGE_COUNT_FAVOURITES_ALL; break;
            case TRANSPORT_ONLY : count = PAGE_COUNT_TRANSPORT_ONLY; break;
            default: count = PAGE_COUNT_FAVOURITES_ALL;
        }
        return count;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ItemsListFragment.getTabTitle(ctx, position);
    }

}
