package com.sam_chordas.android.stockhawk.service;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sam_chordas.android.stockhawk.data.Stock;

import java.lang.reflect.Type;
import java.util.List;

public class StocksDeserializer implements JsonDeserializer<List<Stock>> {

    @Override
    public List<Stock> deserialize(JsonElement json, Type listType, JsonDeserializationContext context)
            throws JsonParseException {

        return new Gson().fromJson(

                json
                        .getAsJsonObject()
                        .get("query")
                        .getAsJsonObject()
                        .get("results")
                        .getAsJsonObject()
                        .get("quote").getAsJsonArray(), listType);

    }
}
