package com.yale.test.spring.database.dao;

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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.yale.test.spring.database.dao.service.User;
import com.yale.test.spring.database.dao.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 使用DAO
 * 在传统的多层应用程序中，通常是Web层调用业务层，业务层调用数据访问层。业务层负责处理各种业务逻辑，而数据访问层只负责对数据进行增删改查。因此，实现数据访问层就是用JdbcTemplate实现对数据库的操作。
 * 编写数据访问层的时候，可以使用DAO模式。DAO即Data Access Object的缩写，它没有什么神秘之处，实现起来基本如下：
 * public class UserDao {
	    @Autowired
	    JdbcTemplate jdbcTemplate;
	    User getById(long id) {
	        ...
	    }
	    List<User> getUsers(int page) {
	        ...
	    }
	    User createUser(User user) {
	        ...
	    }
	    User updateUser(User user) {
	        ...
	    }
	    void deleteUser(User user) {
	        ...
	    }
	}
 * Spring提供了一个JdbcDaoSupport类，用于简化DAO的实现。这个JdbcDaoSupport没什么复杂的，核心代码就是持有一个JdbcTemplate：
 * public abstract class JdbcDaoSupport extends DaoSupport {
	    private JdbcTemplate jdbcTemplate;
	    public final void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
	        this.jdbcTemplate = jdbcTemplate;
	        initTemplateConfig();
	    }
	    public final JdbcTemplate getJdbcTemplate() {
	        return this.jdbcTemplate;
	    }
	    ...
	}
 * 它的意图是子类直接从JdbcDaoSupport继承后，可以随时调用getJdbcTemplate()获得JdbcTemplate的实例。那么问题来了：因为JdbcDaoSupport的jdbcTemplate字段没有标记@Autowired，所以，子类想要注入JdbcTemplate，还得自己想个办法：
 * @Component
	@Transactional
	public class UserDao extends JdbcDaoSupport {
	    @Autowired
	    JdbcTemplate jdbcTemplate;
	
	    @PostConstruct
	    public void init() {
	        super.setJdbcTemplate(jdbcTemplate);
	    }
	}
 * 有的童鞋可能看出来了：既然UserDao都已经注入了JdbcTemplate，那再把它放到父类里，通过getJdbcTemplate()访问岂不是多此一举？
 * 如果使用传统的XML配置，并不需要编写@Autowired JdbcTemplate jdbcTemplate，但是考虑到现在基本上是使用注解的方式，我们可以编写一个AbstractDao，专门负责注入JdbcTemplate：
 * public abstract class AbstractDao extends JdbcDaoSupport {
	    @Autowired
	    private JdbcTemplate jdbcTemplate;
	
	    @PostConstruct
	    public void init() {
	        super.setJdbcTemplate(jdbcTemplate);
	    }
	}
 * 这样，子类的代码就非常干净，可以直接调用getJdbcTemplate()：
 * @Component
	@Transactional
	public class UserDao extends AbstractDao {
	    public User getById(long id) {
	        return getJdbcTemplate().queryForObject(
	                "SELECT * FROM users WHERE id = ?",
	                new BeanPropertyRowMapper<>(User.class),
	                id
	        );
	    }
	    ...
	}
 * 倘若肯再多写一点样板代码，就可以把AbstractDao改成泛型，并实现getById()，getAll()，deleteById()这样的通用方法：
 * public abstract class AbstractDao<T> extends JdbcDaoSupport {
	    private String table;
	    private Class<T> entityClass;
	    private RowMapper<T> rowMapper;
	
	    public AbstractDao() {
	        // 获取当前类型的泛型类型:
	        this.entityClass = getParameterizedType();
	        this.table = this.entityClass.getSimpleName().toLowerCase() + "s";
	        this.rowMapper = new BeanPropertyRowMapper<>(entityClass);
	    }
	
	    public T getById(long id) {
	        return getJdbcTemplate().queryForObject("SELECT * FROM " + table + " WHERE id = ?", this.rowMapper, id);
	    }
	
	    public List<T> getAll(int pageIndex) {
	        int limit = 100;
	        int offset = limit * (pageIndex - 1);
	        return getJdbcTemplate().query("SELECT * FROM " + table + " LIMIT ? OFFSET ?",
	                new Object[] { limit, offset },
	                this.rowMapper);
	    }
	
	    public void deleteById(long id) {
	        getJdbcTemplate().update("DELETE FROM " + table + " WHERE id = ?", id);
	    }
	    ...
	}
 * 这样，每个子类就自动获得了这些通用方法：
 * @Component
	@Transactional
	public class UserDao extends AbstractDao<User> {
	    // 已经有了:
	    // User getById(long)
	    // List<User> getAll(int)
	    // void deleteById(long)
	}
	
	@Component
	@Transactional
	public class BookDao extends AbstractDao<Book> {
	    // 已经有了:
	    // Book getById(long)
	    // List<Book> getAll(int)
	    // void deleteById(long)
	}
 * 可见，DAO模式就是一个简单的数据访问模式，是否使用DAO，根据实际情况决定，因为很多时候，直接在Service层操作数据库也是完全没有问题的。
 * 小结
 * Spring提供了JdbcDaoSupport来便于我们实现DAO模式；
 * 可以基于泛型实现更通用、更简洁的DAO模式。
 */
@Configuration
@ComponentScan
@EnableTransactionManagement
@PropertySource("jdbc.properties")
public class AppConfig {
	
	@Value("${jdbc.url}")
	String jdbcUrl;
	@Value("${jdbc.username}")
	String jdbcUsername;
	@Value("${jdbc.password}")
	String jdbcPassword;
	
	@Bean
	DataSource createDataSource() {
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
	JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	@Bean
	TransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		if (userService.fetchUserByEmail("bob@example.com") == null)  {
			userService.register("bob@example.com", "password1", "Bob");
		}
		if (userService.fetchUserByEmail("alice@example.com") == null) {
			userService.register("alice@example.com", "password2", "Alice");
		}
		if (userService.fetchUserByEmail("tom@example.com") == null) {
			userService.register("tom@exampmle.com", "password3", "Tom");
		}
		try {
			userService.register("root@example.com", "password3", "root");
		} catch(RuntimeException e) {
			System.out.println(e.getMessage());
		}
		for (User u: userService.getUsers(1)) {
			System.out.println(u);
		}
		((ConfigurableApplicationContext)context).close();
	}
}
