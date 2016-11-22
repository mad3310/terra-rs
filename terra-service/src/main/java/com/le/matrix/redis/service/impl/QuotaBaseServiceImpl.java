package com.le.matrix.redis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.le.matrix.redis.dao.IQuotaBaseDao;
import com.le.matrix.redis.facade.IQuotaBaseService;
import com.le.matrix.redis.model.QuotaBase;
import com.letv.common.dao.IBaseDao;

@Service("quotaBaseService")
public class QuotaBaseServiceImpl extends BaseServiceImpl<QuotaBase> implements IQuotaBaseService{
	
	@Autowired
	private IQuotaBaseDao quotaBaseDao;
	
	public QuotaBaseServiceImpl() {
		super(QuotaBase.class);
	}

	@Override
	public IBaseDao<QuotaBase> getDao() {
		return quotaBaseDao;
	}

	@Override
	public List<QuotaBase> getDefaultQuotaByProductNameAndType(String productName, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", productName);
		params.put("type", type);
		return this.quotaBaseDao.selectByMap(params);
	}
	
	public void deleteByPrimaryKey(Long id) {
		quotaBaseDao.deleteByPrimaryKey(id);
	}

}