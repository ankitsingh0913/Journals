package com.xclone.learning.Services.ExternalAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xclone.learning.APIResponse.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisServices {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entitYClass){
        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(o.toString(),entitYClass);
        }catch (Exception e){
            log.error("Exeption: ",e);
            return null;
        }
    }

    public void set(String key, Object o,Long ttl){
        try{
            ObjectMapper mapper = new ObjectMapper();
            String jsonValue = mapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,jsonValue,ttl, TimeUnit.SECONDS);
        }catch (Exception e){
            log.error("Exeption: ",e);
        }
    }
}
