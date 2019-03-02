package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.config.WechatProperties;
import com.hujing.wechat.sell.dto.OrderDTO;
import com.hujing.wechat.sell.enums.TemplateMessageEnum;
import com.hujing.wechat.sell.service.OrderService;
import com.hujing.wechat.sell.service.PushMessageService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @author hj
 * @time 2019-03-02 15:11
 * @description 模板消息推送业务实现类
 */
@Service
@Slf4j
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    OrderService orderService;
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WechatProperties wechatProperties;
    @Override
    public void pushMessage(OrderDTO orderDTO) {

        WxMpTemplateMessage template = WxMpTemplateMessage
                .builder()
                .toUser(orderDTO.getBuyerOpenid())
                .templateId(wechatProperties.getTemplateId().get(TemplateMessageEnum.ORDER_STATUS.getTemplateKey()))
                .data(Arrays.asList(
                        new WxMpTemplateData("first","亲，你好~","red"),
                        new WxMpTemplateData("keyword1","胡競-微信点餐","red"),
                        new WxMpTemplateData("keyword2","12870886294","red"),
                        new WxMpTemplateData("keyword3",orderDTO.getOrderId(),"red"),
                        new WxMpTemplateData("keyword4",orderDTO.getOrderStatusEnum().getMessage(),"red"),
                        new WxMpTemplateData("keyword5","￥"+orderDTO.getOrderAmount(),"red"),
                        new WxMpTemplateData("remark","欢迎下次光临","red")))
                .build();

        try {
            wxMpService.getTemplateMsgService().sendTemplateMsg(template);
        } catch (WxErrorException e) {
            log.error("【模板消息发送失败】 template={}",template);
        }
    }
}
