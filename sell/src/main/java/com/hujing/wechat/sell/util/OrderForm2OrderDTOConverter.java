package com.hujing.wechat.sell.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.form.OrderForm;
import com.hujing.wechat.sell.po.OrderDetail;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author hj
 * @time 2019-02-24 14:25
 * @description 类型转换器
 */
@Slf4j
public class OrderForm2OrderDTOConverter {

    public static OrderDTO conver(OrderForm orderForm) {
        Gson gson = new Gson();
        OrderDTO orderDTO = new OrderDTO();
        //1.将orderForm中的属性映射到orderDTO中
        orderDTO.setBuyerName(orderForm.getName());
        orderDTO.setBuyerAddress(orderForm.getAddress());
        orderDTO.setBuyerOpenid(orderForm.getOpenid());
        orderDTO.setBuyerPhone(orderForm.getPhone());

        List<OrderDetail> detailList = null;

        try {
            detailList = gson.fromJson(orderForm.getItems(), new TypeToken<List<OrderDetail>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            log.error("【类型转化失败 ：传入的json{}】",orderForm.getItems());
            throw new SellException(ResultEnum.PARAM_ERROR);
        }

        orderDTO.setOrderDetailList(detailList);
        return orderDTO;
    }
}
