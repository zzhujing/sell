package com.hujing.wechat.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hj
 * @time 2019-02-23 15:33
 * @description 商品信息vo
 */
@Data
public class ProdInfoVo {

    @JsonProperty("id")
    private String productId;

    /** 名字. */
    @JsonProperty("name")
    private String productName;

    /** 单价. */
    @JsonProperty("price")
    private BigDecimal productPrice;


    /** 描述. */
    @JsonProperty("description")
    private String productDescription;

    /** 小图. */
    @JsonProperty("icon")
    private String productIcon;

}
