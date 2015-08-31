package com.gomeltrans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by yahor on 25.08.15.
 */
public class Transport extends BaseBean {
    public static final String TABLE = "transport";
    public static final String ID = "id";
    public static final String NUMBER = "number_name";
    public static final String ROUTE = "route_name";
    public static final String TYPE = "type";
    public static final String FAVOURITE = "favourite";
    public static final String ACTIVE = "active";

    public enum TRANSPORT_TYPE {
        BUS(0), TROLLEY(1);
        private int code;

        TRANSPORT_TYPE(int num) {
            code = num;
        }

        public int getCode() {
            return code;
        }
    }
    @Expose
    @SerializedName("number")
    private String numberName;
    @Expose
    @SerializedName("routeName")
    private String routeName;
    @Expose
    private int typeNumber;
    @Expose
    private List<Stop> stopsForward;
    @Expose
    private List<Stop> stopsBackward;
    private boolean favourite;
    private boolean active;

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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getTypeNumber() {
        return typeNumber;
    }

    public void setTypeNumber(int typeNumber) {
        this.typeNumber = typeNumber;
    }
}
