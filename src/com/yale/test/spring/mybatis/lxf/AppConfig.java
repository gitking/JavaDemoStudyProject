package com.yale.test.spring.mybatis.lxf;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yale.test.spring.mybatis.lxf.entity.User;
import com.yale.test.spring.mybatis.lxf.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 集成MyBatis
 * 使用Hibernate或JPA操作数据库时，这类ORM干的主要工作就是把ResultSet的每一行变成Java Bean，或者把Java Bean自动转换到INSERT或UPDATE语句的参数中，从而实现ORM。
 * 而ORM框架之所以知道如何把行数据映射到Java Bean，是因为我们在Java Bean的属性上给了足够的注解作为元数据，ORM框架获取Java Bean的注解后，就知道如何进行双向映射。
 * 那么，ORM框架是如何跟踪Java Bean的修改，以便在update()操作中更新必要的属性？
 * 答案是使用Proxy模式，从ORM框架读取的User实例实际上并不是User类，而是代理类，代理类继承自User类，但针对每个setter方法做了覆写：
 * public class UserProxy extends User {
	    boolean _isNameChanged;
	
	    public void setName(String name) {
	        super.setName(name);
	        _isNameChanged = true;
	    }
	}
 * 这样，代理类可以跟踪到每个属性的变化。
 * 针对一对多或多对一关系时，代理类可以直接通过getter方法查询数据库：
 * public class UserProxy extends User {
    Session _session;
    boolean _isNameChanged;

    public void setName(String name) {
        super.setName(name);
        _isNameChanged = true;
    }
	     // 获取User对象关联的Address对象:
	    public Address getAddress() {
	        Query q = _session.createQuery("from Address where userId = :userId");
	        q.setParameter("userId", this.getId());
	        List<Address> list = query.list();
	        return list.isEmpty() ? null : list(0);
	    }
	}
 * 为了实现这样的查询，UserProxy必须保存Hibernate的当前Session。但是，当事务提交后，Session自动关闭，此时再获取getAddress()将无法访问数据库，或者获取的不是事务一致的数据。
 * 因此，ORM框架总是引入了Attached/Detached状态，表示当前此Java Bean到底是在Session的范围内，还是脱离了Session变成了一个“游离”对象。很多初学者无法正确理解状态变化和事务边界，就会造成大量的PersistentObjectException异常。这种隐式状态使得普通Java Bean的生命周期变得复杂。
 * 此外，Hibernate和JPA为了实现兼容多种数据库，它使用HQL或JPQL查询，经过一道转换，变成特定数据库的SQL，理论上这样可以做到无缝切换数据库，但这一层自动转换除了少许的性能开销外，给SQL级别的优化带来了麻烦。
 * 最后，ORM框架通常提供了缓存，并且还分为一级缓存和二级缓存。一级缓存是指在一个Session范围内的缓存，常见的情景是根据主键查询时，两次查询可以返回同一实例：
 * User user1 = session.load(User.class, 123);
 * User user2 = session.load(User.class, 123);
 * 二级缓存是指跨Session的缓存，一般默认关闭，需要手动配置。二级缓存极大的增加了数据的不一致性，原因在于SQL非常灵活，常常会导致意外的更新。例如：
 * // 线程1读取:
	User user1 = session1.load(User.class, 123);
	...
	// 一段时间后，线程2读取:
	User user2 = session2.load(User.class, 123);
 * 当二级缓存生效的时候，两个线程读取的User实例是一样的，但是，数据库对应的行记录完全可能被修改，例如：
 * -- 给老用户增加100积分:
 * UPDATE users SET bonus = bonus + 100 WHERE createdAt <= ?
 * ORM无法判断id=123的用户是否受该UPDATE语句影响。考虑到数据库通常会支持多个应用程序，此UPDATE语句可能由其他进程执行，ORM框架就更不知道了。
 * 我们把这种ORM框架称之为全自动ORM框架。
 * 对比Spring提供的JdbcTemplate，它和ORM框架相比，主要有几点差别：
 * 1.查询后需要手动提供Mapper实例以便把ResultSet的每一行变为Java对象；
 * 2.增删改操作所需的参数列表，需要手动传入，即把User实例变为[user.id, user.name, user.email]这样的列表，比较麻烦。
 * 但是JdbcTemplate的优势在于它的确定性：即每次读取操作一定是数据库操作而不是缓存，所执行的SQL是完全确定的，缺点就是代码比较繁琐，构造INSERT INTO users VALUES (?,?,?)更是复杂。
 * 所以，介于全自动ORM如Hibernate和手写全部如JdbcTemplate之间，还有一种半自动的ORM，它只负责把ResultSet自动映射到Java Bean，或者自动填充Java Bean参数，但仍需自己写出SQL。MyBatis就是这样一种半自动化ORM框架。
 * 我们来看看如何在Spring中集成MyBatis。
 * 首先，我们要引入MyBatis本身，其次，由于Spring并没有像Hibernate那样内置对MyBatis的集成，所以，我们需要再引入MyBatis官方自己开发的一个与Spring集成的库：
 * org.mybatis:mybatis:3.5.4
 * org.mybatis:mybatis-spring:2.0.4
 * 和前面一样，先创建DataSource是必不可少的：
 * 再回顾一下Hibernate和JPA的SessionFactory与EntityManagerFactory，MyBatis与之对应的是SqlSessionFactory和SqlSession：
 * 	JDBC		Hibernate		JPA						MyBatis
	DataSource	SessionFactory	EntityManagerFactory	SqlSessionFactory
	Connection	Session			EntityManager		    SqlSession
 * 可见，ORM的设计套路都是类似的。使用MyBatis的核心就是创建SqlSessionFactory，这里我们需要创建的是SqlSessionFactoryBean：
 * 因为MyBatis可以直接使用Spring管理的声明式事务，因此，创建事务管理器和使用JDBC是一样的：
 * 和Hibernate不同的是，MyBatis使用Mapper来实现映射，而且Mapper必须是接口。我们以User类为例，在User类和users表之间映射的UserMapper编写如下：
 * public interface UserMapper {
		@Select("SELECT * FROM users WHERE id = #{id}")
		User getById(@Param("id") long id);
	}
 * 注意：这里的Mapper不是JdbcTemplate的RowMapper的概念，它是定义访问users表的接口方法。比如我们定义了一个User getById(long)的主键查询方法，不仅要定义接口方法本身，还要明确写出查询的SQL，这里用注解@Select标记。SQL语句的任何参数，都与方法参数按名称对应。
 * 例如，方法参数id的名字通过注解@Param()标记为id，则SQL语句里将来替换的占位符就是#{id}。
 * 如果有多个参数，那么每个参数命名后直接在SQL中写出对应的占位符即可：
 * @Select("SELECT * FROM users LIMIT #{offset}, #{maxResults}")
 * List<User> getAll(@Param("offset") int offset, @Param("maxResults") int maxResults);
 * 注意：MyBatis执行查询后，将根据方法的返回类型自动把ResultSet的每一行转换为User实例，转换规则当然是按列名和属性名对应。如果列名和属性名不同，最简单的方式是编写SELECT语句的别名：
 * -- 列名是created_time，属性名是createdAt:
 * SELECT id, name, email, created_time AS createdAt FROM users
 * 执行INSERT语句就稍微麻烦点，因为我们希望传入User实例，因此，定义的方法接口与@Insert注解如下：
 * @Insert("INSERT INTO users (email, password, name, createdAt) VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
 * void insert(@Param("user") User user);
 * 上述方法传入的参数名称是user，参数类型是User类，在SQL中引用的时候，以#{obj.property}的方式写占位符。和Hibernate这样的全自动化ORM相比，MyBatis必须写出完整的INSERT语句。
 * 如果users表的id是自增主键，那么，我们在SQL中不传入id，但希望获取插入后的主键，需要再加一个@Options注解：
 * @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	@Insert("INSERT INTO users (email, password, name, createdAt) VALUES (#{user.email}, #{user.password}, #{user.name}, #{user.createdAt})")
	void insert(@Param("user") User user);
 * keyProperty和keyColumn分别指出JavaBean的属性和数据库的主键列名。
 * 执行UPDATE和DELETE语句相对比较简单，我们定义方法如下：
 * @Update("UPDATE users SET name = #{user.name}, createdAt = #{user.createdAt} WHERE id = #{user.id}")
	void update(@Param("user") User user);
	
	@Delete("DELETE FROM users WHERE id = #{id}")
	void deleteById(@Param("id") long id); 
 * 有了UserMapper接口，还需要对应的实现类才能真正执行这些数据库操作的方法。虽然可以自己写实现类，但我们除了编写UserMapper接口外，还有BookMapper、BonusMapper……一个一个写太麻烦，
 * 因此，MyBatis提供了一个MapperFactoryBean来自动创建所有Mapper的实现类。可以用一个简单的注解来启用它：
 * @MapperScan("com.itranswarp.learnjava.mapper")
	...其他注解...
	public class AppConfig {
	    ...
	}
 * 有了@MapperScan，就可以让MyBatis自动扫描指定包的所有Mapper并创建实现类。在真正的业务逻辑中，我们可以直接注入：
 * @Component
	@Transactional
	public class UserService {
	    // 注入UserMapper:
	    @Autowired
	    UserMapper userMapper;
	
	    public User getUserById(long id) {
	        // 调用Mapper方法:
	        User user = userMapper.getById(id);
	        if (user == null) {
	            throw new RuntimeException("User not found by id.");
	        }
	        return user;
	    }
	}
 * 可见，业务逻辑主要就是通过XxxMapper定义的数据库方法来访问数据库。
 * XML配置
 * 上述在Spring中集成MyBatis的方式，我们只需要用到注解，并没有任何XML配置文件。MyBatis也允许使用XML配置映射关系和SQL语句，例如，更新User时根据属性值构造动态SQL：
 * <update id="updateUser">
	  UPDATE users SET
	  <set>
	    <if test="user.name != null"> name = #{user.name} </if>
	    <if test="user.hobby != null"> hobby = #{user.hobby} </if>
	    <if test="user.summary != null"> summary = #{user.summary} </if>
	  </set>
	  WHERE id = #{user.id}
	</update>
 * 编写XML配置的优点是可以组装出动态SQL，并且把所有SQL操作集中在一起。缺点是配置起来太繁琐，调用方法时如果想查看SQL还需要定位到XML配置中。这里我们不介绍XML的配置方式，
 * 需要了解的童鞋请自行阅读官方文档(https://mybatis.org/mybatis-3/zh/configuration.html)。
 * 使用MyBatis最大的问题是所有SQL都需要全部手写，优点是执行的SQL就是我们自己写的SQL，对SQL进行优化非常简单，也可以编写任意复杂的SQL，或者使用数据库的特定语法，但切换数据库可能就不太容易。
 * 好消息是大部分项目并没有切换数据库的需求，完全可以针对某个数据库编写尽可能优化的SQL。
 * 小结
 * MyBatis是一个半自动化的ORM框架，需要手写SQL语句，没有自动加载一对多或多对一关系的功能。
 * 问:看完后有个疑问：SqlSessionFactoryBean 这个好像没有显示的用在哪里，不用注入到哪个类吗，然后通过这个类操作数据库？像@Select这样子的注解，是自动连接到数据的吗？
 * 答:注意这个@MapperScan("com.itranswarp.learnjava.mapper")MyBatis在启动时自动给每个Mapper接口创建如下Bean：
 * @Component
	public class UserMapperImpl implements UserMapper {
	    @Autowired
	    SqlSessionFactory sessionFactory;
	
	    public List<User> getAllUsers() {
	        String sql = getSqlFromAnnotation(...);
	        try (SqlSession session = sessionFactory.createSession()) {
	            ...
	        }
	    }
	}
 * Spring允许动态创建Bean并添加到applicationContext中
 */
@Configuration
@ComponentScan
//有了UserMapper接口，还需要对应的实现类才能真正执行这些数据库操作的方法。虽然可以自己写实现类，但我们除了编写UserMapper接口外，还有BookMapper、BonusMapper……一个一个写太麻烦，
//因此，MyBatis提供了一个MapperFactoryBean来自动创建所有Mapper的实现类。可以用一个简单的注解来启用它：
//@MapperScan("com.itranswarp.learnjava.mapper")
//有了@MapperScan，就可以让MyBatis自动扫描指定包的所有Mapper并创建实现类。在真正的业务逻辑中，我们可以直接注入：
@MapperScan("com.yale.test.spring.mybatis.lxf.mapper")
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		if (userService.fetchUserByEmail("bob@example.com") == null) {
			User bob = userService.register("bob@example.com", "bob123", "Bob");
			System.out.println("Registered ok:" + bob);
		}
		if (userService.fetchUserByEmail("alice@example.com") == null) {
			User alice = userService.register("alice@example.com", "helloalice", "Alice");
			System.out.println("Registered ok:" + alice);
		}
		
		if (userService.fetchUserByEmail("tom@example.com") == null) {
			User tom = userService.register("tom@example.com", "tomcat", "Alice");
			System.out.println("Registered ok:" + tom);
		}
		
		for (User u: userService.getUsers(1)) {
			System.out.println(u);
		}
		System.out.println("login...");
		
		User tom = userService.login("tom@example.com", "tomcat");
		System.out.println(tom);
		((ConfigurableApplicationContext)context).close();
	}
	
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
	SqlSessionFactoryBean createSqlSessionFactoryBean(@Autowired DataSource dataSource) {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setDataSource(dataSource);
		return sqlSessionFactoryBean;
	}
	
	@Bean
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
