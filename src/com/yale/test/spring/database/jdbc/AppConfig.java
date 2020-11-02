package com.yale.test.spring.database.jdbc;

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

import com.yale.test.spring.database.jdbc.service.User;
import com.yale.test.spring.database.jdbc.service.UserService;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 使用JDBC
 * 我们在前面介绍JDBC编程时已经讲过，Java程序使用JDBC接口访问关系数据库的时候，需要以下几步：
 * 1.创建全局DataSource实例，表示数据库连接池；
 * 2.在需要读写数据库的方法内部，按如下步骤访问数据库：
 * 		从全局DataSource实例获取Connection实例；
 * 		通过Connection实例创建PreparedStatement实例；
 * 		执行SQL语句，如果是查询，则通过ResultSet读取结果集，如果是修改，则获得int结果。
 * 正确编写JDBC代码的关键是使用try ... finally释放资源，涉及到事务的代码需要正确提交或回滚事务。
 * 在Spring使用JDBC，首先我们通过IoC容器创建并管理一个DataSource实例，然后，Spring提供了一个JdbcTemplate，
 * 可以方便地让我们操作JDBC，因此，通常情况下，我们会实例化一个JdbcTemplate。顾名思义，这个类主要使用了Template模式。
 * 编写示例代码或者测试代码时，我们强烈推荐使用HSQLDB(http://hsqldb.org/)这个数据库，它是一个用Java编写的关系数据库，可以以内存模式或者文件模式运行，
 * 本身只有一个jar包，非常适合演示代码或者测试代码。
 * 我们以实际工程为例，先创建Maven工程spring-data-jdbc，然后引入以下依赖：
 * <dependencies>
	    <dependency>
	        <groupId>org.springframework</groupId>
	        <artifactId>spring-context</artifactId>
	        <version>5.2.0.RELEASE</version>
	    </dependency>
	    <dependency>
	        <groupId>org.springframework</groupId>
	        <artifactId>spring-jdbc</artifactId>
	        <version>5.2.0.RELEASE</version>
	    </dependency>
	    <dependency>
	        <groupId>javax.annotation</groupId>
	        <artifactId>javax.annotation-api</artifactId>
	        <version>1.3.2</version>
	    </dependency>
	    <dependency>
	        <groupId>com.zaxxer</groupId>
	        <artifactId>HikariCP</artifactId>
	        <version>3.4.2</version>
	    </dependency>
	    <dependency>
	        <groupId>org.hsqldb</groupId>
	        <artifactId>hsqldb</artifactId>
	        <version>2.5.0</version>
	    </dependency>
	</dependencies>
 * 在AppConfig中，我们需要创建以下几个必须的Bean：
 * 最后，针对HSQLDB写一个配置文件jdbc.properties：
 * 可以通过HSQLDB自带的工具来初始化数据库表，这里我们写一个Bean:DatabaseInitializer，在Spring容器启动时自动创建一个users表：
 * 现在，所有准备工作都已完毕。我们只需要在需要访问数据库的Bean中，注入JdbcTemplate即可：
 */
@Configuration
@ComponentScan
@PropertySource("jdbc.properties")//通过@PropertySource("jdbc.properties")读取数据库配置文件
public class AppConfig {
	@Value("${jdbc.url}")//通过@Value("${jdbc.url}")注入配置文件的相关配置；
	String jdbcUrl;
	@Value("${jdbc.username}")
	String jdbcUsername;
	@Value("${jdbc.password}")
	String jdbcPassword;
	
	@Bean
	DataSource createDataSource() {//创建一个DataSource实例，它的实际类型是HikariDataSource，创建时需要用到注入的配置；
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(jdbcUsername);
		config.setUsername(jdbcPassword);
		config.addDataSourceProperty("autoCommit", "true");
		config.addDataSourceProperty("connectionTimeout", "5");
		config.addDataSourceProperty("idleTimeout", "60");
		return new HikariDataSource(config);
	}
	
	@Bean//创建一个JdbcTemplate实例，它需要注入DataSource，这是通过方法参数完成注入的。
	JdbcTemplate createJdbcTemplate(@Autowired DataSource datasource) {
		return new JdbcTemplate(datasource);
	}
	
	/*
	 * 不可第二次运行
	 * 我发现这一章的代码实例里都会因为“用户重复”的原因不可重复正常运行，会报错。
	 * 在DatabaseInitializer里加入清理方法即可解决
	 * 我又去复现了一遍，运行会产生四个新文件：
	 * testdb.log
	 * testdb.properties
	 * testdb.script
	 * testdb.tmp 或者testdb.lck
	 * 删掉这四个新文件也可正常运行
	 */
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		userService.register("bob@example.com", "password1", "Bob");
		userService.register("alice@example.com", "password2", "Alice");
		User bob = userService.getUserByName("Bob");
		System.out.println(bob);
		
		User tom = userService.register("tom@example.com", "password3", "Tom");
		System.out.println(tom);
		System.out.println("Total: " + userService.getUsers());
		for (User u: userService.getUsers(1)) {
			System.out.println(u);
		}
		((ConfigurableApplicationContext)context).close();
	}
}
