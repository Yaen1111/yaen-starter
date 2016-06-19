package org.yaen.starter.core.model.models.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.contexts.ModelLoader;
import org.yaen.starter.core.model.models.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * user object, maybe other another model
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_USER")
@OneUniqueIndex("ID")
public class User extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** password hashed by salt */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 64)
	private String passwordHash;

	/** password salt */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 64)
	private String passwordSalt;

	/** last login time, can be used for prevent multi-login */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastLoginTime;

	/** last logout time, can be used for prevent multi-login */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.DATETIME)
	private Date lastLogoutTime;

	/** the role id of the user */
	private List<String> roleIds;

	/**
	 * constructor
	 */
	public User() {
		super();
	}

	/**
	 * getter of roleIds
	 * 
	 * @return
	 * @throws CoreException
	 */
	public List<String> getRoleIds() throws CoreException {
		if (this.roleIds == null) {

			// get values
			UserRole sub = new UserRole();
			List<Object> values = ModelLoader.getQueryService().selectValueListById(sub, this.getId(), "roleId");

			this.roleIds = new ArrayList<String>(values.size());

			// convert to string
			for (Object o : values) {
				this.roleIds.add((String) o);
			}
		}
		return this.roleIds;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#AfterSelect()
	 */
	@Override
	public void AfterSelect() throws CoreException {
		// reset ref
		this.roleIds = null;

		// call super
		super.AfterSelect();
	}
}
