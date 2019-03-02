package com.hujing.wechat.sell.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author hj
 * @time 2019-03-02 15:20
 * @description 模板消息enum
 */
@Getter
@AllArgsConstructor
public enum TemplateMessageEnum {

    ORDER_STATUS("orderStatus", "订单状态模板"),
    ;
    private String templateKey;
    private String msg;

}