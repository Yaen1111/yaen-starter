/**
 * 
 */
package org.yaen.ark.corecif.model.enums;

/**
 * party relationship definition for cif
 * 
 * @author xl 2016年1月13日上午11:52:48
 */
public final class RelationshipTypes {

	// no relation, with out target party, used for party role

	// with relation, with target party, used for party relationship
	// Person to Person

	// Org to Org
	public static final String SUBCOMPANY_PARENTCOMPANY = "SUBCOMPANY";
	public static final String DEPARTMENT_COMPANY = "DEPARTMENT";
	public static final String SUBDEPARTMENT_PARENTDEPARTMENT = "SUBDEPARTMENT";

	// Person to Org
	public static final String EMPLOYEE_COMPANY = "EMPLOYEE";
	public static final String POSITION_DEPARTMENT = "POSITION";

}
