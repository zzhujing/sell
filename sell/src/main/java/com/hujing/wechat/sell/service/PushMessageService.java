package com.hujing.wechat.sell.service;

import com.hujing.wechat.sell.dto.OrderDTO;

/**
 * @author hj
 * @time 2019-03-02 15:10
 * @description 模板消息推送业务接口
 */
public interface PushMessageService {

    void pushMessage(OrderDTO orderDTO);
}
