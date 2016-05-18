package org.yaen.starter.biz.shared.objects;

import java.io.Serializable;

import lombok.Data;

/**
 * party dto
 * 
 * @author Yaen 2016年5月17日下午4:30:18
 */
@Data
public class PartyDTO implements Serializable {
	private static final long serialVersionUID = -6724045254585597274L;

	private long partyID;

	private String partyType;

	private String partyRoleType;

}
