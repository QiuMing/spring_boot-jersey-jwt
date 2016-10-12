package com.example.filter;

import com.example.annotation.RequestLimit;
import com.example.exception.ServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
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

    @Inject
    private StringRedisTemplate stringRedisTemplate;

    @Context
    private HttpServletRequest servletRequest;

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext context) throws IOException {

        Method resourceMethod = resourceInfo.getResourceMethod();

        RequestLimit requestLimit = resourceMethod.getAnnotation(RequestLimit.class);

        if(null == requestLimit){
            return;
        }else{
            String uri = context.getUriInfo().getPath();
            String ip = servletRequest.getRemoteHost();
            String url = servletRequest.getRequestURL().toString();
            String method = servletRequest.getMethod();
            String key = "req_limit_".concat(uri).concat(method).concat(ip);
            logger.info("count:{},time:{},method:{},uri:{},ip:{},url:{},key:{}",requestLimit.count(),requestLimit.time(),context.getMethod(),uri,ip,url,key);

            long count = stringRedisTemplate.opsForValue().increment(key,1);

            if (count == 1) {
                stringRedisTemplate.expire(key, requestLimit.time(), TimeUnit.MILLISECONDS);
            }
            if(count > requestLimit.count()){
                logger.info("用户IP[" + ip + "]访问地址[" + url + "]超过了限定的次数[" + requestLimit.count() + "]");
                throw new ServiceException(30001, "require limit");
            }
        }
    }
}
