package com.yale.test.spring.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/*
 * 同样的，JPA也支持NamedQuery，即先给查询起个名字，再按名字创建查询：
 * NamedQuery通过注解标注在User类上，它的定义和上一节的User类一样：
 */
@NamedQueries(@NamedQuery(name="login", query="SELECT u FROM User u WHERE u.email=:e AND u.password=:p"))
@Entity
public class User extends AbstractEntity{

	private String email;
	private String password;
	private String name;
	public User() {
		super();
	}
	public User(long id, String email, String password, String name) {
		setId(id);
		setEmail(email);
		setPassword(password);
		setName(name);
	}
	@Column(nullable=false,unique=true,length=100)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Column(nullable=false, length=100)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(nullable=false, length=100)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(), getEmail(),
				getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
	}
}
