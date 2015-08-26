package com.gomeltrans.model;

import java.util.List;

/**
 * Created by yahor on 25.08.15.
 */
public class Transport {
    public static final String TABLE = "transport";
    public static final String ID = "id";
    public static final String NUMBER = "number_name";
    public static final String ROUTE = "route_name";
    public static final String TYPE = "type";
    public static final String FAVOURITE = "favourite";
    public static final String ACTIVE = "active";

    public enum TYPE {
        BUS(0), TROLLEY(1);
        private int code;

        TYPE(int num) {
            code = num;
        }

        public int getCode() {
            return code;
        }
    }

    private Long id;
    private String numberName;
    private String routeName;
    private TYPE type;
    private List<Stop> stopsForward;
    private List<Stop> stopsBackward;
    private boolean favourite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberName() {
        return numberName;
    }

    public void setNumberName(String numberName) {
        this.numberName = numberName;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public List<Stop> getStopsForward() {
        return stopsForward;
    }

    public void setStopsForward(List<Stop> stopsForward) {
        this.stopsForward = stopsForward;
    }

    public List<Stop> getStopsBackward() {
        return stopsBackward;
    }

    public void setStopsBackward(List<Stop> stopsBackward) {
        this.stopsBackward = stopsBackward;
    }
}
