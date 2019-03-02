package com.hujing.wechat.sell.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author hj
 * @time 2019-03-01 22:22
 * @description
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerInfo {

    @Id
    private String id;

    /**
     * 用户名
     */
    private  String username;

    /**
     * 密码
     */
    private String password;

    /**
     * openid
     */
    private String openid;

    private Date createTime;

    private Date updateTime;

}
