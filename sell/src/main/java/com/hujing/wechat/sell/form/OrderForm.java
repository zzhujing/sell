package com.hujing.wechat.sell.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author hj
 * @time 2019-02-24 14:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderForm {

    /**
     * name : 张三
     * phone : 18868822111
     * address : 慕课网总部
     * openid : ew3euwhd7sjw9diwkq
     * items : [{"productId":"1423113435324","productQuantity":2}]
     */
    @NotEmpty(message = "姓名必填")
    private String name;

    @NotEmpty(message = "手机必填")
    private String phone;

    @NotEmpty(message = "地址必填")
    private String address;

    @NotEmpty(message = "opendid必填")
    private String openid;

    @NotEmpty(message = "购物车不能为空")
    private String items;

}
