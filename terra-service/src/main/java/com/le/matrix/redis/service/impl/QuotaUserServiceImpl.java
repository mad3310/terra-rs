package com.le.matrix.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.le.matrix.redis.dao.IQuotaUserDao;
import com.le.matrix.redis.facade.IQuotaUserService;
import com.le.matrix.redis.model.QuotaUser;
import com.letv.common.dao.IBaseDao;

@Service("quotaUserService")
public class QuotaUserServiceImpl extends BaseServiceImpl<QuotaUser> implements IQuotaUserService{
	
	@Autowired
	private IQuotaUserDao quotaUserDao;
	
	public QuotaUserServiceImpl() {
		super(QuotaUser.class);
	}

	@Override
	public IBaseDao<QuotaUser> getDao() {
		return quotaUserDao;
	}

}