package com.gomeltrans.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.data.dao.TransportDao;
import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.gomeltrans.ui.view.TransportBadgeView;

/**
 * Created by Yahor_Fralou on 9/17/2015.
 */
public class StopTransportTableActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSPORT_ID = "extra_transport_id";
    public static final String EXTRA_STOP_ID = "extra_stop_id";

    private StopTransportTableActivity that;
    private StopsDao stopsDao;
    private TransportDao transportDao;

    private Toolbar toolbar;
    private TextView textRouteName;
    private TextView textStopName;
    private TextView textStopComment;
    private TransportBadgeView badgeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_transport_table);
        that = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRA_STOP_ID) &&
                getIntent().getExtras().containsKey(EXTRA_TRANSPORT_ID)) {
            Long stopId = getIntent().getExtras().getLong(EXTRA_STOP_ID);
            Long transportId = getIntent().getExtras().getLong(EXTRA_TRANSPORT_ID);

            stopsDao = new StopsDao(that);
            transportDao = new TransportDao(that);

            Stop stopBean = stopsDao.getById(stopId);
            Transport transportBean = transportDao.getById(transportId);

            if (stopBean != null && transportBean != null) {
                textRouteName = (TextView) findViewById(R.id.textRoutName);
                textStopName = (TextView) findViewById(R.id.textStopName);
                textStopComment = (TextView) findViewById(R.id.textStopComment);
                badgeView = (TransportBadgeView) findViewById(R.id.transportBadge);

                badgeView.setNumberName(transportBean.getNumberName());
                badgeView.setTransportType(transportBean.getTypeNumber());
                textRouteName.setText(transportBean.getRouteName());
                textStopName.setText(stopBean.getName());
                textStopComment.setText(stopBean.getComment());
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
