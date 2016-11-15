package com.le.matrix.redis.util;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.le.matrix.redis.constant.Constant;
import com.le.matrix.redis.model.RESTfulResult;
import com.letv.common.result.ApiResultObject;

public class RedisHttpClient {
	
	private final static Logger logger = LoggerFactory.getLogger(RedisHttpClient.class);
	
	@Value("${redis.name}")
	private static String redisName;
	@Value("${redis.token}")
	private static String redisToken;
	
	public static ApiResultObject postObject(String url, Object obj) {
		RESTfulResult restfulResult = StatusHttpClient.postObject(url, obj, Constant.CONNECTION_TIMEOUT, 
				Constant.SO_TIMEOUT, getHttpHeader());
		return analyzeHttpStatusRESTfulResult(restfulResult);
	}
	
	public static ApiResultObject get(String url) {
		RESTfulResult restfulResult = StatusHttpClient.get(url, Constant.CONNECTION_TIMEOUT, 
				Constant.SO_TIMEOUT, getHttpHeader());
		return analyzeHttpStatusRESTfulResult(restfulResult);
	}
	
	private static Map<String, String> getHttpHeader() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("name", redisName);
		headers.put("token", redisToken);
		return headers;
	}
	
	public static ApiResultObject analyzeHttpStatusRESTfulResult(RESTfulResult result) {
		logger.debug("Analyze RESTfulResult:{}", result);
		ApiResultObject aipResultObject = new ApiResultObject();
		
		if(result == null){
			aipResultObject.setAnalyzeResult(false);
			aipResultObject.setResult("result is null");
		} else if(result.getStatus() < 200 || result.getStatus() >= 300) {//http状态码检查
			aipResultObject.setAnalyzeResult(false);
			aipResultObject.setResult(result.getBody());
		} else {//业务检查
			redisResultCheck(result, aipResultObject);
		}
		aipResultObject.setUrl(result.getUrl());
		
		return aipResultObject;
	}
	
	@SuppressWarnings("unchecked")
	private static void redisResultCheck(RESTfulResult result, ApiResultObject aipResultObject) {
		Map<String, Object> resultMap = JSONObject.parseObject(result.getBody(), Map.class);
		
		//业务逻辑检查,返回的code=200时,任务业务逻辑成功
		if(HttpStatus.OK.value() == (Integer)resultMap.get("code")) {
			aipResultObject.setAnalyzeResult(true);
			aipResultObject.setResult(((JSONArray)resultMap.get("data")).toJSONString());
		} else {
			aipResultObject.setAnalyzeResult(false);
			aipResultObject.setResult((String)resultMap.get("message"));
		}
	}
}
