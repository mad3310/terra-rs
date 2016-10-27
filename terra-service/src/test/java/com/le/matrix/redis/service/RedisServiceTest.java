package com.le.matrix.redis.service;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.redis.enumeration.RedisType;
import com.le.matrix.redis.facade.IRedisService;
import com.le.matrix.redis.junitBase.AbstractTest;
import com.le.matrix.redis.model.Redis;
import com.letv.common.paging.impl.Page;

public class RedisServiceTest extends AbstractTest{

	@Autowired
	private IRedisService redisService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			RedisServiceTest.class);
	
	private int totalSize = 0;
	private Long redisId = null;
	
	@Before
    public void testServiceBefore() {
    	Page p = redisService.queryByPagination(new Page(), new HashMap<String, Object>());
    	totalSize = p.getTotalRecords();
    	logger.info("查询获取totalSize：{}", totalSize);
    }
    
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	Redis r = new Redis();
    	r.setName("junitTest"+System.currentTimeMillis());
    	r.setMemorySize(5);
    	r.setType(RedisType.RedisCluster);
    	r.setIdcId("test_idc_id");
    	r.setClusterId("test_cluster_id");
    	r.setConfigId("test_config_id");
    	r.setDescn("test_descn");
    	redisService.insert(r);
    	redisId = r.getId();
    	logger.info("redis创建成功，redisId：{}", redisId);
    	
    	Page p = redisService.queryByPagination(new Page(), new HashMap<String, Object>());
    	Assert.assertEquals(totalSize+1, p.getTotalRecords());
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	Redis r = new Redis();
    	r.setId(redisId);
    	redisService.delete(r);
    	logger.info("redis删除成功，redisId：{}", redisId);
    	
    	Page p = redisService.queryByPagination(new Page(), new HashMap<String, Object>());
    	Assert.assertEquals(totalSize, p.getTotalRecords());
    }

}