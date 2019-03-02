package com.hujing.wechat.sell.util;

import com.hujing.wechat.sell.enums.BaseEnum;

/**
 * @author hj
 * @time 2019-02-28 16:03
 * @description 枚举工具类
 */
public class EnumUtil{

    /**
     * 通用的使用code获取枚举的方法
     * @param code code
     * @param clazz 枚举class对象
     * @param <T> 返回的类型
     * @return 返回的枚举
     */
    public static <T extends BaseEnum> T getEnumByCode(Integer code, Class<T> clazz) {
        for (T constants : clazz.getEnumConstants()) {
            if (constants.getCode().equals(code)) {
                return constants;
            }
        }
        return null;
    }
}
