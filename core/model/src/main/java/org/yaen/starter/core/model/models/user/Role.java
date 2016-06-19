package org.yaen.starter.core.model.models.user;

import java.util.ArrayList;
import java.util.List;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.annotations.OneUniqueIndex;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.contexts.ServiceLoader;
import org.yaen.starter.core.model.models.OneModel;

import lombok.Getter;
import lombok.Setter;

/**
 * role object
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@OneTable(TableName = "ZU_ROLE")
@OneUniqueIndex("ID")
public class Role extends OneModel {
	private static final long serialVersionUID = -709733522935110043L;

	/** group name */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR20)
	private String groupName;

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

			// get values
			RoleAuth sub = new RoleAuth();
			List<Object> values = ServiceLoader.getQueryService().selectValueListById(sub, this.getId(), "authId");

			this.authIds = new ArrayList<String>(values.size());

			// convert to string
			for (Object o : values) {
				this.authIds.add((String) o);
			}
		}
		return this.authIds;
	}

	/**
	 * @see org.yaen.starter.core.model.models.OneModel#AfterSelect()
	 */
	@Override
	public void AfterSelect() throws CoreException {
		// reset ref
		this.authIds = null;

		// call super
		super.AfterSelect();
	}
}
