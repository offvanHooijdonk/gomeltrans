package com.gomeltrans.ui.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.gomeltrans.R;
import com.gomeltrans.data.ScheduleHelper;
import com.gomeltrans.helper.AppHelper;

/**
 * Created by Yahor_Fralou on 9/8/2015.
 */
public class PreferenceActivity extends AppCompatActivity {
    private PrefChangeListener prefChangeListener = new PrefChangeListener();

    private PreferenceActivity that;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        that = this;

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(prefChangeListener);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref);
        }
    }

    private class PrefChangeListener implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(that.getString(R.string.pref_check_update_rate_key))) {
                ScheduleHelper.scheduleUpdate(that);
            } else if (key.equals("pref_bel")) {
                AppHelper.applyLocale(getBaseContext());
                Toast.makeText(that, R.string.restart_app_for_locale, Toast.LENGTH_LONG).show();
            }
        }
    }
}
