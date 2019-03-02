package com.hujing.wechat.sell.dao;

import com.hujing.wechat.sell.po.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author hj
 * @time 2019-03-01 22:25
 * @description
 */
public interface SellerInfoRepository extends JpaRepository<SellerInfo,String> {
    Optional<SellerInfo> findByOpenid(String openid);
}
