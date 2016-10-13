package com.le.matrix.template.service.impl;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.template.facade.HelloWorld3Facade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by linzhanbo on 2016/10/10.
 */
@Service("helloWorld3Service")
//REST路径
@Path("helloWorld3")
//标注可接受请求的MIME媒体类型
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//标注返回的MIME媒体类型
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class HelloWorld3ServiceImpl implements HelloWorld3Facade {
    private Logger logger = LoggerFactory.getLogger(HelloWorld3ServiceImpl.class);

    @GET
    @Path("{id}")
    @Override
    public String sayHz(@PathParam("id") String name) {
        String hiStr = "Hello," + name;
        logger.debug("sayhello to {}", name);
        return hiStr;
    }
}
