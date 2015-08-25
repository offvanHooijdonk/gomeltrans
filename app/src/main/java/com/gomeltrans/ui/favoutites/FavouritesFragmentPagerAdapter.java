package com.gomeltrans.ui.favoutites;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class FavouritesFragmentPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;

    private Context ctx;

    public FavouritesFragmentPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);

        this.ctx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        return FavouritesListFragment.getInstance(position);
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return FavouritesListFragment.getTabTitle(ctx, position);
    }

}
