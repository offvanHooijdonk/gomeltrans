package com.gomeltrans.model;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class TransportStops extends BaseBean {
    public static final String TABLE = "transport_stops";
    public static final String ID = "id";
    public static final String TRANSPORT_ID = "transport_шв";
    public static final String STOP_ID = "transport_stops";
    public static final String DIRECTION_INDEX = "direction";
    public static final String ORDER_NUMBER = "order_number";
    public static final String ACTIVE = "active";

    public enum DIRECTION {
        FORWARD(0), BACKWARD(1);
        private int code;

        DIRECTION(int num) {
            code = num;
        }

        public int getCode() {
            return code;
        }
    }
}
