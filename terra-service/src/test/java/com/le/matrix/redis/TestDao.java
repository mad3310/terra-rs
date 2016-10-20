package com.le.matrix.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.le.matrix.redis.dao.DemoDao;
import com.le.matrix.redis.model.Demo;

/**
 * Created by linzhanbo on 2016/10/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-context.xml"})
public class TestDao {
    @Autowired
    private DemoDao userDao;

    @Test
    public void testUserDaoInsert() {
        Demo user = new Demo();
        user.setUsername("Tom");
        user.setPassword("123456");
        int result = userDao.insert(user);
        System.out.println(result);
    }

}
