package org.yaen.starter.core.model.wechat.entities;

import org.yaen.starter.common.data.annotations.OneIndex;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;

import lombok.Getter;
import lombok.Setter;

/**
 * wechat platform message entity
 * <p>
 * all message from user or wechat system, to server, and server can response to user
 * <p>
 * group by msg type, if is event, then group by event type
 * <p>
 * for none-event messages, msgId should be unique(in one appid)
 * <p>
 * for event messages, FromUserName + CreateTime should be unique(in one appid)
 * 
 * @author Yaen 2016年7月11日下午2:03:46
 */

@Getter
@Setter
@OneTable(TableName = "WX_PLATFORM_COMPONENT_MESSAGE")
@OneUniqueIndex("ID")
@OneIndex({ "FROM_USER_NAME", "TO_USER_NAME", "MSG_TYPE,EVENT", "MSG_ID" })
public class PlatformComponentMessageEntity extends PlatformMessageEntity {
	private static final long serialVersionUID = -5430345924867020281L;

}
