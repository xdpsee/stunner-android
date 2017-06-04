package com.cherry.stunner.view.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JSONUtils {

    private static final Gson gson;
    static {
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create();
    }

    public static <T> T fromJSONString(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }

    public static <T> T fromJSONString(String jsonStr, Type type) {
        return gson.fromJson(jsonStr, type);
    }

    public static<T> String toJSONString(T object) {
        return gson.toJson(object);
    }
}
