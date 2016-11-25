package com.le.matrix.redis.service.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.le.matrix.redis.dao.IQuotaUserDao;
import com.le.matrix.redis.facade.IQuotaBaseService;
import com.le.matrix.redis.facade.IQuotaUserService;
import com.le.matrix.redis.model.QuotaBase;
import com.le.matrix.redis.model.QuotaUser;
import com.letv.common.dao.IBaseDao;

@Service("quotaUserService")
public class QuotaUserServiceImpl extends BaseServiceImpl<QuotaUser> implements IQuotaUserService{
	
	@Autowired
	private IQuotaUserDao quotaUserDao;
	@Autowired
	private IQuotaBaseService quotaBaseService;
	
	public QuotaUserServiceImpl() {
		super(QuotaUser.class);
	}

	@Override
	public IBaseDao<QuotaUser> getDao() {
		return quotaUserDao;
	}

	public void deleteByPrimaryKey(Long id) {
		quotaUserDao.deleteByPrimaryKey(id);
	}

	@Override
	public boolean checkQuota(Long userId, String productName, String type, Long value) {
		//根据产品名称和类型查询是否有用户配额信息
		List<QuotaUser> quotaUsers = getUserQuotaByProductNameAndType(userId, productName, type);
		
		//待检查配额
		QuotaUser checkQuota = null;
		
		//为空写入用户配额
		if(CollectionUtils.isEmpty(quotaUsers)) {
			checkQuota = insertUserQuota(userId, productName, type);
		} else {
			checkQuota = quotaUsers.get(0);
		}
		
		//检查(已经使用值+带检查值>配额)
		if(checkQuota.getUsed()+value > checkQuota.getQuotaBase().getValue()) {
			return false;
		} else {
			return true;
		}
	}
	
	public List<QuotaUser> getUserQuotaByProductNameAndType(Long userId, String productName, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", productName);
		params.put("type", type);
		params.put("createUser", userId);
		return quotaUserDao.selectByMap(params);
	}
	
	private QuotaUser insertUserQuota(Long userId, String productName, String type) {
		List<QuotaBase> quotaBases = quotaBaseService.getDefaultQuotaByProductNameAndType(productName, type);
		QuotaUser qu = null;
		for (QuotaBase quotaBase : quotaBases) {
			qu = new QuotaUser();
			qu.setUsed(0l);
			qu.setQuotaBaseId(quotaBase.getId());
			qu.setQuotaBase(quotaBase);
			qu.setCreateTime(new Timestamp(System.currentTimeMillis()));
			qu.setCreateUser(userId);
			quotaUserDao.insert(qu);
		}
		return qu;
	}
}