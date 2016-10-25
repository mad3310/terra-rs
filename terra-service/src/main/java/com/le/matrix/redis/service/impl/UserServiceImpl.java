package com.le.matrix.redis.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.le.matrix.redis.dao.IUserDao;
import com.le.matrix.redis.facade.IUserService;
import com.le.matrix.redis.model.User;
import com.letv.common.dao.IBaseDao;
import com.letv.common.exception.CommonException;
import com.letv.common.exception.ValidateException;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements IUserService{
	
	@Autowired
	private IUserDao userDao;
	
	public UserServiceImpl() {
		super(User.class);
	}

	public void validationForSaving(User checkingUser) {
		String userName = checkingUser.getUserName();
		boolean exist = existUserByUserName(userName);
		if(exist)
			throw new ValidateException("用户已存在！");
	}
	
	@Override
	public void saveUserObject(User user) {
		this.createUserObject(user);
		try {
			super.insert(user);
		} catch (Exception e) {
			throw new CommonException("保存用户时出现异常",e);
		}
	}
	
	private void createUserObject(User user)
	{		
		Date date = new Date();
		user.setRegisterDate(date);
		user.setLastLoginTime(date);
		user.setLastLoginIp(user.getCurrentLoginIp());
		user.setDeleted(true);
		//TODO re-factor
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		user.setCreateUser(Long.valueOf(0L));
		user.setCreateTime(timestamp);
		user.setUpdateUser(Long.valueOf(0L));
		user.setUpdateTime(timestamp);
	}
	
	
	public User getUserById(Long userid)
	{
		User user = super.selectById(userid);
		if(null == user)
			throw new CommonException("User不存在，user id："+userid);
		return user;
	}
	
	public User getUserByName(String userName){
		userName = StringUtils.lowerCase(userName);
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName",userName);
		List<User> users = selectByMap(map);
		
		if(null == users || users.isEmpty())
			return null;
		
		return users.get(0);
	}
	
	@Override
	public User saveUserObjectWithSpecialName(String userName,String loginIp,String email) {
		User user = new User();
		user.setUserName(userName);
		user.setCurrentLoginIp(loginIp);
		user.setEmail(email);
		saveUserObject(user);
		return user;
	}
	
	public boolean existUserByUserName(String userName)
	{
		userName=StringUtils.lowerCase(userName);
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName", userName);
		int userNameCount = selectByMapCount(map);
		if (userNameCount > 0){
			return true;
		}
		return false;
	}

	@Override
	public void updateUserLoginInfo(User user,String currentLoginIp) {
		if(null == user)
			throw new CommonException("User object is not null");
		
		Date lastLoginTime = user.getCurrentLoginTime();
		user.setLastLoginIp(user.getCurrentLoginIp());
		user.setLastLoginTime(lastLoginTime);
		user.setCurrentLoginIp(currentLoginIp);
		user.setCurrentLoginTime(new Date());
		
		super.update(user);
	}

	@Override
	public IBaseDao<User> getDao() {
		return userDao;
	}

	@Override
	public User getUserByNameAndEmail(String userNamePassport, String email) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("userName",userNamePassport);
		map.put("email",email);
		List<User> users = selectByMap(map);
		if(null == users || users.isEmpty())
			return null;
		
		return users.get(0);
	}

	@Override
	public User selectByOauthId(String oauthId) {
        List<User> users = this.userDao.selectByOauthId(oauthId);
        if(null == users || users.isEmpty())
            return null;
        return users.get(0);
    }
  
}