package com.hujing.wechat.sell.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.impl.WxPayServiceImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hj
 * @time 2019-02-25 16:27
 * @description 微信支付配置类
 */
@Configuration
public class WechatPayConfig {


    /**
     * 配置访问微信支付的service
     * @param wxPayH5Config
     * @return
     */
    @Bean
    public WxPayServiceImpl wxPayService(WxPayH5Config wxPayH5Config){
        WxPayServiceImpl service = new WxPayServiceImpl();
        service.setWxPayH5Config(wxPayH5Config);
        return service;
    }

    /**
     * 配置h5支付方式的商户属性
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "pay.info")
    public WxPayH5Config wxPayH5Config(){
        return  new WxPayH5Config();
    }
}
