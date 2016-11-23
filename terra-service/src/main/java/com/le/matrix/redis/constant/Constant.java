package com.le.matrix.redis.constant;

public class Constant {

	public final static String PYTHON_API_RESPONSE_SUCCESS = "200";

	public final static String PYTHON_API_RESULT_SUCCESS = "000000";
	
	//http请求连接超时时间
	public final static int CONNECTION_TIMEOUT = 5000;
	//http请求等待超时时间
	public final static int SO_TIMEOUT = 10000;
	//查询总时长
	public final static long CHECK_TOTAL_TIME = 180000;
	//查询间隔
	public final static long CHECK_INTERVAL_TIME = 3000;
	//restful结果标志
	public final static String RESTFUL_RESULT_MARK = "true";
	
	//配额redis名称
	public static final String QUOTA_REDIS_NAME = "REDIS";
	//配额redis 类型
	public static final String QUOTA_REDIS_TYPE = "num";
	
	public static String REDIS_NAME="matrix";
	public static String REDIS_TOKEN="21b72c0b7adc5c7b4a50ffcb90d92dd6";

}
