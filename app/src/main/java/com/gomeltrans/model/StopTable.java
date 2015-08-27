package com.gomeltrans.model;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class StopTable {
    public static final String TABLE = "stop_table";
    public static final String ID = "id";
    public static final String TRANSPORT_STOP_ID = "transport_stop_id";
    public static final String TIME = "time";
    public static final String ACTIVE = "active";

    private Transport transport;
    private List<String> time;

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public class TimeComparator implements Comparator<String> {
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
    }
}
