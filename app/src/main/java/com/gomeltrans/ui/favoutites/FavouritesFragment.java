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

        FavouritesFragmentPagerAdapter adapter = new FavouritesFragmentPagerAdapter(getChildFragmentManager(), parent);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.post(new Runnable() { // this is a hack due to a bug in 22.2.1 design lib
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return v;
    }
}
