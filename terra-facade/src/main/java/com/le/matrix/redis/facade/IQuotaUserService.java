package com.le.matrix.redis.facade;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.redis.model.QuotaUser;

@Path("userQuota")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface IQuotaUserService extends IBaseService<QuotaUser> {
	@DELETE
	@Path("{id : \\d+}")
	void deleteByPrimaryKey(@PathParam("id") Long id);
	
	/**
	 * 查询是否超过配额
	 * @param productName
	 * @param type
	 * @param value
	 * @return 未超过配置-true，超过配额-false
	 */
	@GET
	@Path("check")
	boolean checkQuota(@QueryParam("userId") Long userId, @QueryParam("productName") String productName, 
					   @QueryParam("type") String type, @QueryParam("value") Long value);
	
	@GET
	@Path("selective")
	List<QuotaUser> getUserQuotaByProductNameAndType(@QueryParam("userId") Long userId, @QueryParam("productName") String productName, 
			   @QueryParam("type") String type);
}