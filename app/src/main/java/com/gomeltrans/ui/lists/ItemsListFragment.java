package com.gomeltrans.ui.lists;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.gomeltrans.Constants;
import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.actionbar.FavouriteFilterActionProvider;
import com.gomeltrans.ui.lists.adapter.StopAdapter;
import com.gomeltrans.ui.lists.adapter.TransportAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/25/2015.
 */
public class ItemsListFragment extends Fragment implements FavouriteFilterActionProvider.OnFavFilterChangeListener {
    public static final int TAB_POS_BUS = 0;
    public static final int TAB_POS_TROLLEY = 1;
    public static final int TAB_POS_STOPS = 2;

    private static final String ARG_PAGE_NUMBER = "arg_page_number";
    private int pageNumber;
    private Context ctx;
    private FavouriteFilterActionProvider.SHOW_MODE favMode;

    private final Handler handler = new Handler();
    private RecyclerView recyclerList;
    private TransportAdapter transportAdapter;
    private StopAdapter stopAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean justCreated;

    private TransportDao transportDao;
    private StopsDao stopsDao;
    private List<Transport> transportList = new ArrayList<>();
    private List<Stop> stopsList = new ArrayList<>();

    private String currentSearchText;

    public static ItemsListFragment getInstance(int pageNumArg) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumArg);

        ItemsListFragment fragment = new ItemsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);

        this.ctx = getActivity();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_items_list, container, false);

        try {
            favMode = FavouriteFilterActionProvider.SHOW_MODE.valueOf(Constants.getFavFilter(ctx));
        } catch (Exception e) {
            favMode = FavouriteFilterActionProvider.SHOW_MODE.SHOW_ALL;
        }

        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setHasFixedSize(true);
        recyclerList.setLayoutManager(new LinearLayoutManager(ctx));

        if (pageNumber == TAB_POS_BUS || pageNumber == TAB_POS_TROLLEY) {
            transportDao = new TransportDao(ctx);

            transportAdapter = new TransportAdapter(ctx, transportList);
            recyclerList.setAdapter(transportAdapter);
            updateData();
        } else if (pageNumber == TAB_POS_STOPS) {
            stopsDao = new StopsDao(ctx);

            stopAdapter = new StopAdapter(ctx, stopsList);
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
                        updateData(currentSearchText);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 750);
            }
        });

        justCreated = true;

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!justCreated) {
            updateData(currentSearchText);
        } else {
            justCreated = false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem favFilterItem = menu.findItem(R.id.action_fav_filter);
        FavouriteFilterActionProvider favFilterAP = (FavouriteFilterActionProvider) MenuItemCompat.getActionProvider(favFilterItem);
        favFilterAP.setShowMode(favMode);
        favFilterAP.addListener(this);

        MenuItem searchItem = menu.findItem(R.id.action_search_stop).setVisible(pageNumber == TAB_POS_STOPS);
        if (pageNumber == TAB_POS_STOPS) {
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }
                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    currentSearchText = null;
                    return true;
                }
            });

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    currentSearchText = newText;
                    updateData(newText);
                    return true;
                }
            });

        }
    }

    @Override
    public void onFavFilterChanged(FavouriteFilterActionProvider.SHOW_MODE newValue) {
        favMode = newValue;
        Constants.saveFavFilter(ctx, newValue.toString());
        updateData(currentSearchText);
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
        updateData(null);
    }

    private void updateData(String searchText) {
        switch (pageNumber) {
            case TAB_POS_BUS: {
                transportList.clear();
                transportList.addAll(transportDao.getList(Transport.TRANSPORT_TYPE.BUS.getCode(),
                        favMode == FavouriteFilterActionProvider.SHOW_MODE.FAV_ONLY, favMode == FavouriteFilterActionProvider.SHOW_MODE.FAV_FIRST));
                transportAdapter.notifyDataSetChanged();
            } break;
            case TAB_POS_TROLLEY: {
                transportList.clear();
                transportList.addAll(transportDao.getList(Transport.TRANSPORT_TYPE.TROLLEY.getCode(),
                        favMode == FavouriteFilterActionProvider.SHOW_MODE.FAV_ONLY, favMode == FavouriteFilterActionProvider.SHOW_MODE.FAV_FIRST));
                transportAdapter.notifyDataSetChanged();
            } break;
            case TAB_POS_STOPS: {
                stopAdapter.setSearchText(searchText);
                stopsList.clear();
                stopsList.addAll(stopsDao.searchList(searchText,
                        favMode == FavouriteFilterActionProvider.SHOW_MODE.FAV_ONLY, favMode == FavouriteFilterActionProvider.SHOW_MODE.FAV_FIRST));
                stopAdapter.notifyDataSetChanged();
            } break;
        }
    }

}
