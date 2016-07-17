package org.yaen.starter.core.model.entities.log;

import org.yaen.starter.common.dal.entities.AnotherEntity;
import org.yaen.starter.common.dal.entities.TwoEntity;
import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTableHandler;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.AssertUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * recycle of entity, save only deleted record
 * 
 * @author Yaen 2016年7月17日下午9:42:06
 */
@Getter
@Setter
public class RecycleEntity extends TwoEntity implements OneTableHandler {
	private static final long serialVersionUID = -2035026481755554959L;

	/** the update user name */
	@OneData(DataType = DataTypes.VARCHAR32, FieldName = "SYS_UUSER")
	private String uUser;

	/** the update comment */
	@OneData(DataType = DataTypes.VARCHAR250, FieldName = "SYS_UCOMMENT")
	private String uComment;

	/** the entity deleted */
	@OneCopy(Prefix = "DELETED_")
	private BaseEntity deletedEntity;

	/**
	 * construct a recycle entity, use deep copy for before
	 * 
	 * @param deletedEntity
	 */
	public RecycleEntity(BaseEntity deletedEntity) {
		super();

		AssertUtil.notNull(deletedEntity);

		this.deletedEntity = deletedEntity;
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
		// table name is REC_ + container table name
		String name = "";
		try {
			AnotherEntity another = new AnotherEntity(this.deletedEntity);
			name = another.getTableName();
		} catch (RuntimeException ex) {
			name = "UNKNOWN";
		}

		return "REC_" + name;
	}

}
