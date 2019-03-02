package com.hujing.wechat.sell.config;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author hj
 * @time 2019-02-25 0:39
 * @description 微信配置类
 */
@Configuration
@EnableConfigurationProperties(WechatProperties.class)
public class WechatConfig implements WebMvcConfigurer {


    /**
     * 向IOC容器中注册操作微信的bean
     * @param wxMpConfigStorage
     * @return
     */
    @Bean
    public WxMpService wxMpService(WxMpConfigStorage wxMpConfigStorage){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage);
        return wxMpService;
    }

    /**
     * 向容器中注册微信商户的配置信息bean
     * @return
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage(WechatProperties wechatProperties){
        WxMpInMemoryConfigStorage storage = new WxMpInMemoryConfigStorage();
        storage.setAppId(wechatProperties.getAppID());
        storage.setSecret(wechatProperties.getSecret());
        return storage;
    }
}
