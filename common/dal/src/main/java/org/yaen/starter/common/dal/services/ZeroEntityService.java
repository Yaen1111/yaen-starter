package org.yaen.starter.common.dal.services;

import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;

/**
 * table manager, used to create/alter table
 * 
 * @author Yaen 2016年6月26日下午5:03:17
 */
public interface ZeroEntityService {

	/**
	 * create table if not exists, or alter table if columns differs, using given one entity
	 * 
	 * @throws CommonException
	 */
	public void CreateTable(OneEntity entity) throws CommonException;

}
