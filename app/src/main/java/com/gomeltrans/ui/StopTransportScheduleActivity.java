package com.gomeltrans.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopTableDao;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.helper.AppHelper;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.StopTable;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.adapter.ScheduleHourAdapter;
import com.gomeltrans.ui.view.TransportBadgeView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Yahor_Fralou on 9/17/2015.
 */
public class StopTransportScheduleActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSPORT_ID = "extra_transport_id";
    public static final String EXTRA_STOP_ID = "extra_stop_id";
    public static final String EXTRA_DATE_PICKED = "extra_date_picked";
    public static final String EXTRA_DAY_TYPE_PICKED = "extra_day_type_picked";

    private StopTransportScheduleActivity that;
    private StopsDao stopsDao;
    private TransportDao transportDao;
    private StopTableDao stopTableDao;
    private Stop stopBean;
    private Transport transportBean;
    private List<ScheduleHourAdapter.HourSchedule> hourScheduleList = new ArrayList<>();
    private Date datePicked;
    private Integer dayTypeCode;

    private Toolbar toolbar;
    private CoordinatorLayout coordinatorLayout;
    private TextView textRouteName;
    private TextView textStopName;
    private TextView textStopComment;
    private TransportBadgeView badgeView;
    private RecyclerView recyclerView;
    private ScheduleHourAdapter hourAdapter;
    private LinearLayoutManager recyclerLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_transport_schedule);
        that = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator);

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey(EXTRA_STOP_ID) && extras.containsKey(EXTRA_TRANSPORT_ID)) {
            Long stopId = extras.getLong(EXTRA_STOP_ID);
            Long transportId = extras.getLong(EXTRA_TRANSPORT_ID);
            if (extras.containsKey(EXTRA_DATE_PICKED)) {
                datePicked = new Date(extras.getLong(EXTRA_DATE_PICKED));
            }
            if (extras.containsKey(EXTRA_DAY_TYPE_PICKED)) {
                dayTypeCode = extras.getInt(EXTRA_DAY_TYPE_PICKED);
            }

            stopsDao = new StopsDao(that);
            transportDao = new TransportDao(that);

            stopBean = stopsDao.getById(stopId);
            transportBean = transportDao.getById(transportId);

            if (stopBean != null && transportBean != null) {
                stopTableDao = new StopTableDao(that);

                textRouteName = (TextView) findViewById(R.id.textRoutName);
                textStopName = (TextView) findViewById(R.id.textStopName);
                textStopComment = (TextView) findViewById(R.id.textStopComment);
                badgeView = (TransportBadgeView) findViewById(R.id.transportBadge);

                badgeView.setNumberName(transportBean.getNumberName());
                badgeView.setTransportType(transportBean.getTypeNumber());
                textRouteName.setText(transportBean.getRouteName());
                textStopName.setText(stopBean.getName());
                textStopComment.setText(stopBean.getComment());

                recyclerView = (RecyclerView) findViewById(R.id.recyclerList);
                recyclerLayoutManager = new LinearLayoutManager(that);
                recyclerView.setLayoutManager(recyclerLayoutManager);
                hourAdapter = new ScheduleHourAdapter(that, hourScheduleList, null);
                recyclerView.setAdapter(hourAdapter);

                updateSchedule();
                updateDayInfo();
            } else {
                noSchedule();
            }
        } else {
            noSchedule();
        }
    }

    private void updateSchedule() {
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
        int dayType = datePicked != null ? StopTable.getDayType(datePicked).getCode() : (dayTypeCode != null ? dayTypeCode : StopTable.getDayType(dateQuery).getCode());
        final String upcomingTime = stopTableDao.getNextTimeThisDay(transportBean.getId(), stopBean.getId(), dateQuery, null, dayType);
        hourAdapter.setUpcomingTime(upcomingTime);

        List<String> timeList = stopTableDao.getTimeSchedule(stopBean.getId(), transportBean.getId(), dayType);
        List<ScheduleHourAdapter.HourSchedule> hours = ScheduleHourAdapter.timeToHoursList(timeList);

        hourScheduleList.clear();
        hourScheduleList.addAll(hours);
        hourAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              scrollToUpcomingTime(upcomingTime);
            }
        }, 10);
    }

    private void scrollToUpcomingTime(String upcomingTime) {
        if (upcomingTime != null) {
            String hour = upcomingTime.split(":")[0];
            int i = 0;
            for (ScheduleHourAdapter.HourSchedule hs : hourScheduleList) {
                if (hour.equals(hs.getHour())) {
                    break;
                }
                i++;
            }
            Toast.makeText(that, String.valueOf(i), Toast.LENGTH_LONG).show();
            recyclerLayoutManager.scrollToPositionWithOffset(i, 0);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void noSchedule() {
        Toast.makeText(that, "Data not found or not enough parameters", Toast.LENGTH_LONG).show();
    }

    private void updateDayInfo() {
        String dayInfo = AppHelper.createDayInfo(that, dayTypeCode == null ? null : StopTable.DAY_TYPE.create(dayTypeCode), datePicked);
        Snackbar.make(coordinatorLayout, AppHelper.colorizeTextForSnackbar(that, dayInfo), Snackbar.LENGTH_INDEFINITE).show();
    }
}
