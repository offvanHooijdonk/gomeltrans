package com.gomeltrans.ui.lists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomeltrans.R;
import com.gomeltrans.ui.MainActivity;

/**
 * Created by Yahor_Fralou on 8/24/2015.
 */
public class TabbedListFragment extends Fragment {
    private static final String ARG_MODE = "arg_mode";

    private MainActivity parent;
    private MODE mode;

    public enum MODE {FAVOURITES_ALL, TRANSPORT_ONLY, STOPS_ONLY};

    public static TabbedListFragment getInstance(MODE mode) {
        Bundle args = new Bundle();
        args.putString(ARG_MODE, mode.toString());

        TabbedListFragment fragment = new TabbedListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mode = MODE.valueOf(getArguments().getString(ARG_MODE));

        parent = ((MainActivity) getActivity());
        // FIXME this fixes issue when on app start header is not set. Need better fix
        if (mode == MODE.FAVOURITES_ALL) {
            parent.getSupportActionBar().setTitle(getActivity().getString(R.string.item_favourite));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;

        if (mode == MODE.FAVOURITES_ALL || mode == MODE.TRANSPORT_ONLY) {
            v = inflater.inflate(R.layout.frag_tabs, container, false);

            TabbedFragmentPagerAdapter adapter = new TabbedFragmentPagerAdapter(getChildFragmentManager(), parent,
                    mode == MODE.TRANSPORT_ONLY ? TabbedFragmentPagerAdapter.MODE.TRANSPORT_ONLY : TabbedFragmentPagerAdapter.MODE.FAVOURITES_ALL);

            final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
            viewPager.setAdapter(adapter);

            final TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
            tabLayout.post(new Runnable() { // this is a hack due to a bug in 22.2.1 design lib
                @Override
                public void run() {
                    tabLayout.setupWithViewPager(viewPager);
                }
            });
        } else { // MODE.STOPS_ONLY
            v = inflater.inflate(R.layout.frag_no_tabs_list, container, false);
            FragmentManager fragmentManager = parent.getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.containerForList, ItemsListFragment.getInstance(ItemsListFragment.TAB_POS_STOPS, false))
                    .commit();
        }
        return v;
    }
}
