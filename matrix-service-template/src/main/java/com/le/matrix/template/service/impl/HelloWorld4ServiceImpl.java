package com.le.matrix.template.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.template.facade.HelloWorld4Facade;
import com.le.matrix.template.facade.HelloWorldFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by linzhanbo on 2016/10/10.
 */
//配置dubbo Service及其Rest路径
@Service(protocol = {"rest", "dubbo"}, group = "annotationConfig", validation = "true")
//消费端也有@Reference，可省去在xml里面配置dubbo:reference
//REST路径
@Path("helloWorld4")
//标注可接受请求的MIME媒体类型
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//标注返回的MIME媒体类型
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class HelloWorld4ServiceImpl implements HelloWorld4Facade {
    private Logger logger = LoggerFactory.getLogger(HelloWorld4ServiceImpl.class);
    @Autowired
    private HelloWorldFacade helloWorldFacade;

    @GET
    @Path("{id}")
    @Override
    public String sayHello(@PathParam("id") String name) {
        return helloWorldFacade.sayHello(name);
    }
}
