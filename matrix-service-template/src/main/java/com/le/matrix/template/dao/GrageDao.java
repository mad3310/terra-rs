package com.le.matrix.template.dao;


import com.le.matrix.template.model.Grage;

public interface GrageDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Grage record);

    int insertSelective(Grage record);

    Grage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Grage record);

    int updateByPrimaryKey(Grage record);
}