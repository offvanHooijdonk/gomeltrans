package com.gomeltrans.ui.favoutites;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gomeltrans.R;
import com.gomeltrans.model.Bus;
import com.gomeltrans.ui.favoutites.adapter.FavouriteBusAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class FavouritesListFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "arg_page_number";
    private int pageNumber;
    private Context ctx;

    private final Handler handler = new Handler();
    private RecyclerView recyclerList;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static FavouritesListFragment getInstance(int pageNumArg) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumArg);

        FavouritesListFragment fragment = new FavouritesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);

        this.ctx = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_fav_list, container, false);

        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setHasFixedSize(true);
        recyclerList.setLayoutManager(new LinearLayoutManager(ctx));

        List<Bus> buses = new ArrayList<>();
        Bus bus = new Bus();
        bus.setId(16l);
        bus.setNumberName("12a");
        bus.setRouteName("ул. Зайцева - Завод самоходных комбайнов");
        buses.add(bus);
        bus = new Bus();
        bus.setId(23l);
        bus.setNumberName("17");
        bus.setRouteName("Медгородок - м-р Клёнковский");
        buses.add(bus);

        FavouriteBusAdapter adapter = new FavouriteBusAdapter(ctx, buses);
        recyclerList.setAdapter(adapter);

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_one, R.color.refresh_two, R.color.refresh_three);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 750);
            }
        });


        return v;
    }

    public static String getTabTitle(Context ctx, int position) {
        String title;
        switch (position) {
            case 0: {
                title = ctx.getString(R.string.tab_bus);
            } break;
            case 1: {
                title = ctx.getString(R.string.tab_trolley);
            } break;
            case 2: {
                title = ctx.getString(R.string.tab_stop);
            } break;
            default: {
                title = ctx.getString(R.string.tab_bus);
            }
        }

        return title;
    }
}
