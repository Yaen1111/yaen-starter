package org.yaen.starter.core.model.log.entities;

import org.yaen.starter.common.dal.entities.AnotherEntity;
import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTableHandler;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.entities.TwoEntity;

import lombok.Getter;
import lombok.Setter;

/**
 * history of entity, save when changed
 * 
 * @author Yaen 2016年7月17日下午9:42:06
 */
@Getter
@Setter
public class HistoryEntity extends TwoEntity implements OneTableHandler {
	private static final long serialVersionUID = -1542582451653223830L;

	/** the update user name */
	@OneData(DataType = DataTypes.VARCHAR32, FieldName = "SYS_UUSER")
	private String uUser;

	/** the update comment */
	@OneData(DataType = DataTypes.VARCHAR256, FieldName = "SYS_UCOMMENT")
	private String uComment;

	/** the entity history */
	@OneCopy(Prefix = "HISTORY_")
	private BaseEntity historyEntity;

	/**
	 * construct a history entity, use deep copy for before
	 * 
	 * @param historyEntity
	 */
	public HistoryEntity(BaseEntity historyEntity) {
		super();

		AssertUtil.notNull(historyEntity);

		this.historyEntity = historyEntity;
	}

	/**
	 * @see org.yaen.starter.core.model.entities.TwoEntity#BeforeUpdate()
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
		// table name is HIS_ + container table name
		String name = "";
		try {
			AnotherEntity another = new AnotherEntity(this.historyEntity);
			name = another.getTableName();
		} catch (RuntimeException ex) {
			name = "UNKNOWN";
		}

		return "HIS_" + name;
	}

}
