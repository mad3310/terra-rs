package com.le.matrix.redis.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.le.matrix.redis.dao.IRedisDao;
import com.le.matrix.redis.facade.IRedisService;
import com.le.matrix.redis.model.Redis;
import com.le.matrix.redis.util.RedisHttpClient;
import com.letv.common.dao.IBaseDao;
import com.letv.common.result.ApiResultObject;

@Service("redisService")
public class RedisServiceImpl extends BaseServiceImpl<Redis> implements IRedisService{
	
	@Autowired
	private IRedisDao redisDao;
	@Value("${redis.url}")
	private String redisUrl;
	
	public RedisServiceImpl() {
		super(Redis.class);
	}

	@Override
	public IBaseDao<Redis> getDao() {
		return redisDao;
	}
	
	@Override
	public ApiResultObject getReidsRegion() {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+"/redis/region/list");
		return apiResult;
	}

}