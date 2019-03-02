package com.hujing.wechat.sell.util;

/**
 * @author hj
 * @time 2019-03-02 20:29
 * @description redis实现的分布式锁
 */

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 加锁
     * @param k
     * @param v 当前时间+超时时间
     * @return 是否获取锁
     */
    public boolean lock(String k, String v) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        if (valueOperations.setIfAbsent(k, v)){
           return true;
        }
        String currentValue = valueOperations.get(k);
        if (!StringUtils.isEmpty(currentValue) && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            //已经超时,使用getAndSet保证只有一个线程获取锁
            String preValue = valueOperations.getAndSet(k, v);
            if (!StringUtils.isEmpty(preValue) && preValue.equals(currentValue)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 解锁
     * @param k
     * @param v
     */
    public void unlock(String k ,String v) {
        if (v.equals(redisTemplate.opsForValue().get(k))) {
            redisTemplate.opsForValue().getOperations().delete(k);
        }
    }
}
