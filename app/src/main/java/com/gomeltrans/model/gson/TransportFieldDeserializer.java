package com.gomeltrans.model.gson;

import com.gomeltrans.model.Transport;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Yahor_Fralou on 8/27/2015.
 */
public class TransportFieldDeserializer implements JsonDeserializer<Transport> {
    @Override
    public Transport deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Transport transport = new Transport();
        transport.setId(json.getAsLong());
        return transport;
    }
}
