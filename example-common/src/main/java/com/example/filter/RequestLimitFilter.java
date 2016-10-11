package com.example.filter;

import com.example.annotation.RequestLimit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

/**
 * Created by Ming on 2016/10/11.
 */

@Provider
public class RequestLimitFilter implements ContainerRequestFilter {


    private final  Logger logger = LoggerFactory.getLogger(RequestLimitFilter.class);

    @Context
    private RedisTemplate<String,String> redisTemplate;

    @Context
    private HttpServletRequest servletRequest;

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        String uri = context.getUriInfo().getPath();
        String ip = servletRequest.getRemoteHost();
        String url = servletRequest.getRequestURL().toString();
        logger.info("method:{},uri:{},ip:{},url:{}",context.getMethod(),url);
        String key = "req_limit_".concat(uri).concat(ip);

        Class<?> resourceClass = resourceInfo.getResourceClass();

        RequestLimit requestLimit = resourceClass.getAnnotation(RequestLimit.class);

        if(null != requestLimit){
            logger.info("count:{},time:{}",requestLimit.count(),requestLimit.time());
        }else{
            logger.info("requestLimit is null");
        }
        redisTemplate.opsForValue().increment(key,1);
    }
}
