/**
 * 
 */
package org.yaen.starter.common.data.pos;

import lombok.Getter;
import lombok.Setter;

/**
 * describe po for mysql desc result
 * 
 * @author Yaen 2016年1月6日下午7:57:22
 */
@Getter
@Setter
public class MyDescribePO {

	private String myField;

	private String myType;

	private String myNull;

	private String myKey;

	private String myDefault;

	private String myExtra;
}
