package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

/**
 * Created by Ming on 2016/10/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Inject
    RedisTemplate<String,String> redisTemplate;

    @Rollback(false)
    @Test
    public void test_increment(){

        String key = "key_one";
        long count = redisTemplate.opsForValue().increment(key, 1);
        System.out.println("count="+count);
    }
}
