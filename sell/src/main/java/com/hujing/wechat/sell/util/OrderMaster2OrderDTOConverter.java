package com.hujing.wechat.sell.util;

import com.hujing.wechat.sell.dao.OrderDetailRepository;
import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.po.OrderMaster;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hj
 * @time 2019-02-28 14:36
 * @description
 */
public class OrderMaster2OrderDTOConverter {

    @Autowired
    OrderDetailRepository detailRepository;

    public static OrderDTO convert(OrderMaster orderMaster){
        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    public static List<OrderDTO> convert(List<OrderMaster> orderMasterList) {
        return orderMasterList.stream().map(OrderMaster2OrderDTOConverter::convert).collect(Collectors.toList());
    }

}
