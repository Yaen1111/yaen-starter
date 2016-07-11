package org.yaen.starter.common.dal.services;

import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.data.exceptions.CommonException;

/**
 * table service, used to create/alter table
 * 
 * @author Yaen 2016年6月26日下午5:03:17
 */
public interface TableService {

	/**
	 * create table if not exists, or alter table if columns differs, using given one entity. but if isAutoTable is set
	 * to false, do nothing
	 * 
	 * @throws CommonException
	 */
	public void CreateTable(OneEntity entity) throws CommonException;

}
