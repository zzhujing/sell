package com.hujing.wechat.sell.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-23 15:32
 * @description 商品详情vo（包括类目信息和商品信息）
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProdDetailVo {

    /*=======================商品类目信息===========================*/
    /** 类目名字. */
    @JsonProperty("name")
    private String categoryName;

    /** 类目编号. */
    @JsonProperty("type")
    private Integer categoryType;

    /*==================商品信息=============================*/
    private List<ProdInfoVo> foods;

}
