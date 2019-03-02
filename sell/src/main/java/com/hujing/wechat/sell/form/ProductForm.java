package com.hujing.wechat.sell.form;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author hj
 * @time 2019-02-28 21:58
 * @description
 */
@Data
public class ProductForm {


    private String productId;
    /**
     * 名字.
     */
//    @NotEmpty(message = "商品名字不能为空")
    private String productName;

    /**
     * 单价.
     */
//    @NotEmpty(message = "单价不能为空")
    private BigDecimal productPrice;

    /**
     * 库存.
     */
//    @NotEmpty(message = "库存不能为空")
    private Integer productStock;

    /**
     * 描述.
     */
//    @NotEmpty(message = "描述不能为空")
    private String productDescription;

    /**
     * 小图.
     */
//    @NotEmpty(message = "图片不能为空")
    private String productIcon;

    /**
     * 类目编号.
     */
//    @NotEmpty(message = "类目编号不能为空")
    private Integer categoryType;
}

