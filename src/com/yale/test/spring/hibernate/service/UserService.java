package com.yale.test.spring.hibernate.service;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yale.test.spring.hibernate.entity.User;

@Component
@Transactional
public class UserService {
	@Autowired
	HibernateTemplate hibernateTemplate;
	
	public User getUserById(long id) {
		return hibernateTemplate.load(User.class, id);
	}
	
	/*
	 * 最后，我们编写的大部分方法都是各种各样的查询。根据id查询我们可以直接调用load()或get()，如果要使用条件查询，有3种方法。
	 * 假设我们想执行以下查询：
	 * SELECT * FROM user WHERE email = ? AND password = ?
	 * 我们来看看可以使用什么查询。
	 * 使用Example查询
	 * 第一种方法是使用findByExample()，给出一个User实例，Hibernate把该实例所有非null的属性拼成WHERE条件：
	 * 因为example实例只有email和password两个属性为非null，所以最终生成的WHERE语句就是WHERE email = ? AND password = ?。
	 * 如果我们把User的createdAt的类型从Long改为long，findByExample()的查询将出问题，原因在于example实例的long类型字段有了默认值0，
	 * 导致Hibernate最终生成的WHERE语句意外变成了WHERE email = ? AND password = ? AND createdAt = 0。显然，额外的查询条件将导致错误的查询结果。
	 * 使用findByExample()时，注意基本类型字段总是会加入到WHERE条件！ 
	 */
	public User fetchUserByEmail(String email){
		User example = new User();
		example.setEmail(email);
		List<User> list = hibernateTemplate.findByExample(example);
		return list.isEmpty() ? null : list.get(0);
	}
	
	public User getUserByEmail(String email) {
		User user = fetchUserByEmail(email);
		if (user == null) {
			throw new RuntimeException("user not found by email: " + email);
		}
		return user;
	}
	
