package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Ming on 2016/10/12.
 */
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
public class RedisTest {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Rollback(false)
    @Test
    public void test_increment(){

        String key = "key_one";
        long count = redisTemplate.opsForValue().increment(key, 1);
        System.out.println("count="+count);
    }
}
