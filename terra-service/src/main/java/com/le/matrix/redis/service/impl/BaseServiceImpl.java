package com.le.matrix.redis.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.le.matrix.redis.facade.IBaseService;
import com.letv.common.dao.IBaseDao;
import com.letv.common.dao.QueryParam;
import com.letv.common.exception.CommonException;
import com.letv.common.exception.ValidateException;
import com.letv.common.model.ISoftDelete;
import com.letv.common.paging.impl.Page;
import com.letv.common.session.Session;
import com.letv.common.session.SessionServiceImpl;
import com.letv.common.util.BeanUtil;

/**
 * <p>
 * </p>
 * 
 * @author xufei1 <xufei1@letv.com> Create at:2012-11-22 上午11:02:50
 * @param <T>
 */
public abstract class BaseServiceImpl<T> implements IBaseService<T>{
	
	private final static Logger logger = LoggerFactory.getLogger(BaseServiceImpl.class);
	
	private static final String FIELD_CREATED_BY = "createdBy";
	private static final String FIELD_CREATED_ON = "createdOn";
	private static final String FIELD_UPDATED_BY = "updatedBy";
	private static final String FIELD_UPDATED_ON = "updatedOn";
	private static final String FIELD_USER_INFO_ID = "userInfoId";
	
	protected final Class<T> entityClass;
	
	private final boolean isSoftDelete;
	
	private final boolean hasUserInfoId;
	
	private final boolean hasCreateBy;
	
	private final boolean hasCreatedOn;
	
	private final boolean hasUpdatedBy;
	
	private final boolean hasUpdatedOn;
	
	@Autowired
	private SessionServiceImpl sessionService;
	
	public BaseServiceImpl(Class<T> entityClass)
	{
		this.entityClass = entityClass;
		
		isSoftDelete = ISoftDelete.class.isAssignableFrom(this.entityClass);
		hasUserInfoId = BeanUtil.hasField(this.entityClass, FIELD_USER_INFO_ID);
		
		hasCreateBy = BeanUtil.hasField(this.entityClass, FIELD_CREATED_BY);
		hasCreatedOn = BeanUtil.hasField(this.entityClass, FIELD_CREATED_ON);
		hasUpdatedBy = BeanUtil.hasField(this.entityClass, FIELD_UPDATED_BY);
		hasUpdatedOn = BeanUtil.hasField(this.entityClass, FIELD_UPDATED_ON);
	}
	
	protected void abstractPreSaveObject(T o, boolean isNew) {
		initSystemProperties(o, isNew);
	}
	
	private final void initSystemProperties(T o, boolean isNew) {
		final Timestamp timestamp = new Timestamp(System.currentTimeMillis());

		try {
			Session userSession = sessionService.getSession();

			if (userSession == null) {
				logger.warn("UserSession is null for new/updated object " + o);
			}

			if (isNew) {
				if (userSession != null) {
					if (hasUserInfoId)
						PropertyUtils.setProperty(o, FIELD_USER_INFO_ID, userSession.getUserId());
					
					if (hasCreateBy && PropertyUtils.getProperty(o, FIELD_CREATED_BY) == null)
						PropertyUtils.setProperty(o, FIELD_CREATED_BY, userSession.getUserId());

					if (hasUpdatedBy && PropertyUtils.getProperty(o, FIELD_UPDATED_BY) == null)
						PropertyUtils.setProperty(o, FIELD_UPDATED_BY, userSession.getUserId());

					if (hasCreatedOn && PropertyUtils.getProperty(o, FIELD_CREATED_ON) == null)
						PropertyUtils.setProperty(o, FIELD_CREATED_ON, timestamp);
	
					if (hasUpdatedOn && PropertyUtils.getProperty(o, FIELD_UPDATED_ON) == null)
						PropertyUtils.setProperty(o, FIELD_UPDATED_ON, timestamp);
				}

			}
			else {
				if (userSession != null && hasUpdatedBy) {
					PropertyUtils.setProperty(o, FIELD_UPDATED_BY, userSession.getUserId());
					PropertyUtils.setProperty(o, FIELD_UPDATED_ON, timestamp);
				}
			}
		}
		catch (NoSuchMethodException e) {
			throw new CommonException(e);
		}
		catch (InvocationTargetException e) {
			throw new CommonException(e);
		}
		catch (IllegalAccessException e) {
			throw new CommonException(e);
		}
	}

	@Override
	public void insert(T t) {
		abstractPreSaveObject(t, true);
		getDao().insert(t);
	}

	@Override
	public void update(T t) {
		abstractPreSaveObject(t, false);
		getDao().update(t);
	}

	@Override
	public void updateBySelective(T t) {
		abstractPreSaveObject(t, false);
		getDao().updateBySelective(t);
	}

	@Override
	public void delete(T t) {
		if (null == t) 
			throw new ValidateException("待删除数据不可为空！");
		
		if (ISoftDelete.class.isAssignableFrom(entityClass)) {
			ISoftDelete softDeleteObject = (ISoftDelete) t;
			if (!softDeleteObject.isDeleted())
				softDeleteObject.setDeleted(true);
		}
		
		getDao().delete(t);
	}

	@Override
	public T selectById(Long id) {
		if (null == id) 
			throw new ValidateException("select操作中，id不可为空！");
		
		T t = getDao().selectById(id);
		return t;
	}

	@Override
	public Integer selectByModelCount(T t) {
		if (null == t) 
			throw new ValidateException("待删除数据不可为空！");
		
		if (ISoftDelete.class.isAssignableFrom(entityClass)) {
			ISoftDelete softDeleteObject = (ISoftDelete) t;
			softDeleteObject.setDeleted(false);
		}
		
		Integer countNum = getDao().selectByModelCount(t);
		return countNum;
	}

	@Override
	public <K,V> Integer selectByMapCount(Map<K,V> map) {
		Integer countNum = getDao().selectByMapCount(map);
		return countNum;
	}

	@Override
	public List<T> selectByModel(T t) {
		if (null == t) 
			throw new ValidateException("待删除数据不可为空！");
		
		if (ISoftDelete.class.isAssignableFrom(entityClass)) {
			ISoftDelete softDeleteObject = (ISoftDelete) t;
			softDeleteObject.setDeleted(false);
		}
		
		List<T> lists = getDao().selectByModel(t);
		return lists;
	}

	@Override
	public <K,V> List<T> selectByMap(Map<K,V> map) {
		List<T> lists = getDao().selectByMap(map);
		return lists;
	}
	
	@Override
	public final boolean hasSoftDelete() {
		return isSoftDelete;
	}
	
	@Override
	public <K, V> Page selectPageByParams(Page page, Map<K,V> params,String orderBy,Boolean isAsc) {
		QueryParam param = new QueryParam();
		param.setPage(page);
		param.setParams(params);
		page.setData(getDao().selectPageByMap(param));
		page.setTotalRecords(getDao().selectByMapCount(params));
		return page;
	}
	@Override
	public <K, V> Page selectPageByParams(Page page, Map<K,V> params) {
		QueryParam param = new QueryParam();
		param.setPage(page);
		param.setParams(params);
		page.setData(getDao().selectPageByMap(param));
		page.setTotalRecords(getDao().selectByMapCount(params));
		return page;
	}
	
	@Override
	public <K, V> Page queryByPagination(Page page, Map<K, V> params) {
		QueryParam param = new QueryParam();
		param.setPage(page);
		param.setParams(params);
		page.setData(getDao().selectPageByMap(param));
		page.setTotalRecords(getDao().selectByMapCount(param));
		return page;
	}
	
	public abstract IBaseDao<T> getDao();
}