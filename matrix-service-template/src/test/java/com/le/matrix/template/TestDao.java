package com.le.matrix.template;

import com.le.matrix.template.dao.GrageDao;
import com.le.matrix.template.dao.UserDao;
import com.le.matrix.template.model.Grage;
import com.le.matrix.template.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by linzhanbo on 2016/10/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring/spring-context.xml"})
public class TestDao {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GrageDao grageDao;

    @Test
    public void testUserDaoInsert() {
        User user = new User();
        user.setUsername("Tom");
        user.setPassword("123456");
        int result = userDao.insert(user);
        System.out.println(result);
    }

    @Test
    public void testGrageDaoInsert() {
        Grage grage = new Grage();
        grage.setName("grage1");
        int result = grageDao.insert(grage);
        System.out.println(result);
    }

    @Test
    public void testTransaction() {
        User user = new User();
        user.setUsername("Tom");
        user.setPassword("123456");
        int result = userDao.insert(user);
        Grage grage = new Grage();
        grage.setId(1);
        grage.setName("grage1");
        result = grageDao.insert(grage);
    }
}
