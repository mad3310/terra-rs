package com.le.matrix.redis.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.redis.model.Redis;
import com.letv.common.result.ApiResultObject;

@Path("redis")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface IRedisService extends IBaseService<Redis> {
	
	/**
	 * 获取redis服务所有region
	 * @return
	 */
	@GET
	@Path("/region/list")
	ApiResultObject getReidsRegion();
}