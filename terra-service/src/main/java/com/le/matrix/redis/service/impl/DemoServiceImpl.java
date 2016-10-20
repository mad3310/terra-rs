package com.le.matrix.redis.service.impl;

import com.le.matrix.redis.dao.DemoDao;
import com.le.matrix.redis.facade.IDemo;
import com.le.matrix.redis.model.Demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by linzhanbo on 2016/10/11.
 */
@Service("demoService")
public class DemoServiceImpl implements IDemo {
    private Logger logger = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private DemoDao userDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        logger.debug("Delete User：{}", id);
        return userDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Demo record) {
        logger.debug("Insert User：{}", record.toString());
        return userDao.insert(record);
    }

    @Override
    public Demo selectByPrimaryKey(Integer id) {
        logger.debug("Select User：{}", id);
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Demo record) {
        logger.debug("Update User：{}", record.toString());
        return userDao.updateByPrimaryKeySelective(record);
    }
}
