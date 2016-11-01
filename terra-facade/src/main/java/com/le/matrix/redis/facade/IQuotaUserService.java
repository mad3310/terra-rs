package com.le.matrix.redis.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.redis.model.QuotaUser;

@Path("userQuota")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface IQuotaUserService extends IBaseService<QuotaUser> {
	@DELETE
	@Path("{id : \\d+}")
	void deleteByPrimaryKey(@PathParam("productName") Long id);
}