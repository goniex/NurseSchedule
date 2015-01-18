package com.nurseschedule.mvc.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

/**
 * @author Tomasz Morek
 */
public class RequestUtil {

    /**
     * Convert Request param to Object
     * @param requestObject
     * @param clazz
     * @return
     */
    public static Object convert(Object requestObject, Class clazz) {
        ObjectMapper mapper = new ObjectMapper();
        String json = new Gson().toJson(requestObject, Map.class);
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
