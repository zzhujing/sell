package com.hujing.wechat.sell.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author hj
 * @time 2019-02-25 17:06
 * @description
 */
public class JsonUtil {

    public static String toJson(Object obj) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        Gson gson = builder.create();
        return gson.toJson(obj);
    }
}
