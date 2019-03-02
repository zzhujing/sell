package com.hujing.wechat.sell.exception;

import com.hujing.wechat.sell.enums.ResultEnum;

/**
 * @author hj
 * @time 2019-02-23 20:53
 * @description
 */
public class SellException extends RuntimeException {


    private Integer code;

    public SellException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public SellException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = code;
    }
}
