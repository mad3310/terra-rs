package com.le.matrix.template.dao;


import com.le.matrix.template.model.User;

/**
 * Created by linzhanbo on 2016/10/10.
 */
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}
