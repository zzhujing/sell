package com.hujing.wechat.sell.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hujing.wechat.sell.enums.OrderStatusEnum;
import com.hujing.wechat.sell.enums.PayStatusEnum;
import com.hujing.wechat.sell.util.DateSerializer;
import com.hujing.wechat.sell.util.EnumUtil;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 廖师兄
 * 2017-06-11 17:08
 */
@Entity
@Data
@DynamicUpdate
public class OrderMaster {

    /** 订单id. */
    @Id
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus = OrderStatusEnum.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayStatusEnum.WAIT.getCode();

    /** 创建时间. */
    @JsonSerialize(using = DateSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = DateSerializer.class)
    private Date updateTime;


    @JsonIgnore //json序列化的时候不讲该属性序列化
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getEnumByCode(orderStatus, OrderStatusEnum.class);
    }

    @JsonIgnore
    public PayStatusEnum getPayStatusEnum() {
        return EnumUtil.getEnumByCode(payStatus, PayStatusEnum.class);
    }

}
