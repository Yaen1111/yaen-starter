/**
 * 
 */
package org.yaen.ark.common.dal.models;

import lombok.Getter;
import lombok.Setter;

/**
 * describe model for mysql desc result
 * 
 * @author xl 2016年1月6日下午7:57:22
 */
@Getter
@Setter
public class DescribeModel {

	private String myField;

	private String myType;

	private String myNull;

	private String myKey;

	private String myDefault;

	private String myExtra;
}
