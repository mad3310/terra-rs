package com.le.matrix.redis.enumeration;

public enum RedisType implements IntEnum{
	RedisCluster(1);
	
	private final Integer value;
	
	private RedisType(Integer value)
	{
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
}
