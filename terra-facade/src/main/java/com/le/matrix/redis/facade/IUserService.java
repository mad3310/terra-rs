package com.le.matrix.redis.facade;

import com.le.matrix.redis.model.User;

public interface IUserService extends IBaseService<User> {

	public void saveUserObject(User user);

	public User saveUserObjectWithSpecialName(String userName,
			String loginIp, String email);

	public User getUserByName(String userName);

	public void updateUserLoginInfo(User user, String currentLoginIp);

	public boolean existUserByUserName(String userName);

	public User getUserById(Long userId);

	public User getUserByNameAndEmail(String userNamePassport, String email);

	public User selectByOauthId(String oauthId);
}