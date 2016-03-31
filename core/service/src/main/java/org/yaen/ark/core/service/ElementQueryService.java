/**
 * 
 */
package org.yaen.ark.core.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.yaen.ark.core.model.utils.ElementUtil;
import org.yaen.spring.data.entities.AttributeEntity;
import org.yaen.spring.data.entities.BaseEntity;
import org.yaen.spring.data.entities.RelationEntity;
import org.yaen.spring.data.mappers.QueryMapper;
import org.yaen.spring.data.models.ElementInfo;
import org.yaen.spring.data.models.ElementModel;
import org.yaen.spring.data.models.QueryModel;
import org.yaen.spring.data.services.QueryService;
import org.yaen.spring.common.utils.StringUtil;


/**
 * element event service for most operation
 * 
 * @author xl 2016年1月4日下午8:35:55
 */
@Service
public class ElementQueryService implements QueryService {

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

		// need event engine
		QueryModel model = new QueryModel();
		model.setTableName(ElementUtil.getElementTableName(attribute));

		// set given base id
		model.setBaseId(baseId);

		// call event
		return mapper.selectIDsByBaseID(model);
	}

	/**
	 * @see org.yaen.arkcore.core.model.services.QueryService#SelectIDsByRelationFrom(org.yaen.arkcore.core.model.entities.RelationEntity,
	 *      long)
	 */
	@Override
	public <T_REL extends RelationEntity> List<Long> SelectIDsByRelationFrom(T_REL rel, long fromId) throws Exception {
		Assert.notNull(rel);

		// need event engine
		QueryModel model = new QueryModel();
		model.setTableName(ElementUtil.getElementTableName(rel));

		// set given base id
		model.setFromId(fromId);

		// call event
		return mapper.selectIDsByFromID(model);
	}

	/**
	 * @see org.yaen.arkcore.core.model.services.QueryService#SelectIDsByRelationTo(org.yaen.arkcore.core.model.entities.RelationEntity,
	 *      long)
	 */
	@Override
	public <T_REL extends RelationEntity> List<Long> SelectIDsByRelationTo(T_REL rel, long toId) throws Exception {
		Assert.notNull(rel);

		// need event engine
		QueryModel model = new QueryModel();
		model.setTableName(ElementUtil.getElementTableName(rel));

		// set given base id
		model.setFromId(toId);

		// call event
		return mapper.selectIDsByToID(model);
	}

	/**
	 * @see org.yaen.arkcore.core.model.services.QueryService#SelectIDsByFieldName(org.yaen.arkcore.core.model.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByFieldName(T entity, String fieldName) throws Exception {
		List<String> list = new ArrayList<String>();
		list.add(fieldName);
		return this.SelectIDsByFieldNameList(entity, list);
	}

	/**
	 * @see org.yaen.arkcore.core.model.services.QueryService#SelectIDsByFieldNameList(org.yaen.arkcore.core.model.entities.BaseEntity,
	 *      java.util.List)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByFieldNameList(T entity, List<String> fieldNameList)
			throws Exception {
		Assert.notNull(entity);
		Assert.notNull(fieldNameList);

		// need element model
		ElementModel element = ElementUtil.GetElementModel(entity);

		// make event model
		QueryModel model = new QueryModel();

		// set table name
		model.setTableName(element.getTableName());

		// make columns
		{
			Map<String, Object> columns = new HashMap<String, Object>();

			for (String fieldname : fieldNameList) {

				ElementInfo info = element.getColumns().get(fieldname);
				columns.put(info.getColumnName(), info.getValue());
			}

			model.setColumns(columns);
		}

		// call mapper
		return mapper.selectIDsByColumns(model);
	}

	/**
	 * @see org.yaen.arkcore.core.model.services.QueryService#SelectIDsByAllField(org.yaen.arkcore.core.model.entities.BaseEntity)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByAllField(T entity) throws Exception {
		Assert.notNull(entity);

		// need element model
		ElementModel element = ElementUtil.GetElementModel(entity);

		// make event model
		QueryModel model = new QueryModel();

		// set table name
		model.setTableName(element.getTableName());

		// make columns
		{
			Map<String, Object> columns = new HashMap<String, Object>();

			// add columns if not id and is not null
			for (Entry<String, ElementInfo> entry : element.getColumns().entrySet()) {
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
	 * @see org.yaen.arkcore.core.model.services.QueryService#SelectIDsByWhereClause(org.yaen.arkcore.core.model.entities.BaseEntity,
	 *      java.lang.String)
	 */
	@Override
	public <T extends BaseEntity> List<Long> SelectIDsByWhereClause(T entity, String whereClause) throws Exception {
		Assert.notNull(entity);
		Assert.notNull(whereClause);

		// make event model
		QueryModel model = new QueryModel();

		// set table name
		model.setTableName(ElementUtil.getElementTableName(entity));

		// set where clause
		model.setWhereClause(whereClause);

		// call mapper
		return mapper.selectIDsByWhereClause(model);
	}

}
