package com.le.matrix.redis.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.le.matrix.hemera.facade.ITaskEngine;
import com.le.matrix.redis.constant.Constant;
import com.le.matrix.redis.dao.IRedisDao;
import com.le.matrix.redis.enumeration.AuditStatus;
import com.le.matrix.redis.facade.IQuotaUserService;
import com.le.matrix.redis.facade.IRedisService;
import com.le.matrix.redis.facade.IUserService;
import com.le.matrix.redis.model.QuotaUser;
import com.le.matrix.redis.model.Redis;
import com.le.matrix.redis.model.User;
import com.le.matrix.redis.util.LockUtil;
import com.le.matrix.redis.util.RedisHttpClient;
import com.letv.common.dao.IBaseDao;
import com.letv.common.email.ITemplateMessageSender;
import com.letv.common.email.bean.MailMessage;
import com.letv.common.exception.ValidateException;
import com.letv.common.paging.impl.Page;
import com.letv.common.result.ApiResultObject;

@Service("redisService")
public class RedisServiceImpl extends BaseServiceImpl<Redis> implements IRedisService{
	
	public final static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	@Autowired
	private IQuotaUserService quotaUserService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IRedisDao redisDao;
	@Value("${redis.url}")
	private String redisUrl;
	@Autowired
	private ITaskEngine taskEngine;
	
	@Value("${redis.audit.email.to}")
	private String REDIS_AUDIT_MAIL_TO;
	
	@Autowired
	private ITemplateMessageSender defaultEmailSender;
	
	public RedisServiceImpl() {
		super(Redis.class);
	}

	@Override
	public IBaseDao<Redis> getDao() {
		return redisDao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Page queryByPagination(Page page, Map<K, V> params) {
		Page p = super.queryByPagination(page, params);
		List<Redis> rediss = (List<Redis>) p.getData();
		List<Map<String, Object>> rets = new ArrayList<Map<String, Object>>();
		for (Redis redis : rediss) {
			Map<String, Object> ret = new HashMap<String, Object>();
			rets.add(ret);
			if(StringUtils.isNotEmpty(redis.getServiceId())) {
				ApiResultObject apiResult = this.getInfoById(Long.parseLong(redis.getServiceId()));
				List<Object> instances = JSONObject.parseArray(apiResult.getResult());
				Map<String, Object> info = (Map<String, Object>) instances.get(0);
				Map<String, Object> instance = (Map<String, Object>) info.get("appDesc");
				ret.put("name", instance.get("name"));
				ret.put("status", instance.get("status"));
				ret.put("type", redis.getType());
				ret.put("memorySize", instance.get("memSize"));
				ret.put("createTime", instance.get("createTime"));
				
				ret.put("domain", instance.get("domain"));
				ret.put("clusterId", instance.get("clusterId"));
				ret.put("clusterName", instance.get("clusterName"));
				ret.put("clusterId", instance.get("clusterId"));
				ret.put("configId", instance.get("configFile"));
				ret.put("configName", instance.get("configFileName"));
				ret.put("region", instance.get("region"));
				ret.put("regionCNname", instance.get("regionCNname"));
				ret.put("availableZoneId", instance.get("availableZoneId"));
				ret.put("availableZoneName", instance.get("availableZoneName"));
			} else {
				ret.put("name", redis.getName());
				ret.put("status", redis.getAuditStatus().getValue());
				ret.put("type", redis.getType());
				ret.put("memorySize", redis.getMemorySize());
				ret.put("createTime", redis.getCreateTime());
			}
		}
		p.setData(rets);
		return p;
	}
	
	@Override
	public ApiResultObject getReidsRegion() {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+"/redis/region/list");
		return apiResult;
	}
	
	@Override
	public ApiResultObject getReidsRegionAz(Long regionId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+StringUtils.replace("/redis/region/{}/availableZone", "{}", String.valueOf(regionId)));
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
	public ApiResultObject checkCanCreate(Long clusterId, Integer memorySize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("clusterId", String.valueOf(clusterId));
		params.put("memorySize", String.valueOf(memorySize));
		ApiResultObject apiResult = RedisHttpClient.post(redisUrl+"/redis/service/checkCanCreate", params);
		return apiResult;
	}

