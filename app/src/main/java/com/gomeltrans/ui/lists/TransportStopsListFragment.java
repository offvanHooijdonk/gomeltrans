package com.gomeltrans.ui.lists;

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
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Transport;
import com.gomeltrans.model.TransportStops;
import com.gomeltrans.ui.lists.adapter.TransportStopAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yahor on 06.09.15.
 */
public class TransportStopsListFragment extends Fragment {
    private static final int TAB_POSITION_FORWARD = 0;
    private static final int TAB_POSITION_BACKWARD = 1;
    private static final String ARG_PAGE_NUMBER = "arg_page_number";
    private static final String ARG_TRANSPORT_ID = "arg_transport_id";

    private Context ctx;
    private final Handler handler = new Handler();

    private RecyclerView recyclerList;
    private TransportStopAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    private int pageNumber;
    private TransportDao transportDao;
    private List<TransportStops> transportStopsList;
    private boolean justCreated;
    private Transport transportBean;
    private long transportId;

    public static TransportStopsListFragment getInstance(int pageNumArg, long transportIdArg) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumArg);
        args.putLong(ARG_TRANSPORT_ID, transportIdArg);

        TransportStopsListFragment fragment = new TransportStopsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
        this.transportId = getArguments().getLong(ARG_TRANSPORT_ID);

        this.ctx = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_items_list, container, false);

        transportDao = new TransportDao(ctx);
        transportBean = transportDao.getById(transportId);

        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);
        recyclerList.setHasFixedSize(true);
        recyclerList.setLayoutManager(new LinearLayoutManager(ctx));

        transportStopsList = new ArrayList<>();
        adapter = new TransportStopAdapter(ctx, transportStopsList);
        recyclerList.setAdapter(adapter);

        updateList();

        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_one, R.color.refresh_two, R.color.refresh_three);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {// just for fun
                        updateList();
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
            updateList();
        } else {
            justCreated = false;
        }
    }

    private void updateList() {
        transportStopsList.clear();
        transportStopsList.addAll(transportDao.getTransportStopNextTable(transportBean, getDirection(pageNumber), new Date()));
        adapter.notifyDataSetChanged();
    }

    private TransportStops.DIRECTION getDirection(int position) {
        return position == TAB_POSITION_BACKWARD ? TransportStops.DIRECTION.BACKWARD : TransportStops.DIRECTION.FORWARD;
    }

    public static String getTitle(Context ctx, int position) {
        String title;

        switch (position) {
            case TAB_POSITION_FORWARD: title = ctx.getString(R.string.item_direction_forward); break;
            case TAB_POSITION_BACKWARD: title = ctx.getString(R.string.item_direction_backward); break;
            default: title = ctx.getString(R.string.item_direction_forward);
        }
        return title;
    }
}
