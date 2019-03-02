package com.hujing.wechat.sell.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author hj
 * @time 2019-02-23 21:45
 * @description 操作数据的DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDTO {

    private String productId;
    private Integer productQuantity;
}
