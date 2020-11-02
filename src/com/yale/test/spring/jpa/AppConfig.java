package com.yale.test.spring.jpa;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yale.test.spring.jpa.entity.AbstractEntity;
import com.yale.test.spring.jpa.entity.User;
import com.yale.test.spring.jpa.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 集成JPA
 * 上一节我们讲了在Spring中集成Hibernate。Hibernate是第一个被广泛使用的ORM框架，但是很多小伙伴还听说过JPA：Java Persistence API，这又是啥？
 * 在讨论JPA之前，我们要注意到JavaEE早在1999年就发布了，并且有Servlet、JMS等诸多标准。和其他平台不同，Java世界早期非常热衷于标准先行，各家跟进：大家先坐下来把接口定了，
 * 然后，各自回家干活去实现接口，这样，用户就可以在不同的厂家提供的产品进行选择，还可以随意切换，因为用户编写代码的时候只需要引用接口，并不需要引用具体的底层实现（想想JDBC）。
 * JPA就是JavaEE的一个ORM标准，它的实现其实和Hibernate没啥本质区别，但是用户如果使用JPA，那么引用的就是javax.persistence这个“标准”包，而不是org.hibernate这样的第三方包。
 * 因为JPA只是接口，所以，还需要选择一个实现产品，跟JDBC接口和MySQL驱动一个道理。
 * 我们使用JPA时也完全可以选择Hibernate作为底层实现，但也可以选择其它的JPA提供方，比如EclipseLink(https://www.eclipse.org/eclipselink/)。
 * Spring内置了JPA的集成，并支持选择Hibernate或EclipseLink作为实现。这里我们仍然以主流的Hibernate作为JPA实现为例子，演示JPA的基本用法。
 * 和使用Hibernate一样，我们只需要引入如下依赖：
 * org.springframework:spring-context:5.2.0.RELEASE
 * org.springframework:spring-orm:5.2.0.RELEASE
 * javax.annotation:javax.annotation-api:1.3.2
 * org.hibernate:hibernate-core:5.4.2.Final
 * com.zaxxer:HikariCP:3.4.2
 * org.hsqldb:hsqldb:2.5.0
 * 然后，在AppConfig中启用声明式事务管理，创建DataSource：
 * 使用Hibernate时，我们需要创建一个LocalSessionFactoryBean，并让它再自动创建一个SessionFactory。
 * 使用JPA也是类似的，我们需要创建一个LocalContainerEntityManagerFactoryBean，并让它再自动创建一个EntityManagerFactory：
 * 观察下述代码，除了需要注入DataSource和设定自动扫描的package外，还需要指定JPA的提供商，这里使用Spring提供的一个HibernateJpaVendorAdapter，
 * 最后，针对Hibernate自己需要的配置，以Properties的形式注入。
 * 最后，我们还需要实例化一个JpaTransactionManager，以实现声明式事务：
 * 这样，我们就完成了JPA的全部初始化工作。有些童鞋可能从网上搜索得知JPA需要persistence.xml配置文件，以及复杂的orm.xml文件。
 * 这里我们负责地告诉大家，使用Spring+Hibernate作为JPA实现，无需任何配置文件。
 * 所有Entity Bean的配置和上一节完全相同，全部采用Annotation标注。我们现在只需关心具体的业务类如何通过JPA接口操作数据库。
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
 * 小结
 * 在Spring中集成JPA要选择一个实现，可以选择Hibernate或EclipseLink；
 * 使用JPA与Hibernate类似，但注入的核心资源是带有@PersistenceContext注解的EntityManager代理类。
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {

	@Bean
	DataSource createDataSource(@Value("${jdbc.url}")String jdbcUrl, @Value("${jdbc.username}")String jdbcUsername,
			@Value("${jdbc.password}")String jdbcPassword) {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setPassword(jdbcPassword);
		config.addDataSourceProperty("autoCommit", "false");
		config.addDataSourceProperty("connectionTimeout", "5");
		config.addDataSourceProperty("idleTimeout", "60");
		return new HikariDataSource(config);
	}
	
	@Bean
	LocalContainerEntityManagerFactoryBean createEntityManagerFactory(@Autowired DataSource dataSource) {
		Properties props = new Properties();
		props.setProperty("hibernate.hbm2ddl.auto", "update");//生产环境不要使用
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource);//设置DataSource
		//扫描指定的package获取所有的entity class
		entityManagerFactoryBean.setPackagesToScan(AbstractEntity.class.getPackage().getName());
		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		//指定JPA的提供商是Hibernate
		entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
		//设定提供商自己的配置
		entityManagerFactoryBean.setJpaProperties(props);
		return entityManagerFactoryBean;
	}
	
	@Bean
	PlatformTransactionManager createTxManager(@Autowired EntityManagerFactory entityManagerFactory) {
		return new JpaTransactionManager(entityManagerFactory);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		if (userService.fetchUserByEmail("bob@example.com") == null) {
			User bob = userService.register("bob@example.com", "bob123", "Bob");
			System.out.println("Registered ok:" + bob);
		}
		if (userService.fetchUserByEmail("alice@example.com") == null) {
			try {
				User alice = userService.register("alice@example.com", "helloalice", "Alice");
				System.out.println("Registered ok:" + alice);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		for(User u : userService.getUsers(1)) {
			System.out.println(u);
		}
		User bob = userService.login("bob@example.com", "bob123");
		System.out.println(bob);
		((ConfigurableApplicationContext)context).close();
	}
}