	/*
	 * 使用Criteria查询
	 * 第二种查询方法是使用Criteria查询，可以实现如下：
	 * DetachedCriteria使用链式语句来添加多个AND条件。和findByExample()相比，findByCriteria()可以组装出更灵活的WHERE条件，例如：
	 * SELECT * FROM user WHERE (email = ? OR name = ?) AND password = ?
	 * 上述查询没法用findByExample()实现，但用Criteria查询可以实现如下：findUser
	 */
	public User loginCheck(String email, String password) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.eq("email", email))
		.add(Restrictions.eq("password", password));
		List<User> list = (List<User>)hibernateTemplate.findByCriteria(criteria);
		return list.isEmpty() ? null : list.get(0);
	} 
	
	
	/**
	 * DetachedCriteria使用链式语句来添加多个AND条件。和findByExample()相比，findByCriteria()可以组装出更灵活的WHERE条件，例如：
	 * SELECT * FROM user WHERE (email = ? OR name = ?) AND password = ?
	 * 上述查询没法用findByExample()实现，但用Criteria查询可以实现如下：
	 * 只要组织好Restrictions的嵌套关系，Criteria查询可以实现任意复杂的查询。
	 * @param name
	 * @param email
	 * @param password
	 * @return
	 */
	public User findUser(String name, String email, String password) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		criteria.add(Restrictions.and(Restrictions.or(Restrictions.eq("email", email),
				Restrictions.eq("name", name)), Restrictions.eq("password", password)));
		List<User> list = (List<User>)hibernateTemplate.findByCriteria(criteria);
		return list.isEmpty() ? null : list.get(0);
	} 
	
	@SuppressWarnings("unchecked")
	public List<User> getUsers(int pageIndex) {
		int pageSize = 100;
		return (List<User>)hibernateTemplate.findByCriteria(DetachedCriteria.forClass(User.class),
				(pageIndex-1)*pageSize, pageSize);
	}
	
	/*
	 * 使用HQL查询
	 * 最后一种常用的查询是直接编写Hibernate内置的HQL查询：
	 * 和SQL相比，HQL使用类名和属性名，由Hibernate自动转换为实际的表名和列名。详细的HQL语法可以参考Hibernate文档(https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#hql)。
	 */
	public User signin(String email, String password) {
		@SuppressWarnings({"deprecation", "unchecked"})
		List<User> list = (List<User>)hibernateTemplate.find("FROM User WHERE email=? AND password=?", email,
				password);
		return list.isEmpty() ? null : list.get(0);
	}
	
	/*
	 * 除了可以直接传入HQL字符串外，Hibernate还可以使用一种NamedQuery，它给查询起个名字，然后保存在注解中。使用NamedQuery时，我们要先在User类标注：
	 * 注意到引入的NamedQuery是javax.persistence.NamedQuery，它和直接传入HQL有点不同的是，占位符使用?0、?1，并且索引是从0开始的（真乱）
	 * 使用NamedQuery只需要引入查询名和参数：
	 * 直接写HQL和使用NamedQuery各有优劣。前者可以在代码中直观地看到查询语句，后者可以在User类统一管理所有相关查询。
	 * 问:关于find()和findByNamedQuery()的弃用问题
	 * 老师你好，在eclipse上，显示这两个方法已弃用，请问有什么替代方法吗？
	 * 答:你要有代码洁癖可以用execute(HibernateCallback<T> action),我个人觉得hibernate已经不是首选了
	 */
	public User login(String email, String password) {
		@SuppressWarnings({"deprecation", "unchecked"})
		List<User> list = (List<User>)hibernateTemplate.findByNamedQuery("login", email, password);
		return list.isEmpty() ? null : list.get(0);
	}
	
	/*
	 * Insert操作
	 * 要持久化一个User实例，我们只需调用save()方法。以register()方法为例，代码如下：
	 */
	public User register(String email, String password, String name) {
		User user = new User();// 创建一个User对象:
		user.setEmail(email);// 设置好各个属性:
		// 不要设置id，因为使用了自增主键
		user.setPassword(password);
		user.setName(name);
		hibernateTemplate.save(user); // 保存到数据库:
		System.out.println(user.getId());// 现在已经自动获得了id:
		return user;
	}
	
	/*
	 * Update操作
	 * 更新记录相当于先更新User的指定属性，然后调用update()方法：
	 * 前面我们在定义User时，对有的属性标注了@Column(updatable=false)。Hibernate在更新记录时，它只会把@Column(updatable=true)的属性加入到UPDATE语句中，这样可以提供一层额外的安全性，即如果不小心修改了User的email、createdAt等属性，执行update()时并不会更新对应的数据库列。
	 * 但也必须牢记：这个功能是Hibernate提供的，如果绕过Hibernate直接通过JDBC执行UPDATE语句仍然可以更新数据库的任意列的值。
	 */
	public void updateUser(Long id, String name) {
		User user = hibernateTemplate.load(User.class, id);
		user.setName(name);
		hibernateTemplate.update(user);
	}
	
	/*
	 * Delete操作
	 * 删除一个User相当于从表中删除对应的记录。注意Hibernate总是用id来删除记录，因此，要正确设置User的id属性才能正常删除记录：
	 * 通过主键删除记录时，一个常见的用法是先根据主键加载该记录，再删除。load()和get()都可以根据主键加载记录，它们的区别在于，当记录不存在时，get()返回null，而load()抛出异常。
	 */
	public boolean deleteUser(Long id) {
		User user = hibernateTemplate.get(User.class, id);
		if (user != null) {
			hibernateTemplate.delete(user);
			return true;
		}
		return false;
	}
	
	/*
	 * 使用Hibernate原生接口
	 * 如果要使用Hibernate原生接口，但不知道怎么写，可以参考HibernateTemplate的源码。使用Hibernate的原生接口实际上总是从SessionFactory出发，它通常用全局变量存储，
	 * 在HibernateTemplate中以成员变量被注入。有了SessionFactory，使用Hibernate用法如下：
	 * void operation() {
		    Session session = null;
		    boolean isNew = false;
		    // 获取当前Session或者打开新的Session:
		    try {
		        session = this.sessionFactory.getCurrentSession();
		    } catch (HibernateException e) {
		        session = this.sessionFactory.openSession();
		        isNew = true;
		    }
		    // 操作Session:
		    try {
		        User user = session.load(User.class, 123L);
		    }
		    finally {
		        // 关闭新打开的Session:
		        if (isNew) {
		            session.close();
		        }
		    }
		}
	 */
}
