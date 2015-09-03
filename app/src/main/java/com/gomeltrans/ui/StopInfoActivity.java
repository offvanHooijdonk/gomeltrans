package com.gomeltrans.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.dao.StopsDao;
import com.gomeltrans.model.Stop;

/**
 * Created by Yahor_Fralou on 8/31/2015.
 */
public class StopInfoActivity extends AppCompatActivity {
    public static final String EXTRA_STOP_ID = "extra_stop_id";

    private Toolbar toolbar;
    private TextView textName;
    private TextView textComment;

    private StopInfoActivity that;
    private Long stopId;
    private StopsDao stopsDao;
    private Stop stopBean;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_info);

        that = this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textName = (TextView) findViewById(R.id.textStopName);
        textComment = (TextView) findViewById(R.id.textStopComment);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(EXTRA_STOP_ID)) {
            stopId = getIntent().getExtras().getLong(EXTRA_STOP_ID);
            stopsDao = new StopsDao(that);
            stopBean = stopsDao.getById(stopId);
            if (stopBean != null) {
                getSupportActionBar().setTitle(stopBean.getName());
                textName.setText(stopBean.getName());
                textComment.setText(stopBean.getComment());
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
        return true;
    }

    private void onNoStop() {
        // TODO show text view
        Toast.makeText(that, "No stop!", Toast.LENGTH_LONG).show();
    }
}
