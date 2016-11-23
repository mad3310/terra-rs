package com.le.matrix.redis.enumeration;

public enum AuditStatus implements IntEnum{
	WAIT(10),//待审核
	APPROVE(11),//审核通过
	REJECT(12);//审核驳回
	
	private final Integer value;
	
	private AuditStatus(Integer value)
	{
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
}
