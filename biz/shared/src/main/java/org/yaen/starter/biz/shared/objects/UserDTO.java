package org.yaen.starter.biz.shared.objects;

import java.io.Serializable;

import lombok.Data;

/**
 * user dto
 * 
 * @author Yaen 2016年5月17日下午4:30:18
 */
@Data
public class UserDTO implements Serializable {
	private static final long serialVersionUID = -6724045254585597274L;

	private String userName;

	private String password;

	private String passwordSalt;

	private String passwordHash;

	private String trueName;

	private String gender;

	private String mobilePhone;

	private String fixedPhone;

	private String email;

	private String idcardType;

	private String idcardNumber;

}
