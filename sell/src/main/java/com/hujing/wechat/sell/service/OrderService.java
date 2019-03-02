package com.hujing.wechat.sell.service;

import com.hujing.wechat.sell.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/**
 * @author hj
 * @time 2019-02-23 19:56
 * @description 订单业务层接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    OrderDTO createOrder(OrderDTO orderDTO);

    /**
     * 查询一个订单
     */
    OrderDTO findByOrderId(String orderId);

    /**
     * 根据openid查询所有订单列表
     */
    Page<OrderDTO> findOrderList(String buyerOpenId, Pageable pageable);

    /**
     * 查询所有订单
     * @param pageable
     * @return
     */
    Page<OrderDTO> findOrderList(Pageable pageable);

    /**
     * 取消订单
     */
    OrderDTO cancel(OrderDTO orderDTO);

    /**
     * 完结订单
     * @param orderDTO
     * @return
     */
    OrderDTO finish(OrderDTO orderDTO);

    /**
     * 支付订单
     * @param orderDTO
     * @return
     */
    OrderDTO pay(OrderDTO orderDTO);


}
