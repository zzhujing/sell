package com.hujing.wechat.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hj
 * @time 2019-03-01 23:25
 * @description 微信支付平台配置
 */
@Configuration
@EnableConfigurationProperties(WechatProperties.class)
public class WechatOpenConfig {

    @Bean
    public WxMpService wxOpenMpService(WxMpConfigStorage wxOpenMpConfigStorage){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxOpenMpConfigStorage);
        return wxMpService;
    }

    @Bean
    public WxMpConfigStorage wxOpenMpConfigStorage(WechatProperties wechatProperties){
        WxMpInMemoryConfigStorage storage = new WxMpInMemoryConfigStorage();
        storage.setAppId(wechatProperties.getAppOpenId());
        storage.setSecret(wechatProperties.getOpenSecret());
        return storage;
    }
}
