package com.le.matrix.template.service.impl;

import com.le.matrix.template.dao.UserDao;
import com.le.matrix.template.facade.UserFacade;
import com.le.matrix.template.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by linzhanbo on 2016/10/11.
 */
@Service("userService")
public class UserServiceImpl implements UserFacade {
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Override
    public int deleteByPrimaryKey(Integer id) {
        logger.debug("Delete User：{}", id);
        return userDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(User record) {
        logger.debug("Insert User：{}", record.toString());
        return userDao.insert(record);
    }

    @Override
    public User selectByPrimaryKey(Integer id) {
        logger.debug("Select User：{}", id);
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        logger.debug("Update User：{}", record.toString());
        return userDao.updateByPrimaryKeySelective(record);
    }
}
