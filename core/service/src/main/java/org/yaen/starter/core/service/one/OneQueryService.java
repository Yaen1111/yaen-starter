/**
 * 
 */
package org.yaen.starter.core.service.one;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yaen.starter.common.dal.mappers.QueryMapper;
import org.yaen.starter.common.dal.models.QueryModel;
import org.yaen.starter.common.data.entities.AttributeEntity;
import org.yaen.starter.common.data.entities.BaseEntity;
import org.yaen.starter.common.data.entities.RelationEntity;
import org.yaen.starter.common.data.pos.AnotherPO;
import org.yaen.starter.common.data.pos.OneColumnPO;
import org.yaen.starter.common.data.services.QueryService;
import org.yaen.starter.common.util.StringUtil;

/**
 * one event service for most operation
 * 
 * @author Yaen 2016年1月4日下午8:35:55
 */
@Service
public class OneQueryService implements QueryService {

	@Autowired
	private QueryMapper mapper;

	/**
	 * select ids by base id
	 * 
	 * @param element
	 * @param baseId
	 * @throws Exception
	 */
	@Override
	public <T_ATTR extends AttributeEntity> List<Long> SelectIDsByAttributeBase(T_ATTR attribute, long baseId)
			throws Exception {
		Assert.notNull(attribute);

		// get another po
		AnotherPO po = new AnotherPO(attribute);

		// need event engine
		QueryModel model = new QueryModel();
		model.setTableName(po.getTableName());

		// set given base id
		model.setBaseId(baseId);

		// call event
		return mapper.selectIDsByBaseID(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByRelationFrom(org.yaen.starter.common.data.entities.RelationEntity,
	 *      long)
	 */
	@Override
	public <T_REL extends RelationEntity> List<Long> SelectIDsByRelationFrom(T_REL rel, long fromId) throws Exception {
		Assert.notNull(rel);

		// get another po
		AnotherPO po = new AnotherPO(rel);

		// need event engine
		QueryModel model = new QueryModel();
		model.setTableName(po.getTableName());

		// set given base id
		model.setFromId(fromId);

		// call event
		return mapper.selectIDsByFromID(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByRelationTo(org.yaen.starter.common.data.entities.RelationEntity,
	 *      long)
	 */
	@Override
	public <T_REL extends RelationEntity> List<Long> SelectIDsByRelationTo(T_REL rel, long toId) throws Exception {
		Assert.notNull(rel);

		// get another po
		AnotherPO po = new AnotherPO(rel);

		// need event engine
		QueryModel model = new QueryModel();
		model.setTableName(po.getTableName());

		// set given base id
		model.setFromId(toId);

		// call event
		return mapper.selectIDsByToID(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByFieldName(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByFieldName(T entity, String fieldName) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(fieldName);
		return this.SelectIDsByFieldNameList(entity, list);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByFieldNameList(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByFieldNameList(T entity, List<String> fieldNameList)
			throws Exception {
		Assert.notNull(entity);
		Assert.notNull(fieldNameList);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		// make event model
		QueryModel model = new QueryModel();

		// set table name
		model.setTableName(po.getTableName());

		// make columns
		{
			Map<String, Object> columns = new HashMap<String, Object>();

			for (String fieldname : fieldNameList) {

				OneColumnPO info = po.getColumns().get(fieldname);
				columns.put(info.getColumnName(), info.getValue());
			}

			model.setColumns(columns);
		}

		// call mapper
		return mapper.selectIDsByColumns(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByAllField(org.yaen.starter.common.data.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByAllField(T entity) throws Exception {
		Assert.notNull(entity);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		// make event model
		QueryModel model = new QueryModel();

		// set table name
		model.setTableName(po.getTableName());

		// make columns
		{
			Map<String, Object> columns = new HashMap<String, Object>();

			// add columns if not id and is not null
			for (Entry<String, OneColumnPO> entry : po.getColumns().entrySet()) {
				if (!StringUtil.like(entry.getKey(), "id") && entry.getValue().getValue() != null) {
					columns.put(entry.getValue().getColumnName(), entry.getValue().getValue());
				}
			}

			model.setColumns(columns);
		}

		// call mapper
		return mapper.selectIDsByColumns(model);
	}

	/**
	 * 
	 * @see org.yaen.starter.common.data.services.QueryService#SelectIDsByWhereClause(org.yaen.starter.common.data.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByWhereClause(T entity, String whereClause) throws Exception {
		Assert.notNull(entity);
		Assert.notNull(whereClause);

		// get another po
		AnotherPO po = new AnotherPO(entity);

		// make event model
		QueryModel model = new QueryModel();

		// set table name
		model.setTableName(po.getTableName());

		// set where clause
		model.setWhereClause(whereClause);

		// call mapper
		return mapper.selectIDsByWhereClause(model);
	}

}
