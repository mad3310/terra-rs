package com.le.matrix.redis.dao;

import com.le.matrix.redis.model.QuotaBase;
import com.letv.common.dao.IBaseDao;

public interface IQuotaBaseDao extends IBaseDao<QuotaBase>{
	
    int deleteByPrimaryKey(Long id);

    int insertSelective(QuotaBase record);

    int updateByPrimaryKeySelective(QuotaBase record);

    int updateByPrimaryKey(QuotaBase record);
}