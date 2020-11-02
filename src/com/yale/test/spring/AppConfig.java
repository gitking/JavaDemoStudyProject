package com.yale.test.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.yale.test.spring.lxf.service.User;
import com.yale.test.spring.lxf.service.UserService;

/*
 * 使用Annotation配置
 * 使用Spring的IoC容器，实际上就是通过类似XML这样的配置文件，把我们自己的Bean的依赖关系描述出来，然后让容器来创建并装配Bean。一旦容器初始化完毕，我们就直接从容器中获取Bean使用它们。
 * 使用XML配置的优点是所有的Bean都能一目了然地列出来，并通过配置注入能直观地看到每个Bean的依赖。它的缺点是写起来非常繁琐，每增加一个组件，就必须把新的Bean配置到XML中。
 * 有没有其他更简单的配置方式呢？
 * 有！我们可以使用Annotation配置，可以完全不需要XML，让Spring自动扫描Bean并组装它们。
 * 我们把上一节的示例改造一下，先删除XML配置文件，然后，给UserService和MailService添加几个注解。
 * 首先，我们给MailService添加一个@Component注解：
 * 这个@Component注解就相当于定义了一个Bean，它有一个可选的名称，默认是mailService，即小写开头的类名。
 * 然后，我们给UserService添加一个@Component注解和一个@Autowired注解：
 * 使用@Autowired就相当于把指定类型的Bean注入到指定的字段中。和XML配置相比，@Autowired大幅简化了注入，因为它不但可以写在set()方法上，还可以直接写在字段上，甚至可以写在构造方法中：
 * 我们一般把@Autowired写在字段上，通常使用package权限的字段，便于测试。
 * 最后，编写一个AppConfig类启动容器：
 * 除了main()方法外，AppConfig标注了@Configuration，表示它是一个配置类，因为我们创建ApplicationContext时：
 * ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
 * 使用的实现类是AnnotationConfigApplicationContext，必须传入一个标注了@Configuration的类名。
 * 此外，AppConfig还标注了@ComponentScan，它告诉容器，自动搜索当前类所在的包以及子包，把所有标注为@Component的Bean自动创建出来，并根据@Autowired进行装配。
 * 整个工程结构如下：
	spring-ioc-annoconfig
	├── pom.xml
	└── src
	    └── main
	        └── java
	            └── com
	                └── itranswarp
	                    └── learnjava
	                        ├── AppConfig.java
	                        └── service
	                            ├── MailService.java
	                            ├── User.java
	                            └── UserService.java
 * 使用Annotation配合自动扫描能大幅简化Spring的配置，我们只需要保证：
 * 1.每个Bean被标注为@Component并正确使用@Autowired注入；
 * 2.配置类被标注为@Configuration和@ComponentScan；
 * 3.所有Bean均在指定包以及子包内。
 * 使用@ComponentScan非常方便，但是，我们也要特别注意包的层次结构。通常来说，启动配置AppConfig位于自定义的顶层包（例如com.itranswarp.learnjava），其他Bean按类别放入子包。
 * 思考
 * 如果我们想给UserService注入HikariDataSource，但是这个类位于com.zaxxer.hikari包中，并且HikariDataSource也不可能有@Component注解，
 * 如何告诉IoC容器创建并配置HikariDataSource？或者换个说法，如何创建并配置一个第三方Bean？
 * 小结
 * 使用Annotation可以大幅简化配置，每个Bean通过@Component和@Autowired注入；
 * 必须合理设计包的层次结构，才能发挥@ComponentScan的威力。
 * 问:我自己实验的时候一直报错“@EnableAsync annotation metadata was not injected”，查了很久都说是@ComponentScan扫描范围的问题，最后加上了一个范围
 * @ComponentScan（value = “Service”），这个Service是我自己程序的一个包
 * 具体原理我也不是太清楚，如果有人遇到相同问题可以试试。我怀疑是我之前实验的时候下载到maven本地库中的spring包被扫描到了？？
 * 也希望廖老师能指导一下。
 * 请问楼上的花火同学你找到答案了吗？我跟你的错误一样一样的，加了Value = “Service”就好了
 * 我依然没有明白这个包的结构有啥问题。。。
 * 答:你注意这一句：特别注意包的层次结构
 * 不按规范组织包，扫描要么扫不到，要么扫太多
 * 问:廖老师，如果是util工具类，也要用@Component吗
 * 答:@Component相当于spring会对类进行实例化。但对于util工具类，我并不想实例化它，因为实例化相当于在堆上创建了新的对象，增加了内存占用。但这个工具类中又有@Autowired属性，有什么办法做到吗
 * 实例化一个Utils占内存可以忽略不计，但是注入是必须要实例的。可以配合构造方法实例化的时候注入并初始化静态字段：
 * @Component
		class Utils {
			static float interest;
		
			public Utils(@Value("${interest}") float theInterest) {
				interest = theInterest;
			}
		
			public static float calc(float f) {
				return f * interest;
			}
		}
 * 这样就可以正常调用静态方法
 */
@Configuration
@ComponentScan
public class AppConfig {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		User user = userService.login("bob@example.com", "password");
		System.out.println(user.getName());
	}
}
