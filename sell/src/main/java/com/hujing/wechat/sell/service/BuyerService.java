package com.hujing.wechat.sell.service;

import com.hujing.wechat.sell.dto.OrderDTO;

/**
 * @author hj
 * @time 2019-02-24 20:54
 * @description 买家业务接口
 */
public interface BuyerService {

    OrderDTO checkOrder(String openid, String orderId);
}
