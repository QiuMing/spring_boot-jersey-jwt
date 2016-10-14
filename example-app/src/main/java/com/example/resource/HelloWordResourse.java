package com.example.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by binglin on 2016/9/11.
 */
@Path("hello")
public class HelloWordResourse {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        logger.info("");
        return "中文";
    }

}
