package com.hujing.wechat.sell.util;

import java.util.UUID;

/**
 * @author hj
 * @time 2019-02-23 20:24
 * @description id生成器
 */
public class KeyGenerator {

    public static String generate(){
        String uuid = UUID.randomUUID().toString().substring(0, 15);
        String timeStr = (System.currentTimeMillis() + "").substring(5, 8);
        return new StringBuilder(uuid).append(timeStr).toString();
    }
}
