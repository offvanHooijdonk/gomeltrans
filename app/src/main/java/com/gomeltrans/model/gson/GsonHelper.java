package com.gomeltrans.model.gson;

import com.gomeltrans.model.Stop;
import com.gomeltrans.model.Transport;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class GsonHelper {
    public static Gson getTransportGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls().serializeNulls().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Stop.class, new StopFieldDeserializer());
        return gsonBuilder.create();
    }

    public static Gson getStopsGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls().serializeNulls().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(Transport.class, new TransportFieldDeserializer());
        return gsonBuilder.create();
    }
}
