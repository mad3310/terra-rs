/**
 *
 *  Copyright (c) 2016 乐视云计算有限公司（lecloud.com）. All rights reserved
 *
 */
package com.le.matrix.redis.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.le.matrix.redis.service.impl.DealExceptionServiceImpl;

/**
 * 异常处理
 * @author lisuxiao.
 * @since 2016年10月21日, 上午10:41:50 .
 * @version 1.0 .
 */
public class DealExceptionFilter implements Filter{
   
	private Logger logger = LoggerFactory.getLogger(DealExceptionFilter.class);
	
	private DealExceptionServiceImpl dealExceptionService;

	public void setDealExceptionService(DealExceptionServiceImpl dealExceptionService) {
		this.dealExceptionService = dealExceptionService;
	}


	public Result invoke(Invoker<?> invoker, Invocation invocation)
			throws RpcException {
		Result result = invoker.invoke(invocation);
		if(result.hasException()) {
			Throwable t = result.getException();
			dealExceptionService.sendExceptionEmail((Exception)t);
		}
		return result;
	}
}
