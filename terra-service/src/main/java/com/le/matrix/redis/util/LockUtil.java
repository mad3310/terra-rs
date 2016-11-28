package com.le.matrix.redis.util;

import java.util.Calendar;
import java.util.Map;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.letv.common.util.RetryUtil;
import com.letv.common.util.function.IRetry;
import com.letv.mms.cache.ICacheService;
import com.letv.mms.cache.IMemcachedCache;
import com.letv.mms.cache.factory.CacheFactory;
import com.letv.mms.cache.impl.RemoteCacheServiceImpl;

public class LockUtil {
	
	private static ICacheService<?> cacheService = CacheFactory.getCache();
	private static IMemcachedCache memcachedCache = null;
	
	static {
		if(cacheService instanceof RemoteCacheServiceImpl) {
			memcachedCache = ((RemoteCacheServiceImpl)cacheService).getCache();
		}
	}
	
	public static boolean getDistributedLock(final String key) {
		if(null == memcachedCache) {
			return true;
		}
		
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 1);
        
        //1分钟之内获取锁,超过一分钟还未获取到,返回false
        Map<String, Object> ret = RetryUtil.retryByTime(new IRetry<Object, Boolean>() {
			
			@Override
			public Boolean judgeAnalyzeResult(Object o) {
				return (Boolean)o;
			}
			
			@Override
			public Object execute() {
				return memcachedCache.add(key, 1, cal.getTime());
			}
			
			@Override
			public Object analyzeResult(Object r) {
				return r;
			}
		}, 60*1000, 5*1000);
        
        return (boolean) ret.get("judgeAnalyzeResult");
    }
	
	public static void releaseDistributedLock(String key) {
		if(null != memcachedCache) {
        	memcachedCache.delete(key);
        }
	}
	
}
