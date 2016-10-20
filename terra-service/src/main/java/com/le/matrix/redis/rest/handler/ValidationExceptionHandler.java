package com.le.matrix.redis.rest.handler;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Created by linzhanbo on 2016/10/12.
 */
public class ValidationExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.BAD_REQUEST).entity("Fill all fields").build();
    }
}
