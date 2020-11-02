package com.yale.test.spring.jpa.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.yale.test.spring.jpa.entity.User;

/*
 * 还是以UserService为例，除了标注@Component和@Transactional外，我们需要注入一个EntityManager，但是不要使用Autowired，而是@PersistenceContext：
 * 我们回顾一下JDBC、Hibernate和JPA提供的接口，实际上，它们的关系如下：
 * JDBC			Hibernate		JPA
 * DataSource	SessionFactory	EntityManagerFactory
 * Connection	Session			EntityManager
 * SessionFactory和EntityManagerFactory相当于DataSource，Session和EntityManager相当于Connection。每次需要访问数据库的时候，
 * 需要获取新的Session和EntityManager，用完后再关闭。
 * 但是，注意到UserService注入的不是EntityManagerFactory，而是EntityManager，并且标注了@PersistenceContext。难道使用JPA可以允许多线程操作同一个EntityManager？
 * 实际上这里注入的并不是真正的EntityManager，而是一个EntityManager的代理类，相当于：
 * public class EntityManagerProxy implements EntityManager {
    	private EntityManagerFactory emf;
	}
 * Spring遇到标注了@PersistenceContext的EntityManager会自动注入代理，该代理会在必要的时候自动打开EntityManager。
 * 换句话说，多线程引用的EntityManager虽然是同一个代理类，但该代理类内部针对不同线程会创建不同的EntityManager实例。
 * 简单总结一下，标注了@PersistenceContext的EntityManager可以被多线程安全地共享。
 * 因此，在UserService的每个业务方法里，直接使用EntityManager就很方便。以主键查询为例：
 */
@Component
@Transactional
public class UserService {
	
	@PersistenceContext
	EntityManager em;
	
	/*
	 * 因此，在UserService的每个业务方法里，直接使用EntityManager就很方便。以主键查询为例：
	 */
	public User getUserById(long id){
		User user = em.find(User.class, id);
		if (user == null) {
			throw new RuntimeException("User not found by id: " + id);
		}
		return user;
	}
	
	/*
	 * JPA同样支持Criteria查询，比如我们需要的查询如下：
	 * SELECT * FROM user WHERE email = ?
	 * 使用Criteria查询的代码如下：
	 * 一个简单的查询用Criteria写出来就像上面那样复杂，太恐怖了，如果条件多加几个，这种写法谁读得懂？
	 * 所以，正常人还是建议写JPQL查询，它的语法和HQL基本差不多：
	 */
	public User fetchUserByEmail(String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> q = cb.createQuery(User.class);
		Root<User> r = q.from(User.class);
		q.where(cb.equal(r.get("email"), cb.parameter(String.class, "e")));
		TypedQuery<User> query = em.createQuery(q);
		query.setParameter("e", email); //绑定参数:
		List<User> list = query.getResultList();// 执行查询:
		return list.isEmpty() ? null : list.get(0);
	}
	
	/*
	 * 所以，正常人还是建议写JPQL查询，它的语法和HQL基本差不多：
	 */
	public User getUserByEmail(String email) {
		// JPQL查询:
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email=:e", User.class);
		query.setParameter("e", email);
		List<User> list = query.getResultList();
		if (list.isEmpty()) {
			throw new RuntimeException("User not found by email.");
		}
		return list.get(0);
	}
	
	public List<User> getUsers(int pageIndex){
		int pageSize = 100;
		TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
		query.setFirstResult((pageIndex -1)*pageSize);
		query.setMaxResults(pageSize);
		return query.getResultList();
	}
	
	/*
	 * 同样的，JPA也支持NamedQuery，即先给查询起个名字，再按名字创建查询：
	 * NamedQuery通过注解标注在User类上，它的定义和上一节的User类一样：
	 */
	public User login(String email, String password) {
		TypedQuery<User> query = em.createNamedQuery("login", User.class);
		query.setParameter("e", email);
		query.setParameter("p", password);
		List<User> list = query.getResultList();
		return list.isEmpty() ? null : list.get(0);
	}
	
	/*
	 * 对数据库进行增删改的操作，可以分别使用persist()、remove()和merge()方法，参数均为Entity Bean本身，使用非常简单，这里不再多述。
	 * 对于此处的update操作，将实体数据更新到数据库应该用merge(T t), refresh(Object obj)是从数据库中读数据更新到实体。
	 */
	public User register(String email, String password, String name) {
		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(password);
		em.persist(user);
		return user;
	}
	
	public void updateUser(Long id, String name) {
		User user = getUserById(id);
		user.setName(name);
		em.refresh(user);
	}
	
	public void deleteUser(Long id) {
		User user = getUserById(id);
		em.remove(user);
	}
}
