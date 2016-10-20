package com.le.matrix.redis.dao;


import com.le.matrix.redis.model.Demo;

/**
 * Created by linzhanbo on 2016/10/10.
 */
public interface DemoDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Demo record);

    int insertSelective(Demo record);

    Demo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Demo record);

    int updateByPrimaryKey(Demo record);
}
