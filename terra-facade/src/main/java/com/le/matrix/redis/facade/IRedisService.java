package com.le.matrix.redis.facade;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.redis.model.Redis;
import com.letv.common.paging.impl.Page;
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
	 * 根据configId获取配置信息
	 * @return
	 */
	@GET
	@Path("/region/config/{configId}")
	ApiResultObject getReidsConfigByConfigId(@PathParam("configId") Long configId);
	
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
	 * 根据redis serviceId查询运行状态
	 * @return
	 */
	@GET
	@Path("/{serviceId}/status")
	ApiResultObject getStatusByServiceId(@PathParam("serviceId") Long serviceId);
	
	/**
	 * 根据redis serviceId查询运行状态并分析结果（工作流使用）
	 * @return
	 */
	@GET
	@Path("/{serviceId}/status/analyse")
	ApiResultObject getStatusWithAnalyse(@PathParam("serviceId") Long serviceId);
	
	/**
	 * 根据redis serviceId查询信息
	 * @return
	 */
	@GET
	@Path("/instance/{serviceId}")
	ApiResultObject getInstanceByServiceId(@PathParam("serviceId") Long serviceId);
	
	/**
	 * 根据redis id查询信息(信息组合)
	 * @return
	 */
	@GET
	@Path("/group/{id}")
	ApiResultObject getRedisById(@PathParam("id") Long id);
	
	/**
	 * 根据redis id下线实例
	 * @return
	 */
	@GET
	@Path("/{serviceId}/offline")
	ApiResultObject offline(@PathParam("serviceId") Long serviceId);
	
	/**
	 * 根据redis serviceId启动实例
	 * @return
	 */
	@GET
	@Path("/{serviceId}/start")
	ApiResultObject start(@PathParam("serviceId") Long serviceId);
	
	/**
	 * 根据redis serviceId启动实例
	 * @return
	 */
	@DELETE
	@Path("/{id}/deleteDbAndInstance")
	ApiResultObject deleteDbAndInstance(@PathParam("id") Long id);
	
	/**
	 * 根据redis serviceId删除实例
	 * @return
	 */
	@DELETE
	@Path("/{serviceId}/delete")
	ApiResultObject deleteInstance(@PathParam("serviceId") Long serviceId);
	
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
	@GET
	@Path("/createDomain")
	ApiResultObject createDomain(@QueryParam("serviceId") String serviceId);
	
	/**
	 * 根据serviceId发布域名
	 * @return
	 */
	@GET
	@Path("/publishDomain")
	ApiResultObject publishDomain(@QueryParam("serviceId") String serviceId);
	
	/**
	 * 检查并调用工作流
	 * @param redis
	 * @return
	 */
	@POST
	@Path("/build")
	ApiResultObject build(Redis redis);
	
	/**
	 * 审核并调用工作流
	 * @param redisId
	 * @param auditInfo
	 * @param auditUser
	 */
	@POST
	@Path("/audit")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	ApiResultObject auditAndBuild(@FormParam("id") Long id, @FormParam("auditInfo") String auditInfo, @FormParam("auditUser") Long auditUser);
	
	/**
	 * 驳回
	 * @param redis
	 * @param auditInfo
	 * @param auditUser
	 */
	@POST
	@Path("/reject")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	ApiResultObject reject(@FormParam("id") Long id, @FormParam("auditInfo") String auditInfo, @FormParam("auditUser") Long auditUser);
	
	/**
	 * 更新redis信息（serviceId）
	 * @param serviceId
	 * @param id
	 * @return
	 */
	@GET
	@Path("/instance")
	ApiResultObject updateServiceIdById(@QueryParam("serviceId") String serviceId, @QueryParam("id") Long id);
	
	/**
	 * 给用户发送成功邮件（工作流调用）
	 * @param id
	 * @return
	 */
	@GET
	@Path("/email")
	ApiResultObject sendUserEmail(@QueryParam("id") Long id);
	
	@POST
	@Path("/pending")
	ApiResultObject queryPendingRedis(Page p);
	
	
}