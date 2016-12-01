package com.le.matrix.redis.enumeration;

public enum Status implements IntEnum{
	UNKNOWN(0),//未知
	PENDING(1),//待审核
	APPROVE(2),//审核通过
	REJECT(3),//审核驳回
	
    BUILDDING(4),//创建中
	BUILDFAIL(5),//创建失败
	STARTING(6),//启动中
	STARTFAIL(7),//启动失败
    RUNNING(8),//运行中
	STOPPING(9),//停止中
	STOPED(10),//已停止
	DANGER(11),//危险，集群中部分节点宕机
	CRISIS(12),//宕机，集群不可用
	DESTROYING(13),//集群删除中
	DESTROYED(14),//集群已删除
	DESTROYFAILED(15);//集群删除失败
	
	private final Integer value;
	
	private Status(Integer value)
	{
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
}
