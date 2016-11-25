package com.le.matrix.redis.facade;

import java.util.Map;

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
	 * 根据id获取region
	 * @return
	 */
	@GET
	@Path("/region/{regionId}")
	ApiResultObject getRegionByRegionId(@PathParam("regionId") Long regionId);
	
	/**
	 * 根据id获取可用区
	 * @return
	 */
	@GET
	@Path("/az/{azId}")
	ApiResultObject getAzByAzId(@PathParam("azId") Long azId);
	
	/**
	 * 获取redis服务某个region下的可用区
	 * @return
	 */
	@GET
	@Path("/region/{regionId}/az")
	ApiResultObject getReidsRegionAz(@PathParam("regionId") Long regionId);
	
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
	 * 检查可用区是否可以创建
	 * @return
	 */
	@POST
	@Path("/checkCanCreate")
	ApiResultObject checkCanCreate(@QueryParam("azId") Long azId, @QueryParam("memorySize") Integer memorySize);
	
	/**
	 * 根据redis id查询运行状态
	 * @return
	 */
	@GET
	@Path("/{id}/status")
	ApiResultObject getStatusById(@PathParam("id") Long id);
	
	/**
	 * 根据redis id查询运行状态并分析结果（工作流使用）
	 * @return
	 */
	@GET
	@Path("/{id}/status/analyse")
	ApiResultObject getStatusWithAnalyse(@PathParam("id") Long id);
	
	/**
	 * 根据redis id查询信息
	 * @return
	 */
	@GET
	@Path("/instance/{id}")
	ApiResultObject getInstanceByServiceId(@PathParam("id") Long id);
	
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
	ApiResultObject createInstance(Map<String, String> params);
	
	/**
	 * 根据serviceId创建域名
	 * @return
	 */
	@POST
	@Path("/createDomain")
	ApiResultObject createDomain(@QueryParam("serviceId") String serviceId);
	
	/**
	 * 根据serviceId发布域名
	 * @return
	 */
	@POST
	@Path("/publishDomain")
	ApiResultObject publishDomain(@QueryParam("serviceId") String serviceId);
	
	/**
	 * 检查并调用工作流
	 * @param redis
	 * @return
	 */
	@POST
	@Path("/build")
	void build(Redis redis);
	
	/**
	 * 审核并调用工作流
	 * @param redisId
	 * @param auditInfo
	 * @param auditUser
	 */
	@POST
	@Path("/audit")
	void auditAndBuild(@QueryParam("redisId") Long redisId, @QueryParam("auditInfo") String auditInfo, @QueryParam("auditUser") Long auditUser);
	
	/**
	 * 驳回
	 * @param redis
	 * @param auditInfo
	 * @param auditUser
	 */
	@POST
	@Path("/reject")
	void reject(@QueryParam("redisId") Long redisId, @QueryParam("auditInfo") String auditInfo, @QueryParam("auditUser") Long auditUser);
	
	@GET
	@Path("/instance")
	ApiResultObject updateServiceIdById(@QueryParam("serviceId") String serviceId, @QueryParam("id") Long id);
	
	@GET
	@Path("/email")
	ApiResultObject sendUserEmail(@QueryParam("id") Long id);
}