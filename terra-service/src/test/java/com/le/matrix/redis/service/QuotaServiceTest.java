package com.le.matrix.redis.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.redis.facade.IQuotaBaseService;
import com.le.matrix.redis.facade.IQuotaUserService;
import com.le.matrix.redis.junitBase.AbstractTest;
import com.le.matrix.redis.model.QuotaBase;
import com.le.matrix.redis.model.QuotaUser;

public class QuotaServiceTest extends AbstractTest{

	@Autowired
	private IQuotaBaseService quotaBaseService;
	@Autowired
	private IQuotaUserService quotaUserService;
	
	private final static Logger logger = LoggerFactory.getLogger(
			QuotaServiceTest.class);
	
	private int quotaBasesTotalSize = 0;
	private int quotaUsersTotalSize = 0;
	private Long quotaBaseId = null;
	private Long quotaUserId = null;
	
	@Before
    public void testServiceBefore() {
    	List<QuotaBase> quotaBases = quotaBaseService.selectByMap(null);
    	quotaBasesTotalSize = quotaBases.size();
    	logger.info("查询quotaBases获取totalSize：{}", quotaBasesTotalSize);
    	
    	List<QuotaUser> quotaUsers = quotaUserService.selectByMap(null);
    	quotaUsersTotalSize = quotaUsers.size();
    	logger.info("查询quotaUsers获取totalSize：{}", quotaUsersTotalSize);
    }
    
    @Test
    public void testInsertOneAndCheckSize() throws InterruptedException {
    	QuotaBase qb = insertQuotaBase();
    	checkQuotaBaseSize(quotaBasesTotalSize+1);
    	checkQuotaBaseDefaultSize(qb);
    	
    	insertQuotaUser(qb);
    	checkQuotaUserSize(quotaUsersTotalSize+1);
    }
    
    private QuotaBase insertQuotaBase() {
    	QuotaBase qb = new QuotaBase();
    	qb.setName("junitTest"+System.currentTimeMillis());
    	qb.setType("num");
    	qb.setValue(3l);
    	qb.setCreateTime(new Timestamp(new Date().getTime()));
    	qb.setDescn("test insert quotaBase");
    	quotaBaseService.insert(qb);
    	quotaBaseId = qb.getId();
    	logger.info("quotaBase插入成功，quotaBaseId：{}", quotaBaseId);
    	return qb;
    }
    
    private void checkQuotaBaseSize(long size) {
    	List<QuotaBase> quotaBases = quotaBaseService.selectByMap(null);
    	Assert.assertEquals(size, quotaBases.size());
    }
    private void checkQuotaBaseDefaultSize(QuotaBase qb) {
    	//根据名称查询size
    	List<QuotaBase> qbs = quotaBaseService.getDefaultQuotaByProductName(qb.getName());
    	Assert.assertEquals(1, qbs.size());
    }
  
    
    private QuotaUser insertQuotaUser(QuotaBase qb) {
    	QuotaUser qu = new QuotaUser();
    	qu.setUsed(2l);
    	qu.setQuotaBaseId(qb.getId());
    	qu.setDescn("test insert quotaUser");
    	qu.setCreateTime(new Timestamp(new Date().getTime()));
    	quotaUserService.insert(qu);
    	quotaUserId = qu.getId();
    	logger.info("quotaUser插入成功，quotaUserId：{}", qu.getId());
    	return qu;
    }
    
    private void checkQuotaUserSize(long size) {
    	List<QuotaUser> quotaUsers = quotaUserService.selectByMap(null);
    	Assert.assertEquals(size, quotaUsers.size());
    }
    
    @After
    public void testDeleteAfterAndCheck() {
    	quotaBaseService.deleteByPrimaryKey(quotaBaseId);
    	logger.info("quotaBase删除成功，quotaBaseId：{}", quotaBaseId);
    	
    	quotaUserService.deleteByPrimaryKey(quotaUserId);
    	logger.info("quotaUser删除成功，quotaUserId：{}", quotaUserId);
    	
    	checkQuotaBaseSize(quotaBasesTotalSize);
    	checkQuotaUserSize(quotaUsersTotalSize);
    }

}