package com.pettalk.sms.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
public class RedisService {
    private StringRedisTemplate stringRedisTemplate;
    public RedisService( StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void setPhoneNumberWithExpiration(String phone, String authCode, long timeout, TimeUnit unit) {
        stringRedisTemplate.opsForValue().set(phone, authCode, timeout, unit);
    }

    public String getPhoneNumber(String phone) {
        return stringRedisTemplate.opsForValue().get(phone);
    }

    public void deletePhoneNumber(String phone) {
        stringRedisTemplate.delete(phone);
    }
}
