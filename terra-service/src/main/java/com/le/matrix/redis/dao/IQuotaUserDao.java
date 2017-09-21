package com.le.matrix.redis.dao;

import com.le.matrix.redis.model.QuotaUser;
import com.letv.common.dao.IBaseDao;

public interface IQuotaUserDao extends IBaseDao<QuotaUser> {
	
	int deleteByPrimaryKey(Long id);

	int insertSelective(QuotaUser record);

	int updateByPrimaryKeySelective(QuotaUser record);

	int updateByPrimaryKey(QuotaUser record);
}