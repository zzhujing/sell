package com.hujing.wechat.sell.enums;

import lombok.Getter;

/**
 * @author hj
 * @time 2019-02-23 14:50
 * @description 商品状态信息枚举类
 */
@Getter
public enum ProdStateEnum implements BaseEnum{
    UP(1,"上架"),

    DOWN(0,"下架");

    private Integer code;
    private String msg;

    ProdStateEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
