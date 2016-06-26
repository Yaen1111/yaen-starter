package org.yaen.starter.common.dal.services.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Service;
import org.yaen.starter.common.dal.entities.MyDescribeEntity;
import org.yaen.starter.common.dal.entities.OneColumnEntity;
import org.yaen.starter.common.dal.entities.OneEntity;
import org.yaen.starter.common.dal.mappers.ZeroMapper;
import org.yaen.starter.common.dal.services.ZeroEntityService;
import org.yaen.starter.common.data.enums.DataTypes;
import org.yaen.starter.common.data.exceptions.CommonException;
import org.yaen.starter.common.util.utils.StringUtil;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * table manager, used to create/alter table
 * 
 * @author Yaen 2016年6月26日下午5:03:17
 */
@Service
public class ZeroEntityServiceImpl implements ZeroEntityService {

	/** tables that are already updated */
	private static Set<String> tableSet = new HashSet<String>();

	@Autowired
	private ZeroMapper zeroMapper;

	/**
	 * @see org.yaen.starter.common.dal.services.ZeroEntityService#CreateTable(org.yaen.starter.common.dal.entities.OneEntity)
	 */
	@Override
	public void CreateTable(OneEntity entity) throws CommonException {
		if (entity == null)
			return;

		// check cache
		String entity_name = entity.getClass().getName();
		if (tableSet.contains(entity_name))
			return;

		List<MyDescribeEntity> describes = null;

		try {

			// describe table, if table not exists, throw exception
			try {
				describes = zeroMapper.describeTable(entity.getTableName());
			} catch (BadSqlGrammarException ex) {
				Throwable ex2 = ex.getCause();

				if (ex2 instanceof MySQLSyntaxErrorException
						&& StringUtil.like(((MySQLSyntaxErrorException) ex2).getSQLState(), "42S02")) {
					// maybe table not exists, catch and eat it
				} else {
					throw ex;
				}
			}

			// check table exists
			if (describes == null) {
				// not exists, create new table
				zeroMapper.createTable(entity);

			} else {

				// exists, try to check column type and missed column
				// loop every element, and add/mod if not suitable
				for (Entry<String, OneColumnEntity> entry : entity.getColumns().entrySet()) {
					boolean exists = false;

					for (MyDescribeEntity describe : describes) {
						if (StringUtil.like(describe.getMyField(), entry.getValue().getColumnName())) {

							exists = true;

							// has column, check type
							{
								String type = entry.getValue().getDataType();
								int size = entry.getValue().getDataSize();

								if (size == 0) {
									// some special type has default size
									if (StringUtil.like(type, DataTypes.BIGINT)) {
										size = 20;
									} else if (StringUtil.like(type, DataTypes.INT)) {
										size = 11;
									}
								}

								if (size > 0) {
									type = type + "(" + size + ")";
								}

								if (!StringUtil.like(type, describe.getMyType())) {
									// column type changed, try to modify
									entity.setModifiedFieldName(entry.getKey());
									zeroMapper.modifyColumn(entity);
								}
							}

							break;
						}
					} // for

					if (!exists) {
						// no column, try to add one
						entity.setAddedFieldName(entry.getKey());
						zeroMapper.addColumn(entity);
					}
				} // for
			} // describes == null

		} catch (CommonException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new CommonException("create/alter table failed", ex);
		}

		// add to cache
		tableSet.add(entity_name);
	}

}
