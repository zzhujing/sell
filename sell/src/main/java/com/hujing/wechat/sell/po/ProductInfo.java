package com.hujing.wechat.sell.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hujing.wechat.sell.enums.ProdStateEnum;
import com.hujing.wechat.sell.util.DateSerializer;
import com.hujing.wechat.sell.util.EnumUtil;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商品
 * Created by 廖师兄
 * 2017-05-09 11:30
 */
@Entity
@Data
public class ProductInfo {

    @Id
    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private BigDecimal productPrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    private Integer productStatus;

    /** 类目编号. */
    private Integer categoryType;

    /** 创建时间. */
    @JsonSerialize(using = DateSerializer.class)
    private Date createTime;

    /** 更新时间. */
    @JsonSerialize(using = DateSerializer.class)
    private Date updateTime;

    @JsonIgnore
    public ProdStateEnum getProdStateEnum(){
        return EnumUtil.getEnumByCode(productStatus, ProdStateEnum.class);
    }
}
