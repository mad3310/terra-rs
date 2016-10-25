package com.le.matrix.redis.dao;

import java.util.List;

import com.le.matrix.redis.model.User;
import com.letv.common.dao.IBaseDao;

public interface IUserDao extends IBaseDao<User> {
	List<User> selectByOauthId(String oauthId);
}