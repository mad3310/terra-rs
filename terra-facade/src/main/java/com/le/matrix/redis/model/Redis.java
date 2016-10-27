package com.le.matrix.redis.model;

import com.le.matrix.redis.enumeration.RedisType;
import com.letv.common.model.BaseModel;

public class Redis extends BaseModel {

	private static final long serialVersionUID = 5336795056773086076L;

	private String name;//名称

	private RedisType type;//类型

	private Integer memorySize;//缓存大小，单位G

	private String configId;//配置文件id

	private String idcId;//机房id

	private String clusterId;//集群id

	private String password;//密码

	private String serviceId;//服务id
	
	private String descn;//描述

	public Redis() {
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

	public String getIdcId() {
		return idcId;
	}

	public void setIdcId(String idcId) {
		this.idcId = idcId;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
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