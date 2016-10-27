package com.le.matrix.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.le.matrix.redis.dao.IRedisDao;
import com.le.matrix.redis.facade.IRedisService;
import com.le.matrix.redis.model.Redis;
import com.letv.common.dao.IBaseDao;

@Service("redisService")
public class RedisServiceImpl extends BaseServiceImpl<Redis> implements IRedisService{
	
	@Autowired
	private IRedisDao redisDao;
	
	public RedisServiceImpl() {
		super(Redis.class);
	}

	@Override
	public IBaseDao<Redis> getDao() {
		return redisDao;
	}

}