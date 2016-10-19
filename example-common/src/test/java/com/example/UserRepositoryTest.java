package com.example;

import com.alibaba.fastjson.JSON;
import com.example.entity.mongo.User;
import com.example.repository.SequenceBuilder;
import com.example.repository.UserRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by binglin on 2016/10/6.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Rollback(false)
    @Test
    public void testAddUser() {
        User user = new User(SequenceBuilder.builder("user"));
        user.setUserName("binglin");
        user.setPassword("123456");
        user.setRoles(new String[]{"user"});
        userRepository.save(user);
    }

    @Test
    public void TestFindUser() {
        User user = userRepository.findOne(20000L);
        user.setVersion(1);
        userRepository.save(user);
    }

    @Test
    public void test_findAll(){
        System.out.println(JSON.toJSONString(userRepository.findAll()));
    }
}
