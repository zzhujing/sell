package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.form.OrderForm;
import com.hujing.wechat.sell.service.BuyerService;
import com.hujing.wechat.sell.service.OrderService;
import com.hujing.wechat.sell.util.OrderForm2OrderDTOConverter;
import com.hujing.wechat.sell.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hj
 * @time 2019-02-24 14:20
 * @description 买家订单controller
 */
@RestController
@RequestMapping("/buyer/order")
@Slf4j
public class BuyerOrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    BuyerService buyerService;

    /**
     * 创建订单
     *
     * @param orderForm     订单信息
     * @param bindingResult 处理错误信息
     * @return
     */
    @PostMapping("/create")
    public ResultVo<Map<String, String>> createOrder(@Valid OrderForm orderForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("createOrder error, errorMsg:{}", bindingResult.getFieldError().getDefaultMessage());
            throw new SellException(bindingResult.getFieldError().getDefaultMessage(), ResultEnum.PARAM_ERROR.getCode());
        }
        Map<String, String> rtMap = new HashMap<>();
        OrderDTO orderDTO = OrderForm2OrderDTOConverter.conver(orderForm);
        OrderDTO result = orderService.createOrder(orderDTO);
        rtMap.put("orderId", result.getOrderId());
        return ResultVo.success(rtMap);
    }

    /**
     * 查询订单列表
     *
     * @param openid 微信openid
     * @param page   当前页数
     * @param size   每页数量
     * @return
     */
    @GetMapping("/list")
    public ResultVo<List<OrderDTO>> queryOrderList(@RequestParam String openid,
                                                   @RequestParam(defaultValue = "0") Integer page,
                                                   @RequestParam(defaultValue = "10") Integer size) {
        Page<OrderDTO> orderDTOList = orderService.findOrderList(openid, PageRequest.of(page, size));
        return ResultVo.success(orderDTOList.getContent());
    }

    /**
     * 查询订单详情
     *
     * @param openid  买家微信openid
     * @param orderId 订单id
     * @return
     */
    @GetMapping("/detail")
    public ResultVo<OrderDTO> queryOrderDetail(@RequestParam String openid,
                                               @RequestParam String orderId) {
        //1.校验订单是否是该openid下的
        return ResultVo.success(buyerService.checkOrder(openid, orderId));
    }

    /**
     * 取消订单
     * @param openid
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    public ResultVo cancelOrder(@RequestParam String openid,
                                @RequestParam String orderId) {
        //1.校验订单是否是该openid下的
        OrderDTO orderDTO = buyerService.checkOrder(openid, orderId);
        //2.取消订单
        orderService.cancel(orderDTO);
        return ResultVo.success(null);
    }

}
