package com.yale.test.spring.hibernate.entity;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Transient;

/*
 * 对于AbstractEntity来说，我们要标注一个@MappedSuperclass表示它用于继承。此外，注意到我们定义了一个@Transient方法，它返回一个“虚拟”的属性。
 * 因为getCreatedDateTime()是计算得出的属性，而不是从数据库表读出的值，因此必须要标注@Transient，否则Hibernate会尝试从数据库读取名为createdDateTime这个不存在的字段从而出错。
 * 再注意到@PrePersist标识的方法，它表示在我们将一个JavaBean持久化到数据库之前（即执行INSERT语句），Hibernate会先执行该方法，这样我们就可以自动设置好createdAt属性。
 * 有了AbstractEntity，我们就可以大幅简化User和Book：
 * 注意到使用的所有注解均来自javax.persistence，它是JPA规范的一部分。这里我们只介绍使用注解的方式配置Hibernate映射关系，不再介绍传统的比较繁琐的XML配置。
 * 通过Spring集成Hibernate时，也不再需要hibernate.cfg.xml配置文件，用一句话总结：
 * 使用Spring集成Hibernate，配合JPA注解，无需任何额外的XML配置。 
 */
@MappedSuperclass
public abstract class AbstractEntity {
	private Long id;
	private Long createdAt;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, updatable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(nullable = false, updatable = false)
	public Long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}
	
	@Transient
	public ZonedDateTime getCreatedDateTime() {
		return Instant.ofEpochMilli(this.createdAt).atZone(ZoneId.systemDefault());
	}
	
	@PrePersist
	public void preInsert() {
		setCreatedAt(System.currentTimeMillis());
	}
}
