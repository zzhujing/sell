package com.hujing.wechat.sell.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author hj
 * @time 2019-02-25 0:19
 * @description 用于储存微信商户的基本信息
 */
@ConfigurationProperties(prefix = "wx.info")
@Data
public class WechatProperties {

    /**
     * appID
     */
    private String appID;

    /**
     * secret
     */
    private String secret;

    /**
     * 开放平台APPId
     */
    private String appOpenId;

    /**
     * 开放平台secret
     */
    private String openSecret;


    /**
     * 模板消息id
     */
    private Map<String,String> templateId;
}
