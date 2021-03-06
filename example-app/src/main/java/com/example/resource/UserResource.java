package com.example.resource;

import com.example.annotation.Cache;
import com.example.entity.bean.UserBean;
import com.example.service.UserBeanService;
import com.example.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by binglin on 2016/9/17.
 */

@Path("/user")
@PermitAll
@RolesAllowed("user")
public class UserResource {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private UserService userService;

    @Inject
    private UserBeanService userBeanService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Cache
    public Response listUserBean() {
        List<UserBean> userBeans = userBeanService.findAll();
        Response.ResponseBuilder responseBuilder = Response.ok(userBeans);
        return responseBuilder.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUserBean(UserBean userBean) {
        userBeanService.save(userBean);
        return Response.status(Response.Status.CREATED).build();
    }
}
