package com.le.matrix.template.facade;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by linzhanbo on 2016/10/10.
 */
//REST路径
@Path("helloWorld5")
//标注可接受请求的MIME媒体类型
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//标注返回的MIME媒体类型
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface HelloWorld5Facade {
    @GET
    @Path("{id}")
    String sayHello(@PathParam("id") String name);
}
