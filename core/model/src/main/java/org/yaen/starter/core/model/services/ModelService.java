package org.yaen.starter.core.model.services;

import org.yaen.starter.common.data.exceptions.CoreException;
import org.yaen.starter.core.model.models.BaseModel;

/**
 * model service for CRUD
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
public interface ModelService {

	/**
	 * select model by rowid, and update model object with data
	 * 
	 * @param model
	 * @param rowid
	 * @throws Exception
	 */
	public <T extends BaseModel> void selectModelByRowid(T model, long rowid) throws CoreException;

	/**
	 * insert model
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> long insertModelByRowid(T model) throws CoreException;

	/**
	 * update model by rowid, this id is also be updated
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> void updateModelByRowid(T model) throws CoreException;

	/**
	 * delete model by rowid
	 * 
	 * @param model
	 * @throws Exception
	 */
	public <T extends BaseModel> void deleteModelByRowid(T model) throws CoreException;

	/**
	 * try select model by rowid, return false if not exists
	 * 
	 * @param model
	 * @param rowid
	 * @return
	 * @throws Exception
	 */
	public <T extends BaseModel> boolean trySelectModelByRowid(T model, long rowid) throws CoreException;

}
