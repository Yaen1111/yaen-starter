package org.yaen.starter.core.model.wechat.models;

import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.exceptions.DataNotExistsException;
import org.yaen.starter.common.data.exceptions.DuplicateDataException;
import org.yaen.starter.common.data.exceptions.NoDataAffectedException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.DateUtil;
import org.yaen.starter.core.model.models.TwoModel;
import org.yaen.starter.core.model.services.ProxyService;
import org.yaen.starter.core.model.wechat.entities.PlatformMessageEntity;
import org.yaen.starter.core.model.wechat.entities.UserEntity;
import org.yaen.starter.core.model.wechat.enums.EventTypes;

/**
 * the wechat user model, mostly for openid + appid
 * 
 * @author Yaen 2016年6月11日下午3:27:53
 */
public class UserModel extends TwoModel<UserEntity> {

	/**
	 * constructor
	 * 
	 * @param proxy
	 */
	public UserModel(ProxyService proxy) {
		super(proxy, new UserEntity());
	}

	/**
	 * load user by openid + appid
	 * 
	 * @param openId
	 * @param appId
	 * @throws CoreException
	 * @throws DuplicateDataException
	 * @throws DataNotExistsException
	 */
	public void loadByOpenId(String openId, String appId)
			throws CoreException, DataNotExistsException, DuplicateDataException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		// clear first
		this.clear();

		// get user by openid + appid
		try {
			this.entity = this.proxy.getQueryService().selectOneByUniqueFields(new UserEntity(openId, appId),
					new String[] { "openId", "appId" });
		} catch (CommonException ex) {
			throw new CoreException("load user failed", ex);
		}
	}

	/**
	 * create new by openid+appid
	 * 
	 * @param openId
	 * @param appId
	 * @throws CoreException
	 * @throws DuplicateDataException
	 */
	public void createNewByOpenId(String openId, String appId) throws CoreException, DuplicateDataException {
		AssertUtil.notBlank(openId);
		AssertUtil.notBlank(appId);

		try {
			// create new entity
			UserEntity user = new UserEntity(openId, appId);

			// do insert
			this.proxy.getEntityService().insertEntityByRowid(user);

		} catch (NoDataAffectedException ex) {
			throw new CoreException("create model failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("create model failed", ex);
		}
	}

	/**
	 * subscribe, here should be subscribed, maybe re-subscribe
	 * 
	 * @throws CoreException
	 */
	public void subscribe() throws CoreException {
		this.check();

		try {
			// update user info, create new event log
			this.entity.setSubscribeTime(DateUtil.getNow());
			this.entity.setUnsubscribeTime(null);

			this.proxy.getEntityService().updateEntityByRowid(this.entity);

			// create event log
			PlatformMessageEntity message = new PlatformMessageEntity();
			message.setId(this.entity.getId());
			message.setEventType(EventTypes.EVENT_TYPE_SUBSCRIBE);
			message.setEventTime(DateUtil.getNow());

			this.proxy.getEntityService().insertEntityByRowid(ev);

		} catch (NoDataAffectedException ex) {
			throw new CoreException("subscribe failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("subscribe failed", ex);
		} catch (DuplicateDataException ex) {
			throw new CoreException("subscribe failed", ex);
		}
	}

	/**
	 * unsubscribe, just update user info
	 * 
	 * @throws CoreException
	 */
	public void unsubscribe() throws CoreException {
		this.check();

		try {
			// update user info, create new event log
			this.entity.setUnsubscribeTime(DateUtil.getNow());

			this.proxy.getEntityService().updateEntityByRowid(this.entity);

			// create event log
			UserEventLogEntity ev = new UserEventLogEntity();
			ev.setId(this.entity.getId());
			ev.setEventType(EventTypes.EVENT_TYPE_UNSUBSCRIBE);
			ev.setEventTime(DateUtil.getNow());

			this.proxy.getEntityService().insertEntityByRowid(ev);

		} catch (NoDataAffectedException ex) {
			throw new CoreException("unsubscribe failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("unsubscribe failed", ex);
		} catch (DuplicateDataException ex) {
			throw new CoreException("unsubscribe failed", ex);
		}
	}

	/**
	 * click menu
	 * 
	 * @throws CoreException
	 */
	public void clickMenu() throws CoreException {
		this.check();

		try {
			// create event log
			UserEventLogEntity ev = new UserEventLogEntity();
			ev.setId(this.entity.getId());
			ev.setEventType(EventTypes.EVENT_TYPE_CLICK);
			ev.setEventTime(DateUtil.getNow());

			this.proxy.getEntityService().insertEntityByRowid(ev);

		} catch (NoDataAffectedException ex) {
			throw new CoreException("clickMenu failed", ex);
		} catch (CommonException ex) {
			throw new CoreException("clickMenu failed", ex);
		} catch (DuplicateDataException ex) {
			throw new CoreException("clickMenu failed", ex);
		}
	}
}