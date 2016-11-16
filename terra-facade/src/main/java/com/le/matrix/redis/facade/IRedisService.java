package com.le.matrix.redis.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
	
	/**
	 * 获取redis服务所有配置信息
	 * @return
	 */
	@GET
	@Path("/region/config")
	ApiResultObject getReidsConfig();
	
	/**
	 * 获取redis服务所有配置信息
	 * @return
	 */
	@GET
	@Path("/region/config/{id}")
	ApiResultObject getReidsConfigById(@PathParam("id") Long id);
	
	/**
	 * 根据redis名称查询是否存在
	 * @return
	 */
	@POST
	@Path("/checkNameExist")
	ApiResultObject checkNameExist(@QueryParam("name") String name);
	
	/**
	 * 根据redis id查询运行状态
	 * @return
	 */
	@GET
	@Path("/{id}/status")
	ApiResultObject getStatusById(@PathParam("id") Long id);
	
	/**
	 * 根据redis id查询信息
	 * @return
	 */
	@GET
	@Path("/{id}")
	ApiResultObject getInfoById(@PathParam("id") Long id);
	
	/**
	 * 根据redis id下线实例
	 * @return
	 */
	@GET
	@Path("/{id}/offline")
	ApiResultObject offline(@PathParam("id") Long id);
	
	/**
	 * 根据redis id启动实例
	 * @return
	 */
	@GET
	@Path("/{id}/start")
	ApiResultObject start(@PathParam("id") Long id);
	
	/**
	 * 根据redis id启动实例
	 * @return
	 */
	@DELETE
	@Path("/{id}/delete")
	ApiResultObject deleteInstance(@PathParam("id") Long id);
	
	/**
	 * 根据redis信息创建实例
	 * @return
	 */
	@POST
	@Path("/create")
	ApiResultObject create(Redis redis);
}