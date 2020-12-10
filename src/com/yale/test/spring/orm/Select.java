package com.yale.test.spring.orm;

import java.util.Arrays;


@SuppressWarnings("rawtypes")
public final class Select extends CriteriaQuery{
	
	@SuppressWarnings("unchecked")
	Select(Criteria criteria, String...selectFields) {
		super(criteria);
		if (selectFields.length > 0) {
			this.criteria.select  = Arrays.asList(selectFields);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> From<T> from(Class<T> entityClass) {
		return new From<T>(this.criteria, this.criteria.db.getMapper(entityClass));
	}
}
