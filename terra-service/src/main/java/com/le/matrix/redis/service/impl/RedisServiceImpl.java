package com.le.matrix.redis.service.impl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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

	@Override
	public ApiResultObject getReidsConfig() {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+"/redis/config/list");
		return apiResult;
	}
	
	@Override
	public ApiResultObject getReidsConfigById(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/config/{}", "{}", String.valueOf(id)));
		return apiResult;
	}

	@Override
	public ApiResultObject checkNameExist(String name) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("name", name);
		ApiResultObject apiResult = RedisHttpClient.post(redisUrl+"/redis/service/checkNameExist", params);
		return apiResult;
	}

	@Override
	public ApiResultObject getStatusById(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/status", "{}", String.valueOf(id)));
		return apiResult;
	}

	@Override
	public ApiResultObject getInfoById(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}", "{}", String.valueOf(id)));
		return apiResult;
	}

	@Override
	public ApiResultObject offline(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/offline", "{}", String.valueOf(id)));
		return apiResult;
	}

	@Override
	public ApiResultObject start(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/start", "{}", String.valueOf(id)));
		return apiResult;
	}
	
	@Override
	public ApiResultObject deleteInstance(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/delete", "{}", String.valueOf(id)));
		return apiResult;
	}

	@Override
	public ApiResultObject create(Redis redis) {
		// TODO Auto-generated method stub
		return null;
	}


}