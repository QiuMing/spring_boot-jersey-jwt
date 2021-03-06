package com.example.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by binglin on 2016/9/11.
 */
@Component
@Path("hello")
public class HelloWordResourse {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        logger.info("hello");
        return "hello";
    }

    @GET
    public String reverse(@QueryParam("input") @NotNull String input) {
        return new StringBuilder(input).reverse().toString();
    }

}
