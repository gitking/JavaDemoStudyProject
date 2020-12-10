package com.yale.test.spring.orm;

import java.util.List;

public final class Limit<T> extends CriteriaQuery<T> {
	Limit(Criteria<T> criteria, int offset, int maxResults) {
		super(criteria);
		if (offset < 0) {
			throw new IllegalArgumentException("offset must be >=0");
		}
		if (maxResults <=0 ) {
			throw new IllegalArgumentException("maxResults must be > 0.");
		}
		this.criteria.offset = offset;
		this.criteria.maxResults = maxResults;
	}
	
	public List<T> list() {
		return criteria.list();
	}
}
