package com.gomeltrans.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.gomeltrans.data.ScheduleHelper;
import com.gomeltrans.helper.AppHelper;
import com.gomeltrans.helper.IntentsHelper;
import com.gomeltrans.helper.NotificationsUtil;
import com.gomeltrans.ui.lists.TabbedListsFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReloadDataBean.OnReloadFinishedListener {

    private static final long DRAWER_CLOSE_DELAY_MS = 250;
    private static final String NAV_ITEM_ID = "nav_item_id";

    private final Handler drawerActionHandler = new Handler();
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Fragment fragmentCurrent;

    private MainActivity that;
    private int navItemId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        that = this;

        PreferenceManager.setDefaultValues(that, R.xml.pref, false);
        AppHelper.applyLocale(getBaseContext());

        navigationView = (NavigationView) findViewById(R.id.navigation_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        if (null == savedInstanceState) {
            navItemId = R.id.item_timetable;
        } else {
            navItemId = savedInstanceState.getInt(NAV_ITEM_ID);
        }
        navigationView.getMenu().findItem(navItemId).setChecked(true);
        setUpdateDateText();

        drawerToggle = new ActionBarDrawerToggle(that, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close) {
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                that.setUpdateDateText();
            }
        };
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        navigate(navItemId);

        if (Constants.isFirstTimeLaunched(that)) {
            startDataUpdate();
            Constants.setAlreadyLaunched(that);
            ScheduleHelper.scheduleUpdate(that);
        }
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
            drawerActionHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    navigate(menuItem.getItemId());
                }
            }, DRAWER_CLOSE_DELAY_MS);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void navigate(final int itemId) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (itemId == R.id.item_timetable) {
            fragmentCurrent = TabbedListsFragment.getInstance();
        } else {
            // TODO make better
            return;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragmentCurrent)
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
            startDataUpdate();
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
        setUpdateDateText();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        // if a fragment with data lists - make it redraw lists
        if (fragmentCurrent != null && (fragmentCurrent instanceof TabbedListsFragment)) {
            ((TabbedListsFragment) fragmentCurrent).refreshLists();
        }

        /*NotificationsUtil notificationsUtil = new NotificationsUtil(that);
        notificationsUtil.notifyUpdateIfEnabled();*/
    }

    private void startDataUpdate() {
        progressDialog = new ProgressDialog(that);
        progressDialog.setMessage(that.getString(R.string.progress_data_update));
        progressDialog.setCancelable(false);
        progressDialog.show();
        ReloadDataBean reloadDataBean = new ReloadDataBean(that, that);
        reloadDataBean.reloadData();
    }

    private void setUpdateDateText() {
        navigationView.getMenu().findItem(R.id.item_last_updated).setTitle(that.getResources().getString(R.string.last_updated,
                Constants.getUpdateDate(that)));
    }
}
