package com.le.matrix.redis.model;

import java.util.Date;

import com.le.matrix.redis.enumeration.Status;
import com.le.matrix.redis.enumeration.RedisType;
import com.letv.common.model.BaseModel;

public class Redis extends BaseModel {

	private static final long serialVersionUID = 5336795056773086076L;

	private String name;//名称

	private RedisType type;//类型

	private Integer memorySize;//缓存大小，单位G

	private String regionId;//地域id
	
	private String configId;//配置文件id

	private String azId;//可用区id

	private String password;//密码

	private String serviceId;//服务id
	
	private Status status;//状态：1-待审核，2-审核通过，3-驳回，4-创建中，5-创建失败，6-启动中，7-启动失败，8-运行中，9-停止中，10-已停止，11-危险，12-宕机，13-集群删除中，14-集群已删除，15-集群删除失败
	
	private String auditInfo;//审核信息
	
	private Date auditTime;//审核时间
	
	private Long auditUser;//审核人
	
	private String descn;//描述

	public Redis() {
	}

	
	public String getRegionId() {
		return regionId;
	}


	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}

	public Status getStatus() {
		return status;
	}


	public void setStatus(Status status) {
		this.status = status;
	}


	public String getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(String auditInfo) {
		this.auditInfo = auditInfo;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}


	public Long getAuditUser() {
		return auditUser;
	}


	public void setAuditUser(Long auditUser) {
		this.auditUser = auditUser;
	}


	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RedisType getType() {
		return type;
	}

	public void setType(RedisType type) {
		this.type = type;
	}

	public Integer getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(Integer memorySize) {
		this.memorySize = memorySize;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getAzId() {
		return azId;
	}

	public void setAzId(String azId) {
		this.azId = azId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}