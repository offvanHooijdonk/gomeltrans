package com.gomeltrans.ui.lists;

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
public class TabbedListsFragment extends Fragment {
    private static final String ARG_MODE = "arg_mode";

    private MainActivity parent;

    public static TabbedListsFragment getInstance() {
        Bundle args = new Bundle();

        TabbedListsFragment fragment = new TabbedListsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parent = ((MainActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;

        v = inflater.inflate(R.layout.frag_tabs, container, false);

        TabbedListsPagerAdapter adapter = new TabbedListsPagerAdapter(getChildFragmentManager(), parent);

        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        final TabLayout tabLayout = (TabLayout) parent.findViewById(R.id.sliding_tabs);
        if (tabLayout != null) {
            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.post(new Runnable() { // this is a hack due to a bug in 22.2.1 design lib
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });
        }

        return v;
    }
}
