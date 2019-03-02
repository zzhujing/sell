package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.service.BuyerService;
import com.hujing.wechat.sell.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hj
 * @time 2019-02-24 20:55
 * @description 买家业务层接口实现列
 */
@Service
public class BuyerServiceImpl implements BuyerService {
    @Autowired
    OrderService orderService;

    /**
     * 校验该订单是否是该微信openid的
     *
     * @param openid  微信openid
     * @param orderId 订单id
     * @return
     */
    @Override
    public OrderDTO checkOrder(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findByOrderId(orderId);
        if (orderDTO == null) {
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        if (!openid.equals(orderDTO.getBuyerOpenid())){
            throw new SellException(ResultEnum.OPENID_ORDERID_NOT_BELONG_ONE);
        }
        return orderDTO;
    }
}
