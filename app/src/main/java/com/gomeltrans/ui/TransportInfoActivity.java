package com.gomeltrans.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.actionbar.FavouriteActionProvider;
import com.gomeltrans.ui.lists.TransportStopsPagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yahor on 25.08.15.
 */
public class TransportInfoActivity extends AppCompatActivity implements FavouriteActionProvider.ToggleListener, DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_TRANSPORT_ID = "extra_transport_id";

    private TransportInfoActivity that;
    private Toolbar toolbar;
    private Menu optionsMenu;
    private CoordinatorLayout coordinatorLayout;

    private TransportStopsPagerAdapter pagerAdapter;
    private Transport transportBean;
    private TransportDao transportDao;
    private StopTable.DAY_TYPE dayType = null;
    private Date datePicked;

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

                coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
                // init tabs
                pagerAdapter = new TransportStopsPagerAdapter(getSupportFragmentManager(), that, transportId);
                final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
                viewPager.setAdapter(pagerAdapter);

                final TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                tabLayout.post(new Runnable() { // this is a hack due to a bug in 22.2.1 design lib
                    @Override
                    public void run() {
                        tabLayout.setupWithViewPager(viewPager);
                    }
                });

                updateDayInfo();
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
        optionsMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.actions_day_today) {
            item.setChecked(true);
            dayType = null;
            datePicked = null;
            onDayTypeChange();
        } else if (item.getItemId() == R.id.actions_day_working) {
            item.setChecked(true);
            dayType = StopTable.DAY_TYPE.WORKING;
            datePicked = null;
            onDayTypeChange();
        } else if (item.getItemId() == R.id.actions_day_weekend) {
            item.setChecked(true);
            dayType = StopTable.DAY_TYPE.WEEKEND;
            datePicked = null;
            onDayTypeChange();
        } else if (item.getItemId() == R.id.actions_day_pick) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog dialog = new DatePickerDialog(that, that, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
        return false;
    }

    @Override
    public void onFavTogglerStateChanged(boolean newValue) {
        transportDao.setFavourite(transportBean.getId(), newValue);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, 12); // midday

        datePicked = calendar.getTime();
        dayType = null;

        MenuItem pickItem = optionsMenu.findItem(R.id.actions_day_pick);
        pickItem.setTitle(SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(datePicked));
        pickItem.setChecked(true);

        onDayTypeChange();
    }

    public StopTable.DAY_TYPE getDayType() {
        return dayType;
    }

    public Date getDatePicked() {
        return datePicked;
    }

    private void onDayTypeChange() {
        refreshLists();
        updateDayInfo();
    }

    private void updateDayInfo() {
        StringBuilder str = new StringBuilder();
        StopTable.DAY_TYPE dt;
        if (dayType == null) {
            if (datePicked == null) {
                str.append(that.getString(R.string.info_today));
                dt = StopTable.getDayType(new Date());
            } else {
                str.append(SimpleDateFormat.getDateInstance(DateFormat.MEDIUM).format(datePicked));
                dt = StopTable.getDayType(datePicked);
            }
            str.append(", ");
        } else {
            dt = dayType;
        }

        if (dt == StopTable.DAY_TYPE.WEEKEND) {
            str.append(that.getString(R.string.info_weekend_day));
        } else {
            str.append(that.getString(R.string.info_working_day));
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder(str.toString());
        ssb.setSpan(new ForegroundColorSpan(that.getResources().getColor(R.color.snackbar_text)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        Snackbar.make(coordinatorLayout, ssb, Snackbar.LENGTH_INDEFINITE).show();
    }

    private void refreshLists() {
        pagerAdapter.notifyDataSetChanged();
    }

    private void onNoTransport() {
        // TODO show text view
        Toast.makeText(that, "No transport!", Toast.LENGTH_LONG).show();
    }

}
