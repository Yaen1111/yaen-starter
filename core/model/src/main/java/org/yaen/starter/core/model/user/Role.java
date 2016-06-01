package org.yaen.starter.core.model.user;

import java.util.ArrayList;
import java.util.List;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.one.OneModel;
import org.yaen.starter.core.model.utils.ModelUtil;

import lombok.Getter;
import lombok.Setter;

/**
 * role object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_ROLE")
public class Role extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** title */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR50)
	private String title;

	/** description */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR250)
	private String description;

	/** the auth id of the role */
	private List<String> authIds;

	/**
	 * constructor
	 */
	public Role() {
		super();
	}

	/**
	 * getter of authIds
	 * 
	 * @return
	 * @throws CoreException
	 */
	public List<String> getAuthIds() throws CoreException {
		if (this.authIds == null) {
			// get auth id by RoleAuth
			RoleAuth sub = new RoleAuth();
			List<Object> list = ModelUtil.getService().selectValueListById(sub, this.getId(), "authId");

			this.authIds = new ArrayList<String>(list.size());

			for (Object o : list) {
				this.authIds.add((String) o);
			}
		}
		return this.authIds;
	}
}
