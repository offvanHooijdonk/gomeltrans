package com.gomeltrans.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.actionbar.FavouriteActionProvider;
import com.gomeltrans.ui.lists.TransportStopsPagerAdapter;

/**
 * Created by yahor on 25.08.15.
 */
public class TransportInfoActivity extends AppCompatActivity implements FavouriteActionProvider.ToggleListener {
    public static final String EXTRA_TRANSPORT_ID = "extra_transport_id";

    private TransportInfoActivity that;
    private Toolbar toolbar;

    private Transport transportBean;
    private TransportDao transportDao;

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

                // init tabs
                final TransportStopsPagerAdapter pagerAdapter = new TransportStopsPagerAdapter(getSupportFragmentManager(), that, transportId);
                final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setAdapter(pagerAdapter);

                final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                tabLayout.post(new Runnable() { // this is a hack due to a bug in 22.2.1 design lib
                    @Override
                    public void run() {
                        tabLayout.setupWithViewPager(viewPager);
                    }
                });

                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Log.i("gml", "onPageScrolled " + position);
                    }

                    @Override
                    public void onPageSelected(int position) {
                        Log.i("gml", "onPageSelected " + position);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        Log.i("gml", "onPageScrollStateChanged " + state);
                    }
                });
            } else {
                onNoTransport();
            }

        } else {
            onNoTransport();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.transport_info, menu);

        if (transportBean != null) {
            FavouriteActionProvider provider = (FavouriteActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_favourite_toggle));
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
    public void onFavTogglerStateChanged(boolean newValue) {
        transportDao.setFavourite(transportBean.getId(), newValue);
    }

    private void onNoTransport() {
        // TODO show text view
        Toast.makeText(that, "No transport!", Toast.LENGTH_LONG).show();
    }

}
