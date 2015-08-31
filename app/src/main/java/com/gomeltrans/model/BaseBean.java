package com.gomeltrans.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Yahor_Fralou on 8/28/2015.
 */
public class BaseBean {
    @Expose
    private Long id;

    public BaseBean() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
