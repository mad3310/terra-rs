package com.le.matrix.template.service.impl;

import com.le.matrix.template.facade.HelloWorldFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by linzhanbo on 2016/10/10.
 */
@Service("helloWorldService")
public class HelloWorldServiceImpl implements HelloWorldFacade {
    private Logger logger = LoggerFactory.getLogger(HelloWorldServiceImpl.class);

    @Override
    public String sayHello(String username) {
        String helloStr = "Hello," + username;
        logger.debug("sayhello to {}", username);
        return helloStr;
    }
}
