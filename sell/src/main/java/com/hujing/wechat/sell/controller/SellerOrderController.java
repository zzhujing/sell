package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.service.OrderService;
import com.hujing.wechat.sell.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author hj
 * @time 2019-02-28 15:11
 * @description 卖家控制层
 */
@Controller
@RequestMapping("/seller/order")
@Slf4j
public class SellerOrderController {


    @Autowired
    OrderService orderService;

    @GetMapping("/list")
    public ModelAndView getOrderList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                     @RequestParam(required = false, defaultValue = "2") Integer size,

                                     Map<String, Object> model) {
        Page<OrderDTO> orderPage = orderService.findOrderList(PageRequest.of(page - 1, size));
        model.put("orderPage", orderPage);
        model.put("currentPage", page);
        model.put("size", size);
        return new ModelAndView("order/list", model);
    }

    /**
     * 根据orderId取消订单
     *
     * @param orderId
     * @return
     */
    @GetMapping("/cancel")
    public String cancelOrder(@RequestParam("orderId") String orderId, Model model) {
        OrderDTO orderDTO = null;
        try {
            orderDTO = orderService.findByOrderId(orderId);
            orderService.cancel(orderDTO);
            log.info("【订单取消】 成功，orderDTO={}", JsonUtil.toJson(orderDTO));
            model.addAttribute("msg", ResultEnum.ORDER_CANCEL_SUCCESS.getMsg());
            model.addAttribute("returnUrl", "/sell/seller/order/list");
            return "common/success";
        } catch (SellException e) {
            log.error("【订单取消】 失败，orderId={}", orderId);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("returnUrl", "/sell/seller/order/list");
            return "common/error";
        }
    }

    /**
     * 根据id获取订单详细信息
     * @param orderId
     * @param model
     * @return
     */
    @GetMapping("/detail")
    public String getOrderDetail(@RequestParam("orderId") String orderId, Model model) {

        OrderDTO orderDTO = null;
        try {
            orderDTO = orderService.findByOrderId(orderId);
            log.info("【获取订单详情】 成功，orderDetail={}", JsonUtil.toJson(orderDTO.getOrderDetailList()));
            model.addAttribute("orderDTO", orderDTO);
            return "order/detail";
        } catch (SellException e) {
            log.error("【获取订单失败】 失败，orderId={}", orderId);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("returnUrl", "/sell/seller/order/list");
            return "common/error";
        }
    }

    /**
     * 完成订单
     * @param orderId 订单id
     * @param model 模型数据对象
     * @return 视图名
     */
    @GetMapping("/finish")
    public String finishOrder(@RequestParam String orderId,Model model) {
        OrderDTO orderDTO = null;
        try {
            orderDTO = orderService.findByOrderId(orderId);
            orderService.finish(orderDTO);
            log.info("【完成订单】 成功，orderDetail={}", JsonUtil.toJson(orderDTO.getOrderDetailList()));
            model.addAttribute("msg", "订单完成！！");
            model.addAttribute("returnUrl", "/sell/seller/order/list");
            return "common/success";
        } catch (SellException e) {
            log.error("【完成订单】 失败，orderId={}", orderId);
            model.addAttribute("msg", e.getMessage());
            model.addAttribute("returnUrl", "/sell/seller/order/list");
            return "common/error";
        }
    }

}
