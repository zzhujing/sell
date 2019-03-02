package com.hujing.wechat.sell.service;

import com.hujing.wechat.sell.po.SellerInfo;

import java.util.Optional;

/**
 * @author hj
 * @time 2019-03-01 22:33
 * @description 卖家操作service
 */
public interface SellerInfoService {

    /**
     * 添加一个卖家
     * @param info
     * @return
     */
    Optional<SellerInfo> save(SellerInfo info);

    /**
     * 根据openid查询卖家信息
     * @param openid
     * @return
     */
    Optional<SellerInfo> findByOpenid(String openid);

}
