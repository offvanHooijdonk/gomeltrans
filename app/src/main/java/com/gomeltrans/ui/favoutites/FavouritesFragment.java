package com.gomeltrans.ui.favoutites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomeltrans.R;
import com.gomeltrans.ui.MainActivity;

/**
 * Created by Yahor_Fralou on 8/24/2015.
 */
public class FavouritesFragment extends Fragment {
    private MainActivity parent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parent = ((MainActivity) getActivity());
        parent.getSupportActionBar().setTitle(getActivity().getString(R.string.item_favourite));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_favourite, container, false);

        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new FavouritesFragmentPagerAdapter(getChildFragmentManager(), parent));

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return v;
    }
}
