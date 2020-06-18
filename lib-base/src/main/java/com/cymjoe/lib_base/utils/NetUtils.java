package com.cymjoe.lib_base.utils;

import com.cymjoe.lib_base.constant.Constant;
import com.cymjoe.lib_http.NetManager;


import java.util.HashMap;
import java.util.Map;

public class NetUtils {

    public static <T> T getService(String url, Class<T> service) {
        NetManager instance = NetManager.INSTANCE;
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json; charset=UTF-8");
        return instance.Builder()
                .baseUrl(url)
                .setHeader(map)
                .client()
                .build()
                .getService(service);
    }

    public static NetManager getService(String url) {
        NetManager instance = NetManager.INSTANCE;
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json; charset=UTF-8");
        return instance.Builder()
                .baseUrl(url)
                .client()
                .setHeader(map)
                .build()
                ;
    }
}
