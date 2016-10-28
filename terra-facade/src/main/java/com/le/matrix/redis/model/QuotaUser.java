package com.le.matrix.redis.model;

import com.letv.common.model.BaseModel;

public class QuotaUser extends BaseModel {

	private static final long serialVersionUID = -9191865013086288652L;

	private Long used;//使用量

    private QuotaBase QuotaBase;//基础配额

    private String descn;

	public Long getUsed() {
		return used;
	}

	public void setUsed(Long used) {
		this.used = used;
	}

	public QuotaBase getQuotaBase() {
		return QuotaBase;
	}

	public void setQuotaBase(QuotaBase quotaBase) {
		QuotaBase = quotaBase;
	}

	public String getDescn() {
		return descn;
	}

	public void setDescn(String descn) {
		this.descn = descn;
	}

}