/**
 * 
 */
package org.yaen.starter.core.model.log;

import org.yaen.starter.common.data.annotations.OneCopy;
import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTableHandler;
import org.yaen.starter.common.data.entities.AnotherEntity;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.enums.SqlTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.common.data.models.BaseModel;
import org.yaen.starter.common.data.services.ModelService;
import org.yaen.starter.common.util.utils.AssertUtil;
import org.yaen.starter.core.model.one.OneModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * change log of element, save before and after value
 * 
 * @author Yaen 2016年1月4日下午8:40:03
 */
@ToString(callSuper = true)
public class Changelog extends OneModel implements OneTableHandler {
	private static final long serialVersionUID = -606829448983274029L;

	/** the sql type */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "SQL_TYPE")
	private String sqlType;

	/** the update user name */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 20, FieldName = "SYS_UUSER")
	private String uUser;

	/** the update comment */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 250, FieldName = "SYS_UCOMMENT")
	private String uComment;

	/** the model before change, if create, null */
	@Getter
	@Setter
	@OneCopy(Prefix = "BEFORE_")
	private BaseModel beforeModel;

	/** the model after change, if delete, null */
	@Getter
	@Setter
	@OneCopy(Prefix = "AFTER_")
	private BaseModel afterModel;

	/**
	 * construct a changelog model, use deep copy for before
	 * 
	 * @param sqlType
	 * @param beforeModel
	 * @param afterModel
	 * @throws CoreException
	 */
	public Changelog(String sqlType, BaseModel beforeModel, BaseModel afterModel) throws CoreException {
		super();

		// check param by type
		switch (sqlType) {
		case SqlTypes.INSERT:
			AssertUtil.notNull(afterModel);
			break;
		case SqlTypes.DELETE:
			AssertUtil.notNull(beforeModel);
			break;
		case SqlTypes.UPDATE:
			AssertUtil.notNull(beforeModel);
			AssertUtil.notNull(afterModel);
			AssertUtil.isTrue(beforeModel.getClass().getName() == afterModel.getClass().getName(),
					"the before and after should be the same model");
			break;
		default:
			throw new CoreException("unkown or not supported sql type: " + sqlType);
		}

		this.sqlType = sqlType;

		this.beforeModel = beforeModel;
		this.afterModel = afterModel;
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeUpdate(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeUpdate(ModelService service) throws Exception {
		throw new CoreException("change document can not be updated");
	}

	/**
	 * @see org.yaen.starter.core.model.one.OneModel#BeforeDelete(org.yaen.starter.common.data.services.ModelService)
	 */
	@Override
	public boolean BeforeDelete(ModelService service) throws Exception {
		throw new CoreException("change document can not be deleted");
	}

	/**
	 * @see org.yaen.starter.common.data.annotations.OneTableHandler#getTableName()
	 */
	@Override
	public String getTableName() {
		// table name is LOG_ + container table name
		String name = "";
		try {
			AnotherEntity another = new AnotherEntity(this.beforeModel == null ? this.afterModel : this.beforeModel);
			name = another.getTableName();
		} catch (CoreException ex) {
			name = "UNKNOWN";
		}

		return "LOG_" + name;
	}

}
