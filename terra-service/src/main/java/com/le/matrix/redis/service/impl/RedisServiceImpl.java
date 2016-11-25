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
			if(StringUtils.isNotEmpty(redis.getServiceId())) {//redis服务已创建
				useRemoteService(redis, ret);
			} else {//redis服务未创建
				useDbInfo(redis, ret);
			}
		}
		p.setData(rets);
		return p;
	}
	
	/**
	 * 使用调用远程redis服务获取redis信息
	 * @param redis
	 * @param ret
	 */
	@SuppressWarnings("unchecked")
	private void useRemoteService(Redis redis, Map<String, Object> ret) {
		ApiResultObject apiResult = this.getInstanceByServiceId(Long.parseLong(redis.getServiceId()));
		Map<String, Object> instance = null;
		if(apiResult.getAnalyzeResult()) {
			List<Object> instances = JSONObject.parseArray(apiResult.getResult());
			Map<String, Object> info = (Map<String, Object>) instances.get(0);
			instance = (Map<String, Object>) info.get("appDesc");
		} else {
			instance = new HashMap<String, Object>();
		}
		
		ret.put("name", instance.get("name"));
		ret.put("status", instance.get("status"));
		ret.put("type", redis.getType());
		ret.put("memorySize", instance.get("memSize"));
		ret.put("createTime", instance.get("createTime"));
		
		ret.put("domain", instance.get("domain")==null ? null : JSONObject.parseArray((String) instance.get("domain")));
		ret.put("clusterId", instance.get("clusterId"));
		ret.put("clusterName", instance.get("clusterName"));
		ret.put("configId", instance.get("configFile"));
		ret.put("configName", instance.get("configFileName"));
		ret.put("region", instance.get("region"));
		ret.put("regionCNname", instance.get("regionCNname"));
		ret.put("azId", instance.get("availableZoneId"));
		ret.put("azName", instance.get("availableZoneName"));
	}
	
	/**
	 * 使用数据库信息获取redis信息
	 * @param redis
	 * @param ret
	 */
	private void useDbInfo(Redis redis, Map<String, Object> ret) {
		ret.put("name", redis.getName());
		ret.put("status", redis.getAuditStatus().getValue());
		ret.put("type", redis.getType());
		ret.put("memorySize", redis.getMemorySize());
		ret.put("createTime", redis.getCreateTime());
		
		ret.put("domain", null);
		ret.put("configId", redis.getConfigId());
		ret.put("configName", getConfigName(Long.parseLong(redis.getConfigId())));
		
		Map<String, String> regionInfo = getRegionInfo(Long.parseLong(redis.getRegionId()));
		ret.put("region", regionInfo.get("regionName"));
		ret.put("regionCNname", regionInfo.get("regionCNname"));
		ret.put("azId", redis.getAzId());
		ret.put("azName", getAzName(Long.parseLong(redis.getAzId())));
	}
	
	@SuppressWarnings("rawtypes")
	private String getConfigName(Long configId) {
		ApiResultObject apiResult = getReidsConfigByConfigId(configId);
		if(apiResult.getAnalyzeResult()) {
			Map ret = JSONObject.parseObject(apiResult.getResult(), Map.class);
			return ret==null ? null : (String) ret.get("name");
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<String, String> getRegionInfo(Long regionId) {
		ApiResultObject apiResult = this.getRegionByRegionId(regionId);
		Map<String, String> ret = new HashMap<String, String>();
		if(apiResult.getAnalyzeResult()) {
			Map map = JSONObject.parseObject(apiResult.getResult(), Map.class);
			ret.put("regionName", map==null ? null : String.valueOf(map.get("regionName")));
			ret.put("regionCNname", map==null ? null : String.valueOf(map.get("regionCNname")));
		}
		return ret;
	}
	
	@SuppressWarnings("rawtypes")
	private String getAzName(Long azId) {
		ApiResultObject apiResult = this.getAzByAzId(azId);
		if(apiResult.getAnalyzeResult()) {
			Map ret = JSONObject.parseObject(apiResult.getResult(), Map.class);
			return ret==null ? null : (String) ret.get("name");
		}
		return null;
	}
	
	@Override
	public ApiResultObject getReidsRegion() {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+"/redis/region/list");
		return apiResult;
	}
	
	@Override
	public ApiResultObject getRegionByRegionId(Long regionId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+StringUtils.replace("/redis/region/{}", "{}", String.valueOf(regionId)));
		return apiResult;
	}
	
	@Override
	public ApiResultObject getAzByAzId(Long azId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl+StringUtils.replace("/redis/availableZone/{}", "{}", String.valueOf(azId)));
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
	public ApiResultObject getReidsConfigByConfigId(Long configId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/config/{}", "{}", String.valueOf(configId)));
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
	public ApiResultObject checkCanCreate(Long azId, Integer memorySize) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("zoneId", String.valueOf(azId));
		params.put("memSize", String.valueOf(memorySize));
		ApiResultObject apiResult = RedisHttpClient.post(redisUrl+"/redis/service/checkCanCreate", params);
		analyzeCanCreateResult(apiResult);
		return apiResult;
	}
	
	@SuppressWarnings("unchecked")
	private void analyzeCanCreateResult(ApiResultObject apiResult) {
		if(apiResult.getAnalyzeResult()) {
			Map<String, Object> resultMap = JSONObject.parseObject(apiResult.getResult(), Map.class);
			Boolean canCreate = (Boolean) resultMap.get("canCreate");
			apiResult.setAnalyzeResult(canCreate);
		}
	}

	@Override
	public ApiResultObject getStatusByServiceId(Long serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/status", "{}", String.valueOf(serviceId)));
		return apiResult;
	}
	
	@Override
	public ApiResultObject getStatusWithAnalyse(Long serviceId) {
		ApiResultObject apiResult = getStatusByServiceId(serviceId);
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
	public ApiResultObject getInstanceByServiceId(Long serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}", "{}", String.valueOf(serviceId)));
		return apiResult;
	}

	@Override
	public ApiResultObject offline(Long serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/offline", "{}", String.valueOf(serviceId)));
		return apiResult;
	}

	@Override
	public ApiResultObject start(Long serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/start", "{}", String.valueOf(serviceId)));
		return apiResult;
	}
	
	@Override
	public ApiResultObject deleteInstance(Long serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/delete", "{}", String.valueOf(serviceId)));
		return apiResult;
	}

	@Override
	public ApiResultObject createInstance(Map<String, String> params) {
		Long redisId = Long.parseLong(params.get("id"));
		Redis redis = this.selectById(redisId);
		ApiResultObject apiResult = null;
		//如果redis服务端创建失败后，重试时需要调用“reCreate”接口
		if(StringUtils.isNotEmpty(redis.getServiceId())) {
			apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/service/{}/reCreate", "{}", redis.getServiceId()));
			analyzeReCreateResult(apiResult);
		} else {
			apiResult = RedisHttpClient.post(redisUrl+"/redis/service/save", params);
			//变更为符合工作流模板方式
			analyzeCreateResult(apiResult);
		}
		return apiResult;
	}
	
	@Override
	public ApiResultObject createDomain(String serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/domain/{}/createDomain", "{}", String.valueOf(serviceId)));
		return apiResult;
	}
	
	@Override
	public ApiResultObject publishDomain(String serviceId) {
		ApiResultObject apiResult = RedisHttpClient.get(redisUrl + StringUtils.replace("/redis/domain/{}/publishDomain", "{}", String.valueOf(serviceId)));
		return apiResult;
	}
	
	@SuppressWarnings("unchecked")
	private void analyzeReCreateResult(ApiResultObject apiResult) {
		if(apiResult.getAnalyzeResult()) {
			Map<String, Object> resultMap = JSONObject.parseObject(apiResult.getResult(), Map.class);
			String serviceId = String.valueOf(resultMap.get("appId"));
			apiResult.setResult(serviceId);
		}
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
			boolean checkResult = quotaUserService.checkQuota(redis.getCreateUser(), Constant.QUOTA_REDIS_NAME, Constant.QUOTA_REDIS_TYPE, 1l);
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
	public void auditAndBuild(Long id, String auditInfo, Long auditUser) {
		Redis redis = this.selectById(id);
		if(redis.getAuditStatus()==AuditStatus.WAIT) {//待审核状态才进行创建
			auditAndBuild(redis, auditInfo, auditUser);
		} else {
			throw new ValidateException("状态无法审核");
		}
	}
	
	public void auditAndBuild(Redis redis, String auditInfo, Long auditUser) {
		if(LockUtil.getDistributedLock("createOrDeleteRedis")) {
			User u = userService.getUserById(redis.getCreateUser());
			//校验redis服务是否合法
			ApiResultObject nameCheckResult = this.checkNameExist(redis.getName());
			ApiResultObject canCreateCheckResult = this.checkCanCreate(Long.parseLong(redis.getAzId()), redis.getMemorySize());
			
			if(nameCheckResult.getAnalyzeResult() && canCreateCheckResult.getAnalyzeResult()) {//合法
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
				updateQuotaUser(redis.getCreateUser(), 1);
				
				//更新redis信息
				updateRedisAudit(redis, auditInfo, auditUser, AuditStatus.APPROVE);
			}
			
			LockUtil.releaseDistributedLock("createOrDeleteRedis");
		} else {
			throw new ValidateException("服务器正忙,请稍后重试!");
		}
		
	}
	
	private void updateQuotaUser(Long userId, Integer size) {
		List<QuotaUser> quotaUsers = quotaUserService.getUserQuotaByProductNameAndType(userId, Constant.QUOTA_REDIS_NAME, Constant.QUOTA_REDIS_TYPE);
		QuotaUser quotaUser = quotaUsers.get(0);
		quotaUser.setUsed(quotaUser.getUsed()+size);
		quotaUserService.updateBySelective(quotaUser);
	}
	
	@Override
	public void reject(Long id, String auditInfo, Long auditUser) {
		Redis redis = this.selectById(id);
		if(redis.getAuditStatus()==AuditStatus.WAIT) {//待审核状态才进行驳回
			updateRedisAudit(redis, auditInfo, auditUser, AuditStatus.REJECT);
		} else {
			throw new ValidateException("状态无法驳回");
		}
	}
	
	private void updateRedisAudit(Redis redis, String auditInfo, Long auditUser, AuditStatus auditStatus) {
		redis.setAuditInfo(auditInfo);
		redis.setAuditStatus(auditStatus);
		redis.setAuditUser(auditUser);
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

	@SuppressWarnings("unchecked")
	@Override
	public ApiResultObject sendUserEmail(Long id) {
		ApiResultObject ret = new ApiResultObject();
		try {
			Redis redis = this.selectById(id);
			User u = this.userService.getUserById(redis.getCreateUser());
			
			Map<String, Object> info = new HashMap<String, Object>();
			useRemoteService(redis, info);
			
			Map<String, Object> emailParams = new HashMap<String,Object>();
			emailParams.put("redisName", info.get("name"));
			emailParams.put("redisUrl", info.get("domain")==null ? "" : getRedisDomains((List<Map>)info.get("domain")));
			email4User(emailParams, u.getEmail(), "createRedis.ftl");
			ret.setAnalyzeResult(true);
		} catch (Exception e) {
			ret.setAnalyzeResult(false);
			logger.error(e.getMessage(), e);
		}
		return ret;
	}
	
	private String getRedisDomains(List<Map> list) {
		StringBuilder domains = new StringBuilder();
		for (Map map : list) {
			domains.append(map.get("domain")).append("<br>");
		}
		return domains.toString();
	}
	
	private void email4User(Map<String,Object> params, String email, String ftlName){
		MailMessage mailMessage = new MailMessage("乐视云平台Matrix系统", email, "乐视云平台Matrix系统通知", ftlName, params);
		mailMessage.setHtml(true);
		defaultEmailSender.sendMessage(mailMessage);
	}

	@Override
	public ApiResultObject deleteDbAndInstance(Long id) {
		ApiResultObject apiResult = new ApiResultObject();
		Redis r = this.selectById(id);
		if(LockUtil.getDistributedLock("createOrDeleteRedis")) {//获取到分布式锁
			deleteInstance(Long.parseLong(r.getServiceId()));
			delete(r);
			updateQuotaUser(r.getCreateUser(), -1);
			//释放锁
			LockUtil.releaseDistributedLock("createOrDeleteRedis");
			apiResult.setAnalyzeResult(true);
		} else {
			apiResult.setAnalyzeResult(false);
			apiResult.setResult("获取分布式锁失败");
		}
		return apiResult;
	}
	
	
}