package com.gomeltrans.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gomeltrans.helper.IntentsHelper;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.actionbar.FavouriteActionProvider;
import com.gomeltrans.ui.adapter.StopTableAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/31/2015.
 */
public class StopInfoActivity extends AppCompatActivity implements FavouriteActionProvider.ToggleListener, DatePickerDialog.OnDateSetListener, StopTableAdapter.OnItemSelected {
    public static final String EXTRA_STOP_ID = "extra_stop_id";

    private Toolbar toolbar;
    private Menu optionsMenu;
    private CoordinatorLayout coordinatorLayout;
    private com.wefika.flowlayout.FlowLayout blockUpcomingTransport;
    private RecyclerView recyclerView;
    private StopTableAdapter stopTableAdapter;

    private StopInfoActivity that;
    private StopsDao stopsDao;
    private StopService stopService;
    private Stop stopBean;
    private StopTable.DAY_TYPE dayType = null;
    private Date datePicked;
    private List<StopTable> upcomingTransportTable = new ArrayList<>();
    private List<StopTable> transportTableList = new ArrayList<>();
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
                blockUpcomingTransport = (com.wefika.flowlayout.FlowLayout) findViewById(R.id.blockUpcomingTransport);
                recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
                recyclerView.setLayoutManager(new LinearLayoutManager(that));
                stopTableAdapter = new StopTableAdapter(that, transportTableList);
                stopTableAdapter.setListener(that);
                recyclerView.setAdapter(stopTableAdapter);

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

    private void updateTransportList() {
        transportTableList.clear();
        // TODO put real arguments
        transportTableList.addAll(stopService.getStopTransportWithUpcomingTime(stopBean, new Date(), dayType != null ? dayType : StopTable.DAY_TYPE.WORKING));

        stopTableAdapter.notifyDataSetChanged();
    }

    private void updateUpcomingTransport() {
        Date dateQuery = new Date();
        if (datePicked != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateQuery);

            Calendar calendarPicked = Calendar.getInstance();
            calendarPicked.setTime(datePicked);

            calendar.set(Calendar.YEAR, calendarPicked.get(Calendar.YEAR));
            calendar.set(Calendar.MONTH, calendarPicked.get(Calendar.MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, calendarPicked.get(Calendar.DAY_OF_MONTH));

            dateQuery = calendar.getTime();
        }
        upcomingTransportTable.clear();
        upcomingTransportTable.addAll(stopService.getUpcomingTransport(stopBean, dateQuery, AppHelper.TABLE_UPCOMING_MINUTES, dayType));

        displayUpcomingTable();

        updateTransportList();
    }

    private void displayUpcomingTable() {
        blockUpcomingTransport.removeAllViews();
        int index = 0;
        for (final StopTable st : upcomingTransportTable) {
            ViewGroup v = (ViewGroup) LayoutInflater.from(that).inflate(R.layout.item_upcoming_transport_time, blockUpcomingTransport, false);

            /*TransportBadgeView badgeView = (TransportBadgeView) v.findViewById(R.id.blockBadge);
            // if previous item is the same transport - do not show badge for that item
            if (index > 0 && (st.getTransport().equals(upcomingTransportTable.get(index -1).getTransport()))) {
                badgeView.setVisibility(View.GONE);
            } else {
                badgeView.setNumberName(st.getTransport().getNumberName());
                badgeView.setTransportType(st.getTransport().getTypeNumber());
            }*/

            ViewGroup blockName = (ViewGroup) v.findViewById(R.id.blockNumberName);
            TextView textNumberBus = (TextView) v.findViewById(R.id.textNumberNameBus);
            TextView textNumberTrolley = (TextView) v.findViewById(R.id.textNumberNameTrolley);

            if (index > 0 && (st.getTransport().equals(upcomingTransportTable.get(index - 1).getTransport()))) {
                blockName.setVisibility(View.GONE);
            } else {
                if (st.getTransport().getTypeNumber() == Transport.TRANSPORT_TYPE.TROLLEY.getCode()) {
                    textNumberTrolley.setVisibility(View.VISIBLE);
                    textNumberBus.setVisibility(View.GONE);
                    textNumberTrolley.setText(st.getTransport().getNumberName());
                } else {
                    textNumberTrolley.setVisibility(View.GONE);
                    textNumberBus.setVisibility(View.VISIBLE);
                    textNumberBus.setText(st.getTransport().getNumberName());
                }
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
        updateUpcomingTransport();
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

    @Override
    public void onStopScheduleSelected(Long transportId) {
        IntentsHelper.startStopTransportSchedule(that, stopBean.getId(), transportId, datePicked, dayType);
    }
}
