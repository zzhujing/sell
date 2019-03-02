package com.hujing.wechat.sell.exception;

import com.hujing.wechat.sell.config.ProjectUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author hj
 * @time 2019-03-02 14:48
 * @description 卖家后台全局异常处理类
 */
@ControllerAdvice
public class SellerExceptionHandler {

    @Autowired
    private ProjectUrlProperties projectUrlProperties;


    @ExceptionHandler(SellAuthorizeException.class)
    public String handler(){
        StringBuilder sb = new StringBuilder("redirect:");
        sb.append(projectUrlProperties.getWechatOpenAuthorize())
          .append("/sell/wechat/qrAuthorize");
        return sb.toString();
    }
}
