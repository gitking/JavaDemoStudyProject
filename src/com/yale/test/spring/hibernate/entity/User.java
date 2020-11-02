package com.yale.test.spring.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

//注意到引入的NamedQuery是javax.persistence.NamedQuery，它和直接传入HQL有点不同的是，占位符使用?0、?1，并且索引是从0开始的（真乱）
@NamedQueries(@NamedQuery(name="login", query="SELECT u FROM User u WHERE u.email=?0 and u.password=?1"))
@Entity
//如果一个JavaBean被用于映射，我们就标记一个@Entity。默认情况下，映射的表名是user，如果实际的表名不同，例如实际表名是users，可以追加一个@Table(name="users")表示：
//每个属性到数据库列的映射用@Column()标识，nullable指示列是否允许为NULL，updatable指示该列是否允许被用在UPDATE语句，length指示String类型的列的长度（如果没有指定，默认是255）。
//对于主键，还需要用@Id标识，自增主键再追加一个@GeneratedValue，以便Hibernate能读取到自增主键的值。
//细心的童鞋可能还注意到，主键id定义的类型不是long，而是Long。这是因为Hibernate如果检测到主键为null，就不会在INSERT语句中指定主键的值，而是返回由数据库生成的自增值，否则，Hibernate认为我们的程序指定了主键的值，会在INSERT语句中直接列出。long型字段总是具有默认值0，因此，每次插入的主键值总是0，导致除第一次外后续插入都将失败。
//createdAt虽然是整型，但我们并没有使用long，而是Long，这是因为使用基本类型会导致某种查询会添加意外的条件，后面我们会详细讨论，这里只需牢记，作为映射使用的JavaBean，所有属性都使用包装类型而不是基本类型。
//使用Hibernate时，不要使用基本类型的属性，总是使用包装类型，如Long或Integer。 
//@Table(name="users")
public class User extends AbstractEntity{
	private String email;
	private String password;
	private String name;
	
	public User() {
	}
	
	public User(String email, String password, String name) {
		super();
		this.email = email;
		this.password = password;
		this.name = name;
	}

	@Column(nullable = false, unique = true, length=100)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(nullable = false, length = 100)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(nullable= false, length=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return String.format("User[id=%s, email=%s, name=%s, password=%s, createdAt=%s, createdDateTime=%s]", getId(),
				getEmail(), getName(), getPassword(), getCreatedAt(), getCreatedDateTime());
	}
}
