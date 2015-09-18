package com.gomeltrans.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gomeltrans.R;
import com.gomeltrans.helper.AppHelper;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yahor_Fralou on 9/18/2015.
 */
public class ScheduleHourAdapter extends RecyclerView.Adapter<ScheduleHourAdapter.ViewHolder> {

    private Context ctx;
    private List<HourSchedule> hourScheduleList;
    private String upcomingTime;

    public ScheduleHourAdapter(Context context, List<HourSchedule> hourScheduleList, String upcomingTime) {
        this.ctx = context;
        this.hourScheduleList = hourScheduleList;
        this.upcomingTime = upcomingTime;
    }

    public void setUpcomingTime(String upcomingTime) {
        this.upcomingTime = upcomingTime;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_schedule_hour, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {
        HourSchedule hs = hourScheduleList.get(position);

        boolean isUpcomingHour = false;
        String upcMinute = "";
        if (upcomingTime != null) {
            String[] upcomingTimeSplit = upcomingTime.split(":");
            isUpcomingHour = upcomingTimeSplit[0].equals(hs.getHour());
            upcMinute = upcomingTimeSplit[1];
        }
        vh.textHour.setText(hs.getHour());

        vh.blockTime.removeAllViews();
        for (String minute : hs.getMinutes()) {
            View minuteView = LayoutInflater.from(ctx).inflate(R.layout.item_schedule_minute, vh.blockTime, false);
            TextView textMinute = (TextView) minuteView.findViewById(R.id.textMinute);
            textMinute.setText(minute);

            if (isUpcomingHour && minute.equals(upcMinute)) {
                textMinute.setTextColor(ctx.getResources().getColor(R.color.app_accent));
                textMinute.setTypeface(textMinute.getTypeface(), Typeface.BOLD);
            }
            vh.blockTime.addView(minuteView);
        }

        if (isUpcomingHour) {
            vh.blockItem.setBackgroundColor(AppHelper.applyAlphaToColor(ctx.getResources().getColor(R.color.upcoming_time), AppHelper.FAV_BACKGR_ALPHA));
        } else {
            vh.blockItem.setBackgroundColor(ctx.getResources().getColor(R.color.background_material_light));
        }
    }

    @Override
    public int getItemCount() {
        return hourScheduleList.size();
    }

    /**
     *
     * @param timeList assume it is sorted already
     * @return
     */
    public static List<HourSchedule> timeToHoursList(List<String> timeList) {
        List<HourSchedule> hourList = new ArrayList<>();
        String latestHour = "";
        HourSchedule hs = null;
        for (String time : timeList) {
            String[] split = time.split(":");
            String hour = split[0];
            String minute = split[1];
            if (hour.equals(latestHour)) {
                if (hs != null) {
                    hs.getMinutes().add(minute);
                }
            } else {
                if (hs != null) {
                    hourList.add(hs);
                }
                hs = new HourSchedule(hour, new ArrayList<String>());
                hs.getMinutes().add(minute);
                latestHour = hour;
            }
        }

        if (hs != null) {
            hourList.add(hs);
        }

        return hourList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewGroup blockItem;
        FlowLayout blockTime;
        TextView textHour;

        public ViewHolder(View v) {
            super(v);

            blockItem = (ViewGroup) v.findViewById(R.id.blockItem);
            blockTime = (FlowLayout) v.findViewById(R.id.blockTime);
            textHour = (TextView) v.findViewById(R.id.textHour);
        }
    }

    public static class HourSchedule {
        private String hour;
        private List<String> minutes;

        public HourSchedule(String hour, List<String> minutes) {
            this.hour = hour;
            this.minutes = minutes;
        }

        public String getHour() {
            return hour;
        }

        public void setHour(String hour) {
            this.hour = hour;
        }

        public List<String> getMinutes() {
            return minutes;
        }

        public void setMinutes(List<String> minutes) {
            this.minutes = minutes;
        }
    }
}
