package com.le.matrix.template.facade;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.le.matrix.template.model.User;

import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by linzhanbo on 2016/10/11.
 */
//REST路径    更多数据校验注解：完全符合JAX-RS 2.0和Hibernate Validate校验扩展
@Path("user")
//标注可接受请求的MIME媒体类型
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
//标注返回的MIME媒体类型
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public interface UserFacade {
    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    @DELETE
    @Path("{id : \\d+}")
    int deleteByPrimaryKey(Integer id);

    /**
     * 添加用户
     *
     * @param record
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    int insert(/*@Valid*/ User record);

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    @GET
//    @Path("{id : \\d+}")
    @Path("{id}")
    User selectByPrimaryKey(@PathParam("id") @Min(value = 1L, message = "User ID must be greater than 1") Integer id);

    /**
     * 修改用户
     *
     * @param record
     * @return
     */
    @PUT
    int updateByPrimaryKeySelective(/*@Valid*/ User record);
}
