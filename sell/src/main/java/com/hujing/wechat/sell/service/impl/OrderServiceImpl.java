package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.dao.OrderDetailRepository;
import com.hujing.wechat.sell.dao.OrderMasterRepository;
import com.hujing.wechat.sell.dto.CartDTO;
import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.OrderStatusEnum;
import com.hujing.wechat.sell.enums.PayStatusEnum;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import com.hujing.wechat.sell.po.OrderDetail;
import com.hujing.wechat.sell.po.OrderMaster;
import com.hujing.wechat.sell.po.ProductInfo;
import com.hujing.wechat.sell.service.OrderService;
import com.hujing.wechat.sell.service.PayService;
import com.hujing.wechat.sell.service.ProductService;
import com.hujing.wechat.sell.service.PushMessageService;
import com.hujing.wechat.sell.util.KeyGenerator;
import com.hujing.wechat.sell.util.OrderMaster2OrderDTOConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author hj
 * @time 2019-02-23 20:02
 * @description 订单接口实现类
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMasterRepository masterRepository;

    @Autowired
    OrderDetailRepository detailRepository;

    @Autowired
    OrderServiceImpl orderService;

    @Autowired
    ProductService productService;

    @Autowired
    PushMessageService pushMessageService;

    @Autowired
    private PayService payService;

    @Autowired
    WebSocket webSocket;

    /**
     * 创建订单
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        BigDecimal orderAmount = new BigDecimal(0);
        //生成订单id
        String orderId = KeyGenerator.generate();
        //1.查询商品信息（主要是单价，不用让前端传递过来）
        for (OrderDetail orderDetail : orderDetailList) {
            String detailId = KeyGenerator.generate();
            ProductInfo info = productService.findOne(orderDetail.getProductId());
            if (info == null) {
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            //2.计算总价
            orderAmount = orderAmount.add(info.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity())));
            orderDetail.setDetailId(detailId);
            orderDetail.setOrderId(orderId);
            BeanUtils.copyProperties(info, orderDetail);
            detailRepository.save(orderDetail);
        }
        //3.填充到数据库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        orderDTO.setOrderAmount(orderAmount);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        masterRepository.save(orderMaster);
        //4.减库存
        List<CartDTO> cartDTOList = orderDetailList.stream().map(orderDetail ->
                CartDTO.builder().productId(orderDetail.getProductId()).productQuantity(orderDetail.getProductQuantity()).build()
        ).collect(toList());
        productService.decrStock(cartDTOList);

        //5.webSocket通知买家后端
        webSocket.sendMsg("你有一个新订单~");
        return orderDTO;
    }

    /**
     * 根据orderId查询order所有信息
     *
     * @param orderId
     * @return
     */
    @Override
    public OrderDTO findByOrderId(String orderId) {
        OrderMaster orderMaster = masterRepository.findById(orderId)
                .orElseThrow(() -> new SellException(ResultEnum.ORDER_NOT_EXIST));
        List<OrderDetail> detailList = detailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(detailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(detailList);
        return orderDTO;
    }

    /**
     * 分页查询所有的订单列表
     *
     * @param buyerOpenId
     * @param pageable
     * @return
     */
    @Override
    public Page<OrderDTO> findOrderList(String buyerOpenId, Pageable pageable) {
        Page<OrderMaster> masterList = masterRepository.findByBuyerOpenid(buyerOpenId, pageable);
        return new PageImpl<>(OrderMaster2OrderDTOConverter.convert(masterList.getContent()),pageable,masterList.getTotalElements());
    }

    /**
     * 查询所有订单
     * @param pageable
     * @return
     */
    @Override
    public Page<OrderDTO> findOrderList(Pageable pageable) {
        Page<OrderMaster> masterList = masterRepository.findAll(pageable);
        return new PageImpl<>(OrderMaster2OrderDTOConverter.convert(masterList.getContent()),pageable,masterList.getTotalElements());
    }

    /**
     * 取消订单
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态是否是new
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        BeanUtils.copyProperties(orderDTO, orderMaster);
        //更改订单状态
        updateOrderAndPayStatus(orderMaster, OrderStatusEnum.CANCEL.getCode(), null);

        List<OrderDetail> detailList = orderDTO.getOrderDetailList();
        if (CollectionUtils.isEmpty(detailList)) {
            throw new SellException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }
        //2.添加库存
        List<CartDTO> cartDTOList = detailList.stream().map(
                detail -> new CartDTO(detail.getProductId(), detail.getProductQuantity())
        ).collect(toList());
        productService.incrStock(cartDTOList);

        //如果已经支付还要进行退款
        if (!orderMaster.getPayStatus().equals(PayStatusEnum.SUCCESS.getCode())) {
            //退款
            payService.refund(orderDTO);
        }
        return orderDTO;
    }

    /**
     * 订单完成
     *
     * @param orderDTO
     * @return
     */
    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        //判断订单状态是否是new
        if (!orderMaster.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        //修改订单状态和支付状态
        orderService.updateOrderAndPayStatus(orderMaster, OrderStatusEnum.FINISHED.getCode(), PayStatusEnum.SUCCESS.getCode());

        //发送模板消息
        pushMessageService.pushMessage(orderDTO);
        return orderDTO;
    }

    /**
     * 支付完成
     *
     * @param orderDTO
     */
    @Override
    @Transactional
    public OrderDTO pay(OrderDTO orderDTO) {
        OrderMaster orderMaster = new OrderMaster();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【订单支付完成】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //判断支付状态
        if (!orderDTO.getPayStatus().equals(PayStatusEnum.WAIT.getCode())) {
            log.error("【订单支付完成】订单支付状态不正确, orderDTO={}", orderDTO);
            throw new SellException(ResultEnum.ORDER_STATUS_ERROR);
        }
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderService.updateOrderAndPayStatus(orderMaster, null, PayStatusEnum.SUCCESS.getCode());
        return orderDTO;
    }

    /**
     * 修改订单和支付状态
     *
     * @param orderMaster     订单号
     * @param orderStatusCode 订单状态码
     * @param payStatusCode   支付状态码
     */
    public void updateOrderAndPayStatus(OrderMaster orderMaster, Integer orderStatusCode, Integer payStatusCode) {
        if (orderStatusCode != null) {
            orderMaster.setOrderStatus(orderStatusCode);
        }
        if (payStatusCode != null) {
            orderMaster.setPayStatus(payStatusCode);
        }
        masterRepository.save(orderMaster);
    }
}
