package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.service.OrderService;
import com.hujing.wechat.sell.service.PayService;
import com.hujing.wechat.sell.util.JsonUtil;
import com.hujing.wechat.sell.util.MathUtil;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.model.RefundRequest;
import com.lly835.bestpay.model.RefundResponse;
import com.lly835.bestpay.service.impl.WxPayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hj
 * @time 2019-02-25 15:40
 * @description 支付接口实现类
 */
@Service
@Slf4j
public class PayServiceImpl implements PayService {

    @Autowired
    private WxPayServiceImpl wxPayService;

    @Autowired
    OrderService orderService;

    public static final String ORDER_NAME = "微信点餐订单";

    /**
     * 创建预支付订单
     *
     * @param orderDTO
     * @return
     */
    @Override
    public PayResponse create(OrderDTO orderDTO) {
        PayRequest request = new PayRequest();
        try {
            request.setOpenid(orderDTO.getBuyerOpenid());
            request.setOrderName(ORDER_NAME);
            request.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
            request.setOrderId(orderDTO.getOrderId());
            request.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
            PayResponse response = wxPayService.pay(request);
            log.info("【创建预支付订单】 发起支付 response={}",JsonUtil.toJson(response));
            return response;
        } catch (Exception e) {
            log.error("【创建预支付订单】 失败 orderId={},e={}", orderDTO.getOrderId(),e.getMessage());
            throw new SellException(ResultEnum.CREATE_PAY_ERROR);
        }
    }

    /**
     * 回调方法
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse notify(String notifyData) {
        PayResponse payResponse = wxPayService.asyncNotify(notifyData);
        log.info("【收到回调消息】 payResponse={}", JsonUtil.toJson(notifyData));
        //1.查询订单是否存在
        OrderDTO orderDTO = orderService.findByOrderId(payResponse.getOrderId());

        if (orderDTO == null) {
            log.error("【notify回调方法】 订单不存在 orderId={}", payResponse.getOrderId());
        }
        //2.校验金额是否一致
        if (!MathUtil.comparTo(payResponse.getOrderAmount(),orderDTO.getOrderAmount().doubleValue())){
            log.error("【notify回调方法】 金额不一致 orderId={},回调金额={},数据库金额={}",
                    payResponse.getOrderId(),
                    payResponse.getOrderAmount(),
                    orderDTO.getOrderAmount().doubleValue());
        }
        //3.修改状态
        orderService.pay(orderDTO);
        return payResponse;
    }


    /**
     * 退款
     * @param orderDTO
     */
    @Override
    public RefundResponse refund(OrderDTO orderDTO) {
        RefundRequest request = new RefundRequest();
        request.setOrderId(orderDTO.getOrderId());
        request.setOrderAmount(orderDTO.getOrderAmount().doubleValue());
        request.setPayTypeEnum(BestPayTypeEnum.WXPAY_H5);
        log.info("【refund】 发起退款 request={}",JsonUtil.toJson(request));
        RefundResponse response = wxPayService.refund(request);
        log.info("【refund】 发起退款 response={}",JsonUtil.toJson(response));
        return response;
    }
}
