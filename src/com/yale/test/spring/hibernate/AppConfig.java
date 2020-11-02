package com.yale.test.spring.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yale.test.spring.hibernate.entity.AbstractEntity;
import com.yale.test.spring.hibernate.entity.User;
import com.yale.test.spring.hibernate.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 集成Hibernate
 * 使用JdbcTemplate的时候，我们用得最多的方法就是List<T> query(String sql, Object[] args, RowMapper rowMapper)。这个RowMapper的作用就是把ResultSet的一行记录映射为Java Bean。
 * 这种把关系数据库的表记录映射为Java对象的过程就是ORM：Object-Relational Mapping。ORM既可以把记录转换成Java对象，也可以把Java对象转换为行记录。
 * 使用JdbcTemplate配合RowMapper可以看作是最原始的ORM。如果要实现更自动化的ORM，可以选择成熟的ORM框架，例如Hibernate(https://hibernate.org/)。
 * 我们来看看如何在Spring中集成Hibernate。
 * Hibernate作为ORM框架，它可以替代JdbcTemplate，但Hibernate仍然需要JDBC驱动，所以，我们需要引入JDBC驱动、连接池，以及Hibernate本身。在Maven中，我们加入以下依赖项：
 * <!-- JDBC驱动，这里使用HSQLDB -->
	<dependency>
	    <groupId>org.hsqldb</groupId>
	    <artifactId>hsqldb</artifactId>
	    <version>2.5.0</version>
	</dependency>
	
	<!-- JDBC连接池 -->
	<dependency>
	    <groupId>com.zaxxer</groupId>
	    <artifactId>HikariCP</artifactId>
	    <version>3.4.2</version>
	</dependency>
	
	<!-- Hibernate -->
	<dependency>
	    <groupId>org.hibernate</groupId>
	    <artifactId>hibernate-core</artifactId>
	    <version>5.4.2.Final</version>
	</dependency>
	
	<!-- Spring Context和Spring ORM -->
	<dependency>
	    <groupId>org.springframework</groupId>
	    <artifactId>spring-context</artifactId>
	    <version>5.2.0.RELEASE</version>
	</dependency>
	<dependency>
	    <groupId>org.springframework</groupId>
	    <artifactId>spring-orm</artifactId>
	    <version>5.2.0.RELEASE</version>
	</dependency>
 * 在AppConfig中，我们仍然需要创建DataSource、引入JDBC配置文件，以及启用声明式事务：
 * 为了启用Hibernate，我们需要创建一个LocalSessionFactoryBean：
 * 注意我们在定制Bean中讲到过FactoryBean，LocalSessionFactoryBean是一个FactoryBean，它会再自动创建一个SessionFactory，在Hibernate中，
 * Session是封装了一个JDBC Connection的实例，而SessionFactory是封装了JDBC DataSource的实例，即SessionFactory持有连接池，每次需要操作数据库的时候，
 * SessionFactory创建一个新的Session，相当于从连接池获取到一个新的Connection。SessionFactory就是Hibernate提供的最核心的一个对象，
 * 但LocalSessionFactoryBean是Spring提供的为了让我们方便创建SessionFactory的类。
 * 注意到上面创建LocalSessionFactoryBean的代码，首先用Properties持有Hibernate初始化SessionFactory时用到的所有设置，
 * 常用的设置请参考Hibernate文档(https://docs.jboss.org/hibernate/orm/5.4/userguide/html_single/Hibernate_User_Guide.html#configurations)，这里我们只定义了3个设置：
 * 1.hibernate.hbm2ddl.auto=update：表示自动创建数据库的表结构，注意不要在生产环境中启用；
 * 2.hibernate.dialect=org.hibernate.dialect.HSQLDialect：指示Hibernate使用的数据库是HSQLDB。Hibernate使用一种HQL的查询语句，它和SQL类似，但真正在“翻译”成SQL时，会根据设定的数据库“方言”来生成针对数据库优化的SQL；
 * 3.hibernate.show_sql=true：让Hibernate打印执行的SQL，这对于调试非常有用，我们可以方便地看到Hibernate生成的SQL语句是否符合我们的预期。
 * 除了设置DataSource和Properties之外，注意到setPackagesToScan()我们传入了一个package名称，它指示Hibernate扫描这个包下面的所有Java类，
 * 自动找出能映射为数据库表记录的JavaBean。后面我们会仔细讨论如何编写符合Hibernate要求的JavaBean。
 * 紧接着，我们还需要创建HibernateTemplate以及HibernateTransactionManager：
 * 这两个Bean的创建都十分简单。HibernateTransactionManager是配合Hibernate使用声明式事务所必须的，而HibernateTemplate则是Spring为了便于我们使用Hibernate提供的工具类，不是非用不可，但推荐使用以简化代码。
 * 到此为止，所有的配置都定义完毕，我们来看看如何将数据库表结构映射为Java对象。
 * 考察如下的数据库表：
	 * CREATE TABLE user
	    id BIGINT NOT NULL AUTO_INCREMENT,
	    email VARCHAR(100) NOT NULL,
	    password VARCHAR(100) NOT NULL,
	    name VARCHAR(100) NOT NULL,
	    createdAt BIGINT NOT NULL,
	    PRIMARY KEY (`id`),
	    UNIQUE KEY `email` (`email`)
	);
 * 其中，id是自增主键，email、password、name是VARCHAR类型，email带唯一索引以确保唯一性，createdAt存储整型类型的时间戳。用JavaBean的User表示如下：
 * 这种映射关系十分易懂，但我们需要添加一些注解来告诉Hibernate如何把User类映射到表记录：
 * 如果一个JavaBean被用于映射，我们就标记一个@Entity。默认情况下，映射的表名是user，如果实际的表名不同，例如实际表名是users，可以追加一个@Table(name="users")表示：
 * @Entity
	@Table(name="users)
	public class User {
	    ...
	}
 *  每个属性到数据库列的映射用@Column()标识，nullable指示列是否允许为NULL，updatable指示该列是否允许被用在UPDATE语句，length指示String类型的列的长度（如果没有指定，默认是255）。
 *  对于主键，还需要用@Id标识，自增主键再追加一个@GeneratedValue，以便Hibernate能读取到自增主键的值。
 *  细心的童鞋可能还注意到，主键id定义的类型不是long，而是Long。这是因为Hibernate如果检测到主键为null，就不会在INSERT语句中指定主键的值，而是返回由数据库生成的自增值，
 *  否则，Hibernate认为我们的程序指定了主键的值，会在INSERT语句中直接列出。long型字段总是具有默认值0，因此，每次插入的主键值总是0，导致除第一次外后续插入都将失败。
 *  createdAt虽然是整型，但我们并没有使用long，而是Long，这是因为使用基本类型会导致某种查询会添加意外的条件，后面我们会详细讨论，这里只需牢记，作为映射使用的JavaBean，所有属性都使用包装类型而不是基本类型。
 *  使用Hibernate时，不要使用基本类型的属性，总是使用包装类型，如Long或Integer。 
 *  类似的，我们再定义一个Book类：
 *  如果仔细观察User和Book，会发现它们定义的id、createdAt属性是一样的，这在数据库表结构的设计中很常见：对于每个表，通常我们会统一使用一种主键生成机制，并添加createdAt表示创建时间，updatedAt表示修改时间等通用字段。
 *  不必在User和Book中重复定义这些通用字段，我们可以把它们提到一个抽象类中：
 *  对于AbstractEntity来说，我们要标注一个@MappedSuperclass表示它用于继承。此外，注意到我们定义了一个@Transient方法，它返回一个“虚拟”的属性。
 *  因为getCreatedDateTime()是计算得出的属性，而不是从数据库表读出的值，因此必须要标注@Transient，否则Hibernate会尝试从数据库读取名为createdDateTime这个不存在的字段从而出错。
 *  再注意到@PrePersist标识的方法，它表示在我们将一个JavaBean持久化到数据库之前（即执行INSERT语句），Hibernate会先执行该方法，这样我们就可以自动设置好createdAt属性。
 *  有了AbstractEntity，我们就可以大幅简化User和Book：
 *  注意到使用的所有注解均来自javax.persistence，它是JPA规范的一部分。这里我们只介绍使用注解的方式配置Hibernate映射关系，不再介绍传统的比较繁琐的XML配置。
 *  通过Spring集成Hibernate时，也不再需要hibernate.cfg.xml配置文件，用一句话总结：使用Spring集成Hibernate，配合JPA注解，无需任何额外的XML配置。 
 *  类似User、Book这样的用于ORM的Java Bean，我们通常称之为Entity Bean。
 *  最后，我们来看看如果对user表进行增删改查。因为使用了Hibernate，因此，我们要做的，实际上是对User这个JavaBean进行“增删改查”。我们编写一个UserService，注入HibernateTemplate以便简化代码：
 *  Insert操作
 *  要持久化一个User实例，我们只需调用save()方法。以register()方法为例，代码如下：
 *  小结
 *  在Spring中集成Hibernate需要配置的Bean如下：
 *  DataSource
 *  LocalSessionFactory
 *  HibernateTransactionManager
 *  HibernateTemplate（推荐）。
 *  推荐使用Annotation配置所有的Entity Bean。
 *  关于使用hibernate映射继承关系可以参考这篇文章
 *  如果需要将多个实体类映射到数据库，而这多个实体类继承自同一个抽象类或者接口，而查询或者别的操作又需要使用这种多态性，可以参考这篇文章
 *  https://thorben-janssen.com/complete-guide-inheritance-strategies-jpa-hibernate/
 *  关于使用hibernate映射组合关系可以参考这篇文章
 *  里面讨论了一对一、一对多、多对多、多对一等各种情况。
 *  如果实体类中有一个字段是一个集合，就可以参考这篇文章。
 *  https://thorben-janssen.com/ultimate-guide-association-mappings-jpa-hibernate/
 *  
 *  安利一下@Embeddable标签
 *  老师讲了继承复用表的问题，但是如果多个类共同持有一个类，比如学生这个类中有一个字段是联系方式，老师这个类中也有一个字段是联系方式，联系方式本身又是一个类，包括姓名，电话等。
 *  这时候想把学生的信息，包括他的联系方式里的所有信息都存储到一个表中，这种情况就可以用@Embeddable标签。首先用@Embeddable标签标记在联系方式上，
 *  然后用@Embedded标记在学生类里对应联系方式的字段上，就可以将学生类中联系方式字段的信息映射到学生这个表中。
 *  https://memorynotfound.com/hibernate-embeddable-embedded-annotation-example/
 *  
 *  问:廖老师可以讲讲bean的生命周期吗？
 *  答:太复杂了，你就记住几点：读取出来的bean只在同一个事务里修改；出了事务范围就不要修改，更不要重新传到另一个事务里改；
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
	
	@Bean
	DataSource createDataSource(@Value("${jdbc.url}") String jdbcUrl, @Value("${jdbc.username}") String jdbcUsername,
			@Value("${jdbc.password}") String jdbcPassword) {
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
	LocalSessionFactoryBean createSessionFactory(@Autowired DataSource dataSource) {
		Properties props = new Properties();
		props.setProperty("hibernate.hbm2ddl.auto", "update");//生产环境不要使用
		props.setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		props.setProperty("hibernate.show_sql", "true");
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		//扫描指定的package获取所有的entity class.
		//sessionFactoryBean.setPackagesToScan(AbstractEntity.class.getPackageName());jdk8没有getPackageName这个方法
		sessionFactoryBean.setPackagesToScan(AbstractEntity.class.getPackage().getName());// 扫描指定的package获取所有entity class:
		sessionFactoryBean.setHibernateProperties(props);
		return sessionFactoryBean;
	}
	
	@Bean
	HibernateTemplate createHibernateTemplate(@Autowired SessionFactory sessionFactory) {
		return new HibernateTemplate(sessionFactory);
	}
	
	@Bean
	PlatformTransactionManager createTxManager(@Autowired SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		if (userService.fetchUserByEmail("bob@example.com") == null) {
			User bob = userService.register("bob@example.com", "bob123", "Bon");
			System.out.println("Registered ok:" + bob);
		}
		
		if (userService.fetchUserByEmail("alice@example.com") == null) {
			User alice = userService.register("alice@example.com", "helloalice", "Bob");
			System.out.println("Registered ok:" + alice);
		}
		
		for (User u : userService.getUsers(1)) {
			System.out.println(u);
		}
		
		User bob = userService.login("bob@example.com", "bob123");
		System.out.println(bob);
		((ConfigurableApplicationContext)context).close();
	}
}
