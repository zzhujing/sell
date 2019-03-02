package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.config.ProjectUrlProperties;
import com.hujing.wechat.sell.config.WechatProperties;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.exception.SellException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author hj
 * @time 2019-02-25 0:07
 * @description 微信操作控制层
 */
@Controller
@RequestMapping("/wechat")
@Slf4j
@SuppressWarnings("all")
public class WechatController {

    @Autowired
    WechatProperties prop;
    @Autowired
    WxMpService wxMpService;
    @Autowired
    WxMpService wxOpenMpService;
    @Autowired
    WechatProperties wechatProperties;
    @Autowired
    ProjectUrlProperties projectUrlProperties;
    /**
     * 发起网页授权
     *
     * @param returnUrl 授权完毕返回的url
     * @return
     */
    @GetMapping("/authorize")
    public String auth(@RequestParam("returnUrl") String returnUrl) {
        String url =projectUrlProperties.getWechatMpAuthorize()+"/sell/wechat/userInfo";
        //将returnUrl放到state参数位置，重定向会携带到获取accessToken方法
        String redirectUrl = null;
        try {
            redirectUrl = wxMpService.oauth2buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, URLEncoder.encode(returnUrl,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("【returnUrl编码格式转化错误】");
            throw new SellException(ResultEnum.CHARSET_CONVERT_ERROR);
        }
        log.info("【微信网页授权】获取code reidrectUrl: {}", redirectUrl);
        return "redirect:" + redirectUrl;
    }


    /**
     * 获取code，并且完成accessToken验证拿到openid并返回returnUrl?openid=xxx
     *
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        if (StringUtils.isEmpty(code)) {
            log.error("【获取code失败】 code ：{}", code);
        }
        WxMpOAuth2AccessToken accessToken = null;
        try {
            accessToken = wxMpService.oauth2getAccessToken(code);
            return "redirect:"+returnUrl+"?openid=" + accessToken.getOpenId();
        } catch (WxErrorException e) {
            log.error("【获取accessToken异常】 msg:{}", e.getError().getErrorMsg());
            throw new SellException(ResultEnum.AUTH_ERROR);
        }
    }


    /**
     * 公众平台授权
     * @param returnUrl 授权完毕返回的url
     * @return
     */
    @GetMapping("/qrAuthorize")
    public String qrAuthorize(/*@RequestParam("returnUrl") String returnUrl*/) {
        String url = projectUrlProperties.getWechatOpenAuthorize()+"/sell/wechat/qrUserInfo";
        //将returnUrl放到state参数位置，重定向会携带到获取accessToken方法
        String redirectUrl = null;
        try {
            redirectUrl = "https://open.weixin.qq.com/connect/qrconnect?appid="+wechatProperties.getAppOpenId()+"&redirect_uri=http%3A%2F%2Fsell.springboot.cn%2Fsell%2Fqr%2FoTgZpwfm0uWj7p4svKjlM94NxEAM&response_type=code&scope=snsapi_login&state="+ URLEncoder.encode(url,"utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("【url编码转化异常】");
            throw new SellException(ResultEnum.CHARSET_CONVERT_ERROR);
        }
        return "redirect:" + redirectUrl;
    }


    /**
     * 获取code，并且完成accessToken验证拿到openid并返回returnUrl?openid=xxx
     *
     * @param code
     * @param returnUrl
     * @return
     */
    @GetMapping("/qrUserInfo")
    public String qrUserInfo(@RequestParam("code") String code/*,
                           @RequestParam("state") String returnUrl*/) {
        if (StringUtils.isEmpty(code)) {
            log.error("【获取code失败】 code ：{}", code);
        }
        WxMpOAuth2AccessToken accessToken = null;
        try {
            accessToken = wxOpenMpService.oauth2getAccessToken(code);
            return "redirect:"+projectUrlProperties.getWechatOpenReturnUrl()+"?openid=" + accessToken.getOpenId();
        } catch (WxErrorException e) {
            log.error("【获取accessToken异常】 msg:{}", e.getError().getErrorMsg());
            throw new SellException(ResultEnum.AUTH_ERROR);
        }
    }

}
