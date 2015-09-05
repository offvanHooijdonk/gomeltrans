package com.gomeltrans.ui;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Transport;
import com.gomeltrans.model.TransportStops;
import com.gomeltrans.ui.actionbar.FavouritesActionProvider;
import com.gomeltrans.ui.lists.adapter.TransportStopAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yahor on 25.08.15.
 */
public class TransportInfoActivity extends AppCompatActivity implements FavouritesActionProvider.ToggleListener {
    public static final String EXTRA_TRANSPORT_ID = "extra_transport_id";

    private TransportInfoActivity that;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TransportStopAdapter adapter;

    private Transport transportBean;
    private TransportDao transportDao;
    private List<TransportStops> transportStopsList;
    private boolean justCreated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_info);
        that = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRA_TRANSPORT_ID)) {
            Long transportId = getIntent().getExtras().getLong(EXTRA_TRANSPORT_ID);
            transportDao = new TransportDao(that);
            transportBean = transportDao.getById(transportId);
            if (transportBean != null) {
                getSupportActionBar().setTitle(String.format("%s %s", transportBean.getNumberName(), transportBean.getRouteName()));

                // init recycler
                recyclerView = (RecyclerView) findViewById(R.id.listStops);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(that));

                transportStopsList = new ArrayList<>();
                adapter = new TransportStopAdapter(that, transportStopsList);
                recyclerView.setAdapter(adapter);

                updateList();

                justCreated = true;
            } else {
                onNoTransport();
            }

        } else {
            onNoTransport();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transport_info, menu);

        if (transportBean != null) {
            FavouritesActionProvider provider = (FavouritesActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_favourite_toggle));
            provider.setFavourite(transportBean.isFavourite());
            provider.addToggleListener(this);
        } else {
            menu.findItem(R.id.action_favourite_toggle).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onStateChanged(boolean newValue) {
        transportDao.setFavourite(transportBean.getId(), newValue);
    }

    private void updateList() {
        transportStopsList.clear();
        transportStopsList.addAll(transportDao.getTransportStopNextTable(transportBean, TransportStops.DIRECTION.FORWARD, new Date()));
        adapter.notifyDataSetChanged();
    }

    private void onNoTransport() {
        // TODO show text view
        Toast.makeText(that, "No transport!", Toast.LENGTH_LONG).show();
    }

}
