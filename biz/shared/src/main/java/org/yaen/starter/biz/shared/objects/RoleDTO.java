package org.yaen.starter.biz.shared.objects;

import java.io.Serializable;

import lombok.Data;

/**
 * role dto
 * 
 * @author Yaen 2016年5月17日下午4:30:18
 */
@Data
public class RoleDTO implements Serializable {
	private static final long serialVersionUID = 2689960845760007277L;

	private String roleId;

	private String groupName;

	private String title;

	private String description;
}
