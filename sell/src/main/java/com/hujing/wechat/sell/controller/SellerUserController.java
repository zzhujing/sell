package com.hujing.wechat.sell.controller;

import com.hujing.wechat.sell.config.ProjectUrlProperties;
import com.hujing.wechat.sell.constant.CookieConstant;
import com.hujing.wechat.sell.constant.RedisConstant;
import com.hujing.wechat.sell.enums.ResultEnum;
import com.hujing.wechat.sell.po.SellerInfo;
import com.hujing.wechat.sell.service.SellerInfoService;
import com.hujing.wechat.sell.util.CookieUtils;
import com.hujing.wechat.sell.util.KeyGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author hj
 * @time 2019-03-02 11:51
 * @description 卖家用户控制器
 */
@RequestMapping("/seller")
@Controller
@Slf4j
public class SellerUserController {

    @Autowired
    SellerInfoService sellerInfoService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    ProjectUrlProperties projectUrlProperties;

    /**
     * 登录
     *
     * @param openid
     * @return
     */
    @GetMapping("/login")
    public String login(@RequestParam String openid, Model model, HttpServletRequest request, HttpServletResponse response) {

        //1.根据openid查询是否有该数据
        Optional<SellerInfo> sellerInfo = sellerInfoService.findByOpenid(openid);
        if (!sellerInfo.isPresent()) {
            model.addAttribute("msg", ResultEnum.LOGIN_FAIL.getMsg());
            model.addAttribute("returnUrl", "/sell/wechat/qrAuthorize");
            return "common/error";
        }
        //2.添加到redis中
        String token = KeyGenerator.generate();
        Integer expire = RedisConstant.EXPIRE;
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX,token), openid, expire, TimeUnit.SECONDS);
        //3.设置到cookie中
        CookieUtils.setCookie(request, response, CookieConstant.COOKIE_NAME, token, expire, "utf-8");
        return "redirect:" + projectUrlProperties.getSell() + "/sell/seller/order/list";
    }


    /**
     * 登出
     * @param request
     * @param response
     * @param model
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,Model model) {
        String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_NAME, "utf-8");
        //1.清除redis
        redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookieValue));
        //2.清除cookie
        CookieUtils.deleteCookie(request,response,CookieConstant.COOKIE_NAME);
        return "redirect:" + projectUrlProperties.getSell() + "/sell/seller/order/list";
    }
}

