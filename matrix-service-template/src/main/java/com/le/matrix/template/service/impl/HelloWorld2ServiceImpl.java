package com.le.matrix.template.service.impl;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.template.facade.HelloWorld2Facade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by linzhanbo on 2016/10/10.
 */
@Service("helloWorld2Service")
//Rest定义、异常处理详看 RestEasy  http://dangdangdotcom.github.io/dubbox/rest.html
//如统一处理使用javax.ws.rs，若异常    http://redhacker.iteye.com/blog/1924071
//REST路径
@Path("helloWorld2")
//标注可接受请求的MIME媒体类型
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//标注返回的MIME媒体类型
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class HelloWorld2ServiceImpl implements HelloWorld2Facade {
    private Logger logger = LoggerFactory.getLogger(HelloWorld2ServiceImpl.class);

    @GET
    @Path("{id}")
    @Override
    public String sayHi(@PathParam("id") String id) {
        String hiStr = "Hello," + id;
        logger.debug("sayhello to {}", id);
        return hiStr;
    }
}
