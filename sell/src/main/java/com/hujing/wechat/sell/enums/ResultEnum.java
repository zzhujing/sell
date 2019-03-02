package com.hujing.wechat.sell.enums;

import lombok.Getter;

/**
 * @author hj
 * @time 2019-02-23 20:54
 * @description 结果枚举
 */
@Getter
public enum ResultEnum {

    PRODUCT_NOT_EXIST(10, "商品不存在"),
    STOCK_NOT_ENOUGH(20, "库存不足"),
    ORDER_NOT_EXIST(30,"订单不存在"),
    ORDERDETAIL_NOT_EXIST(40,"订单详情不存在"),
    ORDER_STATUS_ERROR(50,"订单状态异常"),
    PARAM_ERROR(60,"类型转化错误"),
    OPENID_ORDERID_NOT_BELONG_ONE(70,"openid和订单id对应不上"),
    AUTH_ERROR(80,"授权失败"),
    CREATE_PAY_ERROR(90,"支付创建失败"),
    ORDER_CANCEL_SUCCESS(100,"订单取消成功"),
    PARAM_INVALID_ERROR(110,"表单参数校验错误"),
    PROD_STATUS_ERROR(110,"商品状态异常"),
    CHARSET_CONVERT_ERROR(120,"编码转化异常"),
    LOGIN_FAIL(130,"登录异常"),
    ;

    private Integer code;
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
