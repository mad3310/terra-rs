package com.le.matrix.redis.service.impl;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.le.matrix.redis.facade.ILoginService;
import com.le.matrix.redis.facade.IOauthService;
import com.le.matrix.redis.facade.IUserService;
import com.le.matrix.redis.model.User;
import com.letv.common.session.Session;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.SessionUtil;
import com.letv.mms.cache.ICacheService;
import com.letv.mms.cache.factory.CacheFactory;

@Service("loginService")
public class LoginServiceImpl implements ILoginService {

	private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	private IUserService userService;

	@Autowired
	private IOauthService oauthService;

	@Autowired
	private SessionServiceImpl sessionService;
	
	@Value("${oauth.token.cache.expire}")
	public long OAUTH_TOKEN_CACHE_EXPIRE;

	private ICacheService<?> cacheService = CacheFactory.getCache();

	@Override
	public Session saveOrUpdateUserBySession(Session session) {
		if (null == session)
			return null;
		User user = this.userService.getUserByNameAndEmail(session.getUserName(), session.getEmail());
		Long userId;
		if (null == user) {
			userId = this.insertUser(session);
		} else {
			userId = user.getId();
			user.setOauthId(session.getOauthId());
			user.setLastLoginTime(new Date());
			this.userService.updateBySelective(user);
		}
		session.setUserId(userId);
		logger.info("logined successfully:{}", session.getUserName());
		return session;
	}

	public Long insertUser(Session session) {
		User userModel = new User();
		userModel.setOauthId(session.getOauthId());
		userModel.setEmail(session.getEmail());
		userModel.setUserName(session.getUserName());
		this.userService.insert(userModel);
		return userModel.getId();
	}

	@Override
	public Session login(String clientId, String clientSecret) throws AuthenticationException, IllegalArgumentException {

		Map<String, Object> oauthUser = this.oauthService.getUserInfo(
				clientId, clientSecret);
		if (null == oauthUser || oauthUser.isEmpty()) {
			return null;
		}
		String oauthId = (String) oauthUser.get("uuid");

		Session session = new Session();
		// use clinetId when user logout.
		session.setClientId(clientId);
		session.setClientSecret(clientSecret);
		session.setOauthId(oauthId);
		String username = (String) oauthUser.get("username");
		String email = (String) oauthUser.get("email");
		
		if(email.endsWith("le.com"))
			email = email.replace("le.com","letv.com");

		session.setUserName(username);
		session.setEmail(email);

		session = this.saveOrUpdateUserBySession(session);
		if (null != session)
			this.cacheService.set(oauthId, session, OAUTH_TOKEN_CACHE_EXPIRE);
		return session;
	}

	@Override
	public Session getUserByToken(String token) {
		if (StringUtils.isEmpty(token)) {
			return null;
		}
		String oauthId = SessionUtil.getUuidBySessionId(token);
		Session session = (Session) this.cacheService.get(oauthId, null);
		if (null == session) {
			User userModel = this.userService.selectByOauthId(oauthId);
			if (null == userModel)
				return null;
			session = createUserSession(userModel);
			session.setClientId(SessionUtil.getClientIdAndClientSecretBySessionId(token).getClient_id());
			session.setClientSecret(SessionUtil.getClientIdAndClientSecretBySessionId(token).getClient_secret());
			if (session != null)
				this.cacheService.set(oauthId, session, OAUTH_TOKEN_CACHE_EXPIRE);
		}
		return session;
	}

	private Session createUserSession(User user) {
		if (null == user)
			return null;
		Session session = new Session();
		session.setUserId(user.getId());
		session.setUserName(user.getUserName());
		session.setEmail(user.getEmail());
		session.setAdmin(user.isAdmin());
		session.setOauthId(user.getOauthId());
		return session;
	}
}