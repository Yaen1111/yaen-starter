/**
 * 
 */
package org.yaen.ark.cif.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.data.services.EntityService;

/**
 * cif service for most operation
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@Service
public class CifService {

	@Autowired
	private EntityService entityService;

	/**
	 * save given entity
	 * 
	 * @param entity
	 * @throws Exception
	 */
	public <T extends BaseEntity> void saveEntity(T entity) throws Exception {
		entityService.insertEntity(entity);
	}

}
