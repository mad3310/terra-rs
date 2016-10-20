package com.le.matrix.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 测试服务 开发测试时使用
 */
public class ServiceTemplateProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceTemplateProvider.class);

    public static void main(String[] args) {
        //启动服务方法1
        /*try {
            ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/spring-context.xml");
			context.start();
		} catch (Exception e) {
            logger.error("== DubboProvider context start error:",e);
		}
		synchronized (ServiceRedisProvider.class) {
			while (true) {
				try {
                    ServiceRedisProvider.class.wait();
				} catch (InterruptedException e) {
                    logger.error("== synchronized error:",e);
				}
			}
		}*/
        //启动服务方法2   dubbo建议使用
        com.alibaba.dubbo.container.Main.main(args);
    }
}
