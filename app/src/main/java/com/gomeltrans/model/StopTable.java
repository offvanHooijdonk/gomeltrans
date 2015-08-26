package com.gomeltrans.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Yahor_Fralou on 8/26/2015.
 */
public class StopTable {
    private Transport transport;
    private List<Date> time;

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public List<Date> getTime() {
        return time;
    }

    public void setTime(List<Date> time) {
        this.time = time;
    }
}
