package com.hujing.wechat.sell.util;

/**
 * @author hj
 * @time 2019-02-25 17:15
 * @description 金额比较工具
 */
public class MathUtil {

    public static boolean comparTo(double b1, double b2) {
        if (b1-b2<0.01){
            return true;
        }
        return false;
    }
}
