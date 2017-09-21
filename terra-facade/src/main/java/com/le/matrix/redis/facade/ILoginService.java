package com.le.matrix.redis.facade;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.http.auth.AuthenticationException;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.letv.common.session.Session;

@Path("loginService")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface ILoginService {
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})//指定saveOrUpdateUserBySession接收JSON格式的数据。REST框架会自动将JSON数据反序列化为Session对象
	Session saveOrUpdateUserBySession(Session session);

	@POST
	@Path("login")
	Session login(@QueryParam("clientId") String clientId, @QueryParam("clientSecret") String clientSecret) throws AuthenticationException, IllegalArgumentException;

	@GET
	Session getUserByToken(@QueryParam("token") String token);
}