/**
 * 
 */
package org.yaen.starter.core.model.party;

import org.yaen.starter.common.data.annotations.OneData;
import org.yaen.starter.common.data.annotations.OneTable;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.core.model.one.OneFractalModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * party fractal, 1:1
 * 
 * @author Yaen 2016年5月17日下午2:28:32
 */
@ToString(callSuper = true)
@OneTable(TableName = "PARTY_FRACTAL")
public class PartyFractal extends OneFractalModel {
	private static final long serialVersionUID = -7283963978173189572L;

	/**
	 * the true name
	 */
	@Getter
	@Setter
	@OneData(DataType = DataTypes.VARCHAR, DataSize = 100)
	private String trueName;

	/**
	 * constructor
	 */
	public PartyFractal(Party party) {
		super(party);
	}

}
