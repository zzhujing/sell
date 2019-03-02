package com.hujing.wechat.sell.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hj
 * @time 2019-03-02 0:07
 * @description 工程url配置属性
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@ConfigurationProperties("project.url")
public class ProjectUrlProperties {

    /**
     * 微信公众平台授权url
     */
    public String wechatMpAuthorize;

    /**
     * 微信开放平台授权url
     */
    public String wechatOpenAuthorize;

    /**
     * 开发平台登录返回url
     */
    private String wechatOpenReturnUrl;

    /**
     * 点餐系统
     */
    public String sell;

}
