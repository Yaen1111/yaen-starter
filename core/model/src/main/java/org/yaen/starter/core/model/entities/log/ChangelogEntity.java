package org.yaen.starter.core.model.entities.log;

import org.yaen.starter.common.dal.entities.AnotherEntity;
import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTableHandler;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.enums.SqlTypes;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.common.util.utils.StringUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * change log of entity, save before and after value
 * 
 * @author Yaen 2016年1月4日下午8:40:03
 */
@Getter
@Setter
public class ChangelogEntity extends TwoEntity implements OneTableHandler {
	private static final long serialVersionUID = -606829448983274029L;

	/** the sql type, see SqlTypes */
	@OneData(DataType = DataTypes.VARCHAR32, FieldName = "SQL_TYPE")
	private String sqlType;

	/** the update user name */
	@OneData(DataType = DataTypes.VARCHAR32, FieldName = "SYS_UUSER")
	private String uUser;

	/** the update comment */
	@OneData(DataType = DataTypes.VARCHAR250, FieldName = "SYS_UCOMMENT")
	private String uComment;

	/** the entity before change, if create, null */
	@OneCopy(Prefix = "BEFORE_")
	private BaseEntity beforeEntity;

	/** the entity after change, if delete, null */
	@OneCopy(Prefix = "AFTER_")
	private BaseEntity afterEntity;

	/**
	 * construct a changelog entity, use deep copy for before
	 * 
	 * @param sqlType
	 * @param beforeEntity
	 * @param afterEntity
	 * @throws CommonException
	 */
	public ChangelogEntity(String sqlType, BaseEntity beforeEntity, BaseEntity afterEntity) throws CommonException {
		super();

		// check param by type
		switch (sqlType) {
		case SqlTypes.INSERT:
			AssertUtil.notNull(afterEntity);
			break;
		case SqlTypes.DELETE:
			AssertUtil.notNull(beforeEntity);
			break;
		case SqlTypes.UPDATE:
			AssertUtil.notNull(beforeEntity);
			AssertUtil.notNull(afterEntity);
			AssertUtil.isTrue(StringUtil.equals(beforeEntity.getClass().getName(), afterEntity.getClass().getName()),
					"the before and after should be the same entity");
			break;
		default:
			throw new CommonException("unkown or not supported sql type: " + sqlType);
		}

		this.sqlType = sqlType;

		this.beforeEntity = beforeEntity;
		this.afterEntity = afterEntity;
	}

	/**
	 * @see org.yaen.starter.common.dal.entities.TwoEntity#BeforeUpdate()
	 */
	@Override
	public boolean BeforeUpdate() {
		return false;
	}

	/**
	 * @see org.yaen.starter.common.dal.entities.OneEntity#BeforeDelete()
	 */
	@Override
	public boolean BeforeDelete() {
		return false;
	}

	/**
	 * @see org.yaen.starter.common.dal.entities.OneEntity#getTableName()
	 */
	@Override
	public String getTableName() {
		// table name is LOG_ + container table name
		String name = "";
		try {
			AnotherEntity another = new AnotherEntity(this.beforeEntity == null ? this.afterEntity : this.beforeEntity);
			name = another.getTableName();
		} catch (RuntimeException ex) {
			name = "UNKNOWN";
		}

		return "LOG_" + name;
	}

}
