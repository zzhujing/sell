package com.hujing.wechat.sell.service.impl;

import com.hujing.wechat.sell.dao.SellerInfoRepository;
import com.hujing.wechat.sell.po.SellerInfo;
import com.hujing.wechat.sell.service.SellerInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author hj
 * @time 2019-03-01 22:35
 * @description 卖家信息实现类
 */
@Service
public class SellerInfoServiceImpl implements SellerInfoService {


    @Autowired
    SellerInfoRepository repository;

    /**
     * 添加卖家信息
     * @param info
     * @return
     */
    @Override
    public Optional<SellerInfo> save(SellerInfo info) {
        SellerInfo sellerInfo = repository.save(info);
        return Optional.ofNullable(sellerInfo);
    }

    /**
     * 根据openid查询卖家信息
     * @param openid
     * @return
     */
    @Override
    public Optional<SellerInfo> findByOpenid(String openid) {
        return  repository.findByOpenid(openid);
    }
}
