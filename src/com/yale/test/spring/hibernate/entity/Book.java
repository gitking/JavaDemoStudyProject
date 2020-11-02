package com.yale.test.spring.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;

/*
 * 如果仔细观察User和Book，会发现它们定义的id、createdAt属性是一样的，这在数据库表结构的设计中很常见：对于每个表，通常我们会统一使用一种主键生成机制，并添加createdAt表示创建时间，updatedAt表示修改时间等通用字段。
 * 不必在User和Book中重复定义这些通用字段，我们可以把它们提到一个抽象类AbstractEntity 中：
 */
@Entity
public class Book extends AbstractEntity{
	private String title;

	@Column(nullable = false, length = 100)
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return String.format("Book[id=%s, title=%s, createdAt=%s, createdDateTime=%s]", getId(), getTitle(),
				getCreatedAt(), getCreatedDateTime());
	}
}
