package com.gomeltrans.model;

/**
 * Created by yahor on 25.08.15.
 */
public class Bus {
    private Long id;
    private String numberName;
    private String routeName;

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
}
