package com.hujing.wechat.sell.dto;

import com.hujing.wechat.sell.po.OrderDetail;
import com.hujing.wechat.sell.po.OrderMaster;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.util.List;

/**
 * @author hj
 * @time 2019-02-23 19:57
 * @description
 */
@Data
@NoArgsConstructor
public class OrderDTO extends OrderMaster {
    @Transient
    private List<OrderDetail> orderDetailList;
}
