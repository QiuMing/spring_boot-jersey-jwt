package com.example.resource;

import org.springframework.data.redis.core.StringRedisTemplate;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by binglin on 2016/9/11.
 */
@Path("hello")
public class HelloWordResourse {

    @Inject
    private StringRedisTemplate stringRedisTemplate;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {

        stringRedisTemplate.getExpire("A");
        return "中文";
    }

}
