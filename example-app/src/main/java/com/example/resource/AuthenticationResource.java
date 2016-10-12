/**
 * Created by Philip A Senger on November 10, 2015
 */
package com.example.resource;


import com.example.entity.bean.Token;
import com.example.entity.mongo.User;
import com.example.exception.EntityNotFoundException;
import com.example.repository.UserRepository;
import com.example.util.TokenUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Component
@PermitAll
@Path("/authentication")
public class AuthenticationResource {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationResource.class);
    /**
     * HK2 Injection.
     */
    @Context
    UserRepository dao;

    @Inject
    private StringRedisTemplate stringRedisTemplate;


    @Value("${example.jwt.key}")
    String key;


    String redisKey = "USER_{0}_TOKEN";

    @POST
    @Produces("application/json")
    @Consumes("application/x-www-form-urlencoded")
    public Response authenticateUser(@FormParam("username") String username,
                                     @FormParam("password") String password) {

        Date expiry = getExpiryDate(120);
        User user = authenticate(username, password);

        logger.info("JWT key:{}", key);
        String jwtString = TokenUtil.getJWTString(username, user.getRoles(), user.getVersion(), user.getId(), expiry, key);
        Token token = new Token();
        token.setAuthToken(jwtString);
        token.setExpires(expiry);
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();

        String key = MessageFormat.format(redisKey, user.getId().toString());
        opsForValue.set(key, jwtString, 120, TimeUnit.MINUTES);

        String value = opsForValue.get(key);
        logger.info("value:{}", value);
        return Response.ok(token).build();
    }

    @Path("refresh")
    @GET
    @Produces("application/json")
    public Response refreshToken(@QueryParam("token") String token) {

        logger.info("token is " + token);
        if (TokenUtil.isValid(token, key)) {
            Long id = Long.valueOf(TokenUtil.getId(token, key));
            String tokenInRedis = stringRedisTemplate.opsForValue().get(MessageFormat.format(redisKey, id.toString()));


            if (null == tokenInRedis || !tokenInRedis.equals(token)) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            Date expiry = getExpiryDate(120);
            String userName = TokenUtil.getName(token, key);
            String[] roles = TokenUtil.getRoles(token, key);
            Integer version = TokenUtil.getVersion(token, key);
            String jwtString = TokenUtil.getJWTString(userName, roles, version, id, expiry, key);
            Token jwtToken = new Token();
            jwtToken.setAuthToken(jwtString);
            jwtToken.setExpires(expiry);
            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
            opsForValue.set(MessageFormat.format(redisKey, id.toString()), jwtString, 120, TimeUnit.MINUTES);
            return Response.ok(jwtToken).build();
        } else {
            logger.info("刷新 token 失效");
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    /**
     * get Expire date in minutes.
     *
     * @param minutes the minutes in the future.
     */

    private Date getExpiryDate(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private User authenticate(String username, String password) throws NotAuthorizedException {
        // Validate the extracted credentials
        User user = null;
        try {
            user = dao.findByUserName(username);
        } catch (EntityNotFoundException e) {
            logger.info("Invalid username '" + username + "' ");
            throw new NotAuthorizedException("Invalid username '" + username + "' ");
        }
        // we need to actually test the Hash not the password, we should never store the password in the database.
        if (user.getPassword().equals(password)) {
            logger.info("USER AUTHENTICATED");
        } else {
            logger.info("USER NOT AUTHENTICATED");
            throw new NotAuthorizedException("Invalid username or password");
        }
        return user;
    }


}
