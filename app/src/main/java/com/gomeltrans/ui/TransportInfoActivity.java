package com.gomeltrans.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.gomeltrans.R;

/**
 * Created by yahor on 25.08.15.
 */
public class TransportInfoActivity extends AppCompatActivity {
    private TransportInfoActivity that;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_transport_info);
        that = this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("17");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*@Override
    protected void onDestroy() {
        NavUtils.navigateUpFromSameTask(that);
        super.onDestroy();
    }*/

}
