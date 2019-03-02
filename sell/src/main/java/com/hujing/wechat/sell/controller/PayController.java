package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.service.OrderService;
import com.hujing.wechat.sell.service.PayService;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author hj
 * @time 2019-02-25 15:33
 * @description 支付控制层
 */
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private PayService payService;

    /**
     * 创建支付预付款
     *
     * @param orderId   订单id
     * @param returnUrl 付款成功后跳转的url
     * @return
     */
    @GetMapping("/create")
    public ModelAndView create(@RequestParam String orderId,
                               @RequestParam String returnUrl,
                               Map<String, Object> map) {
        OrderDTO orderDTO = orderService.findByOrderId(orderId);
        if (orderDTO == null) {
            log.error("【创建支付预付款】查询订单  orderDTO为null");
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }
        PayResponse response = payService.create(orderDTO);
        map.put("response", response);
        System.out.println(response);
        map.put("returnUrl", returnUrl);
        return new ModelAndView("pay/create", map);
    }



    @PostMapping("/notify")
    public ModelAndView notify(@RequestBody String notifyData) {
        payService.notify(notifyData);
        return new ModelAndView("pay/success");
    }


}
