package com.gomeltrans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class Stop extends BaseBean {
    public static final String TABLE = "stops";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String COMMENT = "comment";
    public static final String FAVOURITE = "favourite";
    public static final String ACTIVE = "active";
    @Expose
    private String name;
    @Expose
    private String comment;
    @Expose
    @SerializedName("table")
    // TODO implement tables for working days and weekends
    private List<StopTable> stopTables;
    private boolean favourite;
    private boolean active;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StopTable> getStopTables() {
        return stopTables;
    }

    public void setStopTables(List<StopTable> stopTables) {
        this.stopTables = stopTables;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
