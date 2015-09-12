package com.gomeltrans.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.data.service.StopService;
import com.gomeltrans.helper.AppHelper;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.ui.actionbar.FavouriteActionProvider;
import com.gomeltrans.ui.view.TransportBadgeView;

import org.apmem.tools.layouts.FlowLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/31/2015.
 */
public class StopInfoActivity extends AppCompatActivity implements FavouriteActionProvider.ToggleListener, DatePickerDialog.OnDateSetListener {
    public static final String EXTRA_STOP_ID = "extra_stop_id";

    private Toolbar toolbar;
    private Menu optionsMenu;
    private CoordinatorLayout coordinatorLayout;
    private FlowLayout blockUpcomingTransport;

    private StopInfoActivity that;
    private StopsDao stopsDao;
    private StopService stopService;
    private Stop stopBean;
    private StopTable.DAY_TYPE dayType = null;
    private Date datePicked;
    private List<StopTable> upcomingTransportTable = new ArrayList<>();
    //private TransportDao transportDao;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_info);

        that = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRA_STOP_ID)) {
            Long stopId = getIntent().getExtras().getLong(EXTRA_STOP_ID);
            stopsDao = new StopsDao(that);
            stopBean = stopsDao.getById(stopId);
            if (stopBean != null) {
                getSupportActionBar().setTitle(stopBean.getName());
                getSupportActionBar().setSubtitle(stopBean.getComment());

                coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);
                blockUpcomingTransport = (FlowLayout) findViewById(R.id.blockUpcomingTransport);

                //transportDao = new TransportDao(that);
                stopService = new StopService(that);

                updateUpcomingTransport();
                updateDayInfo();
            } else {
                onNoStop();
            }
        } else {
            onNoStop();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stop_info, menu);
        this.optionsMenu = menu;
        if (stopBean != null) {
            FavouriteActionProvider provider = (FavouriteActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.action_favourite_toggle));
            provider.setFavourite(stopBean.isFavourite());
            provider.addToggleListener(this);
        } else {
            menu.findItem(R.id.action_favourite_toggle).setVisible(false);
        }
        return true;
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

    private void updateUpcomingTransport() {
        upcomingTransportTable.clear();
        upcomingTransportTable.addAll(stopService.getUpcomingTransport(stopBean, new Date(), AppHelper.TABLE_UPCOMING_MINUTES, null));

        displayUpcomingTable();
    }

    private void displayUpcomingTable() {
        blockUpcomingTransport.removeAllViews();
        int index = 0;
        for (final StopTable st : upcomingTransportTable) {
            ViewGroup v = (ViewGroup) LayoutInflater.from(that).inflate(R.layout.item_upcoming_transport_time, blockUpcomingTransport, false);

            TransportBadgeView badgeView = (TransportBadgeView) v.findViewById(R.id.blockBadge);
            // if previous item is the same transport - do not show badge for that item
            if (index > 0 && (st.getTransport().equals(upcomingTransportTable.get(index -1).getTransport()))) {
                badgeView.setVisibility(View.GONE);
            } else {
                badgeView.setNumberName(st.getTransport().getNumberName());
                badgeView.setTransportType(st.getTransport().getTypeNumber());
            }
            ((TextView) v.findViewById(R.id.textNextTime)).setText(st.getTimeUpcoming());

            /*if (st.getTransport().isFavourite()) {
                v.findViewById(R.id.blockBackground).setBackgroundColor(AppHelper.applyAlphaToColor(that.getResources().getColor(R.color.fav_item_bckgr),
                        AppHelper.FAV_BACKGR_ALPHA));
            }*/

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(that, "We'll show you info on " + st.getTransport().getNumberName(), Toast.LENGTH_LONG).show();
                }
            });
            blockUpcomingTransport.addView(v);
            index++;
        }
    }

    private void onDayTypeChange() {
        //refreshLists();
        updateDayInfo();
    }

    private void updateDayInfo() {
        String dayInfo = AppHelper.createDayInfo(that, dayType, datePicked);
        Snackbar.make(coordinatorLayout, AppHelper.colorizeTextForSnackbar(that, dayInfo), Snackbar.LENGTH_INDEFINITE).show();
    }

    private void onNoStop() {
        // TODO show text view
        Toast.makeText(that, "No stop!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFavTogglerStateChanged(boolean newValue) {
        stopsDao.setFavourite(stopBean.getId(), newValue);
    }
}
