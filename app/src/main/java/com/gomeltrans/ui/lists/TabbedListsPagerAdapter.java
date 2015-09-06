package com.gomeltrans.ui.lists;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class TabbedListsPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private Context ctx;

    public TabbedListsPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);

        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        return ItemsListFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ItemsListFragment.getTabTitle(ctx, position);
    }

}
