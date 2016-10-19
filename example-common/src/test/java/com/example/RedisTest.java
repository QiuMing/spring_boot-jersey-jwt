package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Created by Ming on 2016/10/12.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {

    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Autowired
    Redisson redisson;

    @Rollback(false)
    @Test
    public void testIncrement(){
        String key = "key_one";
        long count = redisTemplate.opsForValue().increment(key, 1);
        System.out.println("count="+count);
    }

    @Rollback(false)
    @Test
    public void testRLock(){
        RLock lock = redisson.getLock("foobar"); // 1.获得锁对象实例
        //lock.lock(1, TimeUnit.MINUTES);
        try {
            //第一个参数代表等待时间，第二是代表超过时间释放锁，第三个代表设置的时间制
            lock.tryLock(0, 1, TimeUnit.MINUTES);
            System.out.printf("业务处理");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } /*finally {
            //lock.unlock(); // 3.释放锁
        }*/
    }
}
