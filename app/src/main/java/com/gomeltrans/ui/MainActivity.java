package com.gomeltrans.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.gomeltrans.Constants;
import com.gomeltrans.R;
import com.gomeltrans.data.ReloadDataBean;
import com.gomeltrans.helper.IntentsHelper;
import com.gomeltrans.ui.lists.TabbedListsFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReloadDataBean.OnReloadFinishedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "nav_item_id";

    private final Handler drawerActionHandler = new Handler();
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private MainActivity that;
    private int navItemId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        that = this;

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (null == savedInstanceState) {
            navItemId = R.id.item_timetable;
        } else {
            navItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        navigationView.getMenu().findItem(navItemId).setChecked(true);
        setUpdateDateText();

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigate(navItemId);
    }

    @Override
    public boolean onNavigationItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.item_settings) {
            IntentsHelper.startPreference(that);
        } else { // then must be main navigation
            menuItem.setChecked(true);
            navItemId = menuItem.getItemId();

            // allow some time after closing the drawer before performing real navigation
            // so the user can see what is happening
            drawerLayout.closeDrawer(GravityCompat.START);
            drawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigate(menuItem.getItemId());
                }
            }, DRAWER_CLOSE_DELAY_MS);
        }
        return true;
    }

    private void navigate(final int itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment;

        if (itemId == R.id.item_timetable) {
            fragment = TabbedListsFragment.getInstance();
        } else {
            // TODO make better
            return;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reload_data) {
            progressDialog = new ProgressDialog(that);
            progressDialog.setMessage(that.getString(R.string.progress_data_update));
            progressDialog.setCancelable(false);
            progressDialog.show();
            ReloadDataBean reloadDataBean = new ReloadDataBean(that, that);
            reloadDataBean.reloadData();
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NAV_ITEM_ID, navItemId);
    }

    @Override
    public void onReloadDataFinished() {
        // TODO in future this date comes from server
        Constants.saveUpdateDate(that, new Date());
        setUpdateDateText();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        // TODO implement fragment awareness of data load so they can refresh theirselves if on screen
    }

    private void setUpdateDateText() {
        navigationView.getMenu().findItem(R.id.item_last_updated).setTitle(that.getResources().getString(R.string.last_updated,
                Constants.getUpdateDate(that)));
    }
}
