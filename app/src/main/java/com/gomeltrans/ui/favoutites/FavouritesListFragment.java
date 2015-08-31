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
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.favoutites.adapter.FavouriteStopAdapter;
import com.gomeltrans.ui.favoutites.adapter.FavouriteTransportAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class FavouritesListFragment extends Fragment {
    public static final int TAB_POS_BUS = 0;
    public static final int TAB_POS_TROLLEY = 1;
    public static final int TAB_POS_STOPS = 2;

    private static final String ARG_PAGE_NUMBER = "arg_page_number";
    private int pageNumber;
    private Context ctx;

    private final Handler handler = new Handler();
    private RecyclerView recyclerList;
    private FavouriteTransportAdapter transportAdapter;
    private FavouriteStopAdapter stopAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private TransportDao transportDao;
    private StopsDao stopsDao;
    private List<Transport> transportList = new ArrayList<>();
    private List<Stop> stopsList = new ArrayList<>();

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

        if (pageNumber == TAB_POS_BUS || pageNumber == TAB_POS_TROLLEY) {
            transportDao = new TransportDao(ctx);

            transportAdapter = new FavouriteTransportAdapter(ctx, transportList);
            recyclerList.setAdapter(transportAdapter);
            updateData();
        } else if (pageNumber == TAB_POS_STOPS) {
            stopsDao = new StopsDao(ctx);

            stopAdapter = new FavouriteStopAdapter(ctx, stopsList);
            recyclerList.setAdapter(stopAdapter);
            updateData();
        }

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_one, R.color.refresh_two, R.color.refresh_three);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {// just for fun
                        updateData();
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

    private void updateData() {
        switch (pageNumber) {
            case TAB_POS_BUS: {
                transportList.clear();
                transportList.addAll(transportDao.getList(Transport.TRANSPORT_TYPE.BUS.getCode(), false));
                transportAdapter.notifyDataSetChanged();
            } break;
            case TAB_POS_TROLLEY: {
                transportList.clear();
                transportList.addAll(transportDao.getList(Transport.TRANSPORT_TYPE.TROLLEY.getCode(), false));
                transportAdapter.notifyDataSetChanged();
            } break;
            case TAB_POS_STOPS: {
                stopsList.clear();
                stopsList.addAll(stopsDao.getList(false));
                stopAdapter.notifyDataSetChanged();
            } break;
        }
    }
}
