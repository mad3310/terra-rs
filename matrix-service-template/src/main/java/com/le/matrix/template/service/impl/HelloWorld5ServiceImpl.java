package com.le.matrix.template.service.impl;

import com.le.matrix.template.facade.HelloWorld5Facade;
import com.le.matrix.template.facade.HelloWorldFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by linzhanbo on 2016/10/10.
 */
@Service("helloWorld5Service")
public class HelloWorld5ServiceImpl implements HelloWorld5Facade {
    private Logger logger = LoggerFactory.getLogger(HelloWorld5ServiceImpl.class);
    @Autowired
    private HelloWorldFacade helloWorldFacade;

    @Override
    public String sayHello(String name) {
        return helloWorldFacade.sayHello(name);
    }
}
