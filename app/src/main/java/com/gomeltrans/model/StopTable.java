package com.gomeltrans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class StopTable extends BaseBean {
    public static final int HOUR_SHIFT_START_WITH = 0;
    public static final int HOUR_SHIFT_END_WITH = 4;
    public static final int HOUR_SHIFT_BY = 24;
    public  static final String DELIMITER_TIME = ":";

    public static final String TABLE = "stop_table";
    public static final String ID = "id";
    public static final String TRANSPORT_ID = "transport_id";
    public static final String STOP_ID = "stop_id";
    public static final String DAY_TYPE_CODE = "day_type";
    public static final String TIME = "times";
    public static final String ACTIVE = "active";


    public enum DAY_TYPE {
        WORKING(0), WEEKEND(1);

        private int code;

        DAY_TYPE(int num) {
            code = num;
        }

        public int getCode() {
            return code;
        }
    }

    ;

    @Expose
    private Transport transport;
    @Expose
    @SerializedName("time")
    private List<String> times;
    @Expose
    @SerializedName("dayType")
    private int dayTypeCode;

    private String timeUpcoming;

    public String getTimeUpcoming() {
        return timeUpcoming;
    }

    public void setTimeUpcoming(String timeUpcoming) {
        this.timeUpcoming = timeUpcoming;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public List<String> getTimes() {
        return times;
    }

    public void setTimes(List<String> times) {
        this.times = times;
    }

    public int getDayTypeCode() {
        return dayTypeCode;
    }

    public void setDayTypeCode(int dayTypeCode) {
        this.dayTypeCode = dayTypeCode;
    }
/*public class TimeComparator implements Comparator<String> {
        private final Map<String, Integer> ranks = new HashMap<>();

        {
            ranks.put("5", 2);ranks.put("05", 2);
            ranks.put("6", 3);ranks.put("06", 3);
            ranks.put("7", 4);ranks.put("07", 4);
            ranks.put("8", 5);ranks.put("08", 5);
            ranks.put("9", 6);ranks.put("09", 6);
            ranks.put("10", 7);
            ranks.put("11", 8);
            ranks.put("12", 9);
            ranks.put("13", 10);
            ranks.put("14", 11);
            ranks.put("15", 12);
            ranks.put("16", 13);
            ranks.put("17", 14);
            ranks.put("18", 15);
            ranks.put("19", 16);
            ranks.put("20", 17);
            ranks.put("21", 18);
            ranks.put("22", 19);
            ranks.put("23", 20);
            ranks.put("0", 21);ranks.put("00", 21);
            ranks.put("1", 22);ranks.put("01", 22);
            ranks.put("2", 23);ranks.put("02", 23);
            ranks.put("3", 24);ranks.put("03", 24);
            ranks.put("4", 25);ranks.put("04", 25);
        }

        @Override
        public int compare(String lhs, String rhs) {
            String lHour = lhs.split(":")[0];
            String rHour = rhs.split(":")[0];
            if (lHour.equals(rHour)) {
                return lhs.compareTo(rhs);
            } else {
                return ranks.get(lhs).compareTo(ranks.get(rhs));
            }
        }
    }*/

    public static DAY_TYPE getDayType(Date date) {
        DAY_TYPE dayType;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.setTime(getDateWithShift(calendar));
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (dayOfWeek) {
            case Calendar.MONDAY:
            case Calendar.TUESDAY:
            case Calendar.WEDNESDAY:
            case Calendar.THURSDAY:
            case Calendar.FRIDAY: {
                dayType = DAY_TYPE.WORKING;
            }
            break;
            case Calendar.SATURDAY:
            case Calendar.SUNDAY: {
                dayType = DAY_TYPE.WEEKEND;
            }
            break;
            default:
                dayType = DAY_TYPE.WORKING;
        }

        return dayType;
    }

    public static Date getDateWithShift(Calendar calendar) {
        int dayOfWeek;
        int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        if (HOUR_SHIFT_START_WITH <= hour && hour <= HOUR_SHIFT_END_WITH) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }

        return calendar.getTime();
    }
}