	@Override
	public ApiResultObject getStatusById(Long id) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/status", "{}", String.valueOf(id)));
		return apiResult;
	}
	
	@Override
	public ApiResultObject getStatusWithAnalyse(Long id) {
		ApiResultObject apiResult = getStatusById(id);
		analyzeStatusResult(apiResult);
		return apiResult;
	}
	
	@SuppressWarnings("unchecked")
	private void analyzeStatusResult(ApiResultObject apiResult) {
		if(apiResult.getAnalyzeResult()) {
			Map<String, Object> resultMap = JSONObject.parseObject(apiResult.getResult(), Map.class);
			Object status = resultMap.get("status");
			if(null != status && 2 != (Integer)status) {
				apiResult.setAnalyzeResult(false);
			}
		}
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
	public ApiResultObject createInstance(Map<String, String> params) {
		ApiResultObject apiResult = RedisHttpClient.post(redisUrl+"/redis/service/save", params);
		//变更为符合工作流模板方式
		analyzeCreateResult(apiResult);
		return apiResult;
	}
	
	@SuppressWarnings("unchecked")
	private void analyzeCreateResult(ApiResultObject apiResult) {
		if(apiResult.getAnalyzeResult()) {
			Map<String, Object> resultMap = JSONObject.parseObject(apiResult.getResult(), Map.class);
			resultMap = (Map<String, Object>) resultMap.get("app");
			String serviceId = String.valueOf(resultMap.get("appId"));
			apiResult.setResult(serviceId);
		}
	}
	
	@Override
	public void build(Redis redis) {
		User u = userService.getUserById(redis.getCreateUser());
		
		if(LockUtil.getDistributedLock(String.valueOf(u.getId()))) {//获取到分布式锁
			boolean checkResult = quotaUserService.checkQuota(Constant.QUOTA_REDIS_NAME, Constant.QUOTA_REDIS_TYPE, 1l);
			if(checkResult) {
				auditAndBuild(redis, null, null);
			} else {
				//发送邮件 进行审批
				sendAuditEmail(redis, u);
			}
			//释放锁
			LockUtil.releaseDistributedLock(String.valueOf(u.getId()));
		} else {
			throw new ValidateException("服务器正忙,请稍后重试!");
		}
	}
	
	private void sendAuditEmail(Redis redis, User u) {
		//邮件通知
		Map<String,Object> emailParams = new HashMap<String,Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		emailParams.put("createUser", u.getUserName());
		emailParams.put("createTime", sdf.format(new Date()));
		emailParams.put("redisName", redis.getName());
		MailMessage mailMessage = new MailMessage("乐视云平台Matrix系统Redis服务审批",REDIS_AUDIT_MAIL_TO,"乐视云平台Matrix系统Redis服务审批","auditRedisNotice.ftl",emailParams);
		defaultEmailSender.sendMessage(mailMessage);
	}
	
	
	@Override
	public void auditAndBuild(Redis redis, String auditInfo, Long auditUser) {
		if(LockUtil.getDistributedLock("createRedis")) {
			User u = userService.getUserById(redis.getCreateUser());
			//校验redis服务是否合法
			ApiResultObject apiResultObject = this.checkNameExist(redis.getName());
			
			if(apiResultObject.getAnalyzeResult()) {//合法
				//进行审核
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", redis.getId());
				map.put("name", redis.getName());
				map.put("type", redis.getType().getValue());
				map.put("memSize", redis.getMemorySize());
				map.put("username", u.getUserName());
				map.put("config", redis.getConfigId());
				map.put("password", redis.getPassword());
				map.put("zoneId", redis.getAzId());
				this.taskEngine.run("REDIS_CREATE", map);
				
				//更新配额使用量
				updateQuotaUser();
				
				//更新redis信息
				updateRedisAudit(redis, auditInfo, auditUser, AuditStatus.APPROVE);
			}
			
			LockUtil.releaseDistributedLock("createRedis");
		} else {
			throw new ValidateException("服务器正忙,请稍后重试!");
		}
		
	}
	
	private void updateQuotaUser() {
		List<QuotaUser> quotaUsers = quotaUserService.getUserQuotaByProductNameAndType(Constant.QUOTA_REDIS_NAME, Constant.QUOTA_REDIS_TYPE);
		QuotaUser quotaUser = quotaUsers.get(0);
		quotaUser.setUsed(quotaUser.getUsed()+1);
		quotaUserService.updateBySelective(quotaUser);
	}
	
	@Override
	public void reject(Redis redis, String auditInfo, Long auditUser) {
		updateRedisAudit(redis, auditInfo, auditUser, AuditStatus.REJECT);
	}
	
	private void updateRedisAudit(Redis redis, String auditInfo, Long auditUser, AuditStatus auditStatus) {
		redis.setAuditInfo(auditInfo);
		redis.setAuditStatus(auditStatus);
		redis.setAuditTime(new Date());
		this.redisDao.updateBySelective(redis);
	}

	@Override
	public ApiResultObject updateServiceIdById(String serviceId, Long id) {
		Redis r = new Redis();
		r.setId(id);
		r.setServiceId(serviceId);
		this.redisDao.updateBySelective(r);
		
		ApiResultObject apiResult = new ApiResultObject();
		apiResult.setAnalyzeResult(true);
		return apiResult;
	}

	@Override
	public void insert(Redis redis) {
		super.insert(redis);
		logger.info("检测该用户redis配额并执行");
    	this.build(redis);
	}
}