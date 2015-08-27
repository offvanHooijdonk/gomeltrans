package com.gomeltrans.model.gson;

import com.gomeltrans.model.Stop;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class StopFieldDeserializer implements JsonDeserializer<Stop> {
    @Override
    public Stop deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Stop stop = new Stop();
        stop.setId(json.getAsLong());
        return stop;
    }
}
