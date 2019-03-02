package com.hujing.wechat.sell.aspect;

import com.hujing.wechat.sell.constant.CookieConstant;
import com.hujing.wechat.sell.constant.RedisConstant;
import com.hujing.wechat.sell.exception.SellAuthorizeException;
import com.hujing.wechat.sell.util.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author hj
 * @time 2019-03-02 14:13
 * @description 用户切面类
 */
@Aspect
@Slf4j
@Component
public class SellerAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Pointcut("execution(public * com.hujing.wechat.sell.controller.Seller*.*(..)) && !execution(public * com.hujing.wechat.sell.controller.SellerUserController.*(..))")
    public void verify() {
    }


    @Before("verify()")
    public void before() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        //1.获取到cookie中的token，无则直接跳到错误页面
        String cookieValue = CookieUtils.getCookieValue(request, CookieConstant.COOKIE_NAME, "utf-8");
        if (StringUtils.isEmpty(cookieValue)) {
            log.error("【登录校验】 从cookie中获取token失败 cookieValue={}" ,cookieValue);
            throw new SellAuthorizeException();
        }
        //2.有拿到值去redis中查，有值的话则放行，并且重置其过期时间，没值说明登录过期了
        String tokenName = String.format(RedisConstant.TOKEN_PREFIX, cookieValue);
        String redisValue = redisTemplate.opsForValue().get(tokenName);
        if (StringUtils.isEmpty(redisValue)) {
            log.error("【登录校验】 从redis中获取token失败 redisValue={}" ,redisValue);
            throw new SellAuthorizeException();
        }

        //重新设置过期时间
        redisTemplate.opsForValue().set(tokenName,redisValue,RedisConstant.EXPIRE, TimeUnit.SECONDS);
        CookieUtils.setCookie(request, response, CookieConstant.COOKIE_NAME, cookieValue, CookieConstant.expire, "utf-8");

    }

}
