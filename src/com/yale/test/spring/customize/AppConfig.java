package com.yale.test.spring.customize;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/*
 * 定制Bean
 * Scope:对于Spring容器来说，当我们把一个Bean标记为@Component后，它就会自动为我们创建一个单例（Singleton），即容器初始化时创建Bean，
 * 容器关闭前销毁Bean。在容器运行期间，我们调用getBean(Class)获取到的Bean总是同一个实例。
 * 还有一种Bean，我们每次调用getBean(Class)，容器都返回一个新的实例，这种Bean称为Prototype（原型），
 * 它的生命周期显然和Singleton不同。声明一个Prototype的Bean时，需要添加一个额外的@Scope注解：如MailSession
 * 注入List
 * 有些时候，我们会有一系列接口相同，不同实现类的Bean。例如，注册用户时，我们要对email、password和name这3个变量进行验证。
 * 为了便于扩展，我们先定义验证接口：Validator 
 * 然后，分别使用3个Validator对用户参数进行验证：
 * EmailValidator, PasswordValidator, NameValidator 
 * 最后，我们通过一个Validators作为入口进行验证：
 * 注意到Validators被注入了一个List<Validator>，Spring会自动把所有类型为Validator的Bean装配为一个List注入进来，这样一来，我们每新增一个Validator类型，就自动被Spring装配到Validators中了，非常方便。
 * 因为Spring是通过扫描classpath获取到所有的Bean，而List是有序的，要指定List中Bean的顺序，可以加上@Order注解：
 * 可选注入
 * 默认情况下，当我们标记了一个@Autowired后，Spring如果没有找到对应类型的Bean，它会抛出NoSuchBeanDefinitionException异常。
 * 可以给@Autowired增加一个required = false的参数：MailService
 * 这个参数告诉Spring容器，如果找到一个类型为ZoneId的Bean，就注入，如果找不到，就忽略。
 * 这种方式非常适合有定义就使用定义，没有就使用默认值的情况。
 * 创建第三方Bean
 * 如果一个Bean不在我们自己的package管理之内，例如ZoneId，如何创建它？
 * 答案是我们自己在@Configuration类中编写一个Java方法创建并返回它，注意给方法标记一个@Bean注解：
 * Spring对标记为@Bean的方法只调用一次，因此返回的Bean仍然是单例。
 * 初始化和销毁
 * 有些时候，一个Bean在注入必要的依赖后，需要进行初始化（监听消息等）。在容器关闭时，有时候还需要清理资源（关闭连接池等）。
 * 我们通常会定义一个init()方法进行初始化，定义一个shutdown()方法进行清理，然后，引入JSR-250定义的Annotation：
 * <dependency>
	    <groupId>javax.annotation</groupId>
	    <artifactId>javax.annotation-api</artifactId>
	    <version>1.3.2</version>
	</dependency>
 * 在Bean的初始化和清理方法上标记@PostConstruct和@PreDestroy：
 * Spring容器会对上述MailService Bean做如下初始化流程：
 * 1.调用构造方法创建MailService实例
 * 2.根据@Autowired进行注入；
 * 3.调用标记有@PostConstruct的init()方法进行初始化。
 * 而销毁时，容器会首先调用标记有@PreDestroy的shutdown()方法。
 * 要手动调用applicationContext.close()不能直接点结束进程
 * Spring只根据Annotation查找无参数方法，对方法名不作要求。
 * 使用别名
 * 默认情况下，对一种类型的Bean，容器只创建一个实例。但有些时候，我们需要对一种类型的Bean创建多个实例。例如，同时连接多个数据库，就必须创建多个DataSource实例。
 * 如果我们在@Configuration类中创建了多个同类型的Bean：
 * @Configuration
	@ComponentScan
	public class AppConfig {
	    @Bean
	    ZoneId createZoneOfZ() {
	        return ZoneId.of("Z");
	    }
	
	    @Bean
	    ZoneId createZoneOfUTC8() {
	        return ZoneId.of("UTC+08:00");
	    }
	}
 * Spring会报NoUniqueBeanDefinitionException异常，意思是出现了重复的Bean定义。
 * 这个时候，需要给每个Bean添加不同的名字：
 * @Configuration
	@ComponentScan
	public class AppConfig {
	    @Bean("z")
	    ZoneId createZoneOfZ() {
	        return ZoneId.of("Z");
	    }
	
	    @Bean
	    @Qualifier("utc8")
	    ZoneId createZoneOfUTC8() {
	        return ZoneId.of("UTC+08:00");
	    }
	}
 * 可以用@Bean("name")指定别名，也可以用@Bean+@Qualifier("name")指定别名。
 * 存在多个同类型的Bean时，注入ZoneId又会报错：
 * NoUniqueBeanDefinitionException: No qualifying bean of type 'java.time.ZoneId' available: expected single matching bean but found 2
 * 意思是期待找到唯一的ZoneId类型Bean，但是找到两。因此，注入时，要指定Bean的名称：
 * @Component
	public class MailService {
		@Autowired(required = false)
		@Qualifier("z") // 指定注入名称为"z"的ZoneId
		ZoneId zoneId = ZoneId.systemDefault();
	    ...
	}
 * 还有一种方法是把其中某个Bean指定为@Primary：
 * @Configuration
	@ComponentScan
	public class AppConfig {
	    @Bean
	    @Primary // 指定为主要Bean
	    @Qualifier("z")
	    ZoneId createZoneOfZ() {
	        return ZoneId.of("Z");
	    }
	
	    @Bean
	    @Qualifier("utc8")
	    ZoneId createZoneOfUTC8() {
	        return ZoneId.of("UTC+08:00");
	    }
	}
 * 这样，在注入时，如果没有指出Bean的名字，Spring会注入标记有@Primary的Bean。这种方式也很常用。例如，对于主从两个数据源，通常将主数据源定义为@Primary：
 * 其他Bean默认注入的就是主数据源。如果要注入从数据源，那么只需要指定名称即可。
 * 使用FactoryBean
 * 我们在设计模式的工厂方法中讲到，很多时候，可以通过工厂模式创建对象。Spring也提供了工厂模式，允许定义一个工厂，然后由工厂创建真正的Bean。
 * 用工厂模式创建Bean需要实现FactoryBean接口。我们观察下面的代码：
 * @Component
	public class ZoneIdFactoryBean implements FactoryBean<ZoneId> {
	
	    String zone = "Z";
	
	    @Override
	    public ZoneId getObject() throws Exception {
	        return ZoneId.of(zone);
	    }
	
	    @Override
	    public Class<?> getObjectType() {
	        return ZoneId.class;
	    }
	}
 * 当一个Bean实现了FactoryBean接口后，Spring会先实例化这个工厂，然后调用getObject()创建真正的Bean。getObjectType()可以指定创建的Bean的类型，因为指定类型不一定与实际类型一致，可以是接口或抽象类。
 * 因此，如果定义了一个FactoryBean，要注意Spring创建的Bean实际上是这个FactoryBean的getObject()方法返回的Bean。为了和普通Bean区分，我们通常都以XxxFactoryBean命名。
 * 小结
 * Spring默认使用Singleton创建Bean，也可指定Scope为Prototype；
 * 可将相同类型的Bean注入List；
 * 可用@Autowired(required=false)允许可选注入；
 * 可用带@Bean标注的方法创建Bean；
 * 可使用@PostConstruct和@PreDestroy对Bean进行初始化和清理；
 * 相同类型的Bean只能有一个指定为@Primary，其他必须用@Quanlifier("beanName")指定别名；
 * 注入时，可通过别名@Quanlifier("beanName")指定某个Bean；
 * 可以定义FactoryBean来使用工厂模式创建Bean。
 * 
 * 问:给bean添加别名用@Bean(name="xxx")，指定注入bean的名称用@Qualifier("xxx")
 * 答:用@Bean(name="xxx")也可以的
 * 问:熟悉了就好了，我现在也在想：要不要重新再读读廖老师的Maven，我还没完全搞清楚classpath路径问题，
 * 尤其在eclipse项目中，那个@Value("classpath:/resources/logo.txt")，到底在哪？
 */
@Configuration
@ComponentScan
public class AppConfig {
	public static void main(String[] args) {
		
	}
	
	//@Bean("z"),可以用@Bean("name")指定别名，也可以用@Bean+@Qualifier("name")指定别名。
	@Bean// 创建一个Bean:Spring对标记为@Bean的方法只调用一次，因此返回的Bean仍然是单例。
	@Primary//@Primary指定为主要Bean,这样，在注入时，如果没有指出Bean的名字，Spring会注入标记有@Primary的Bean。这种方式也很常用。例如，对于主从两个数据源，通常将主数据源定义为@Primary：
	@Qualifier("z")
	ZoneId createZoneOfZ() {
		return ZoneId.of("Z");
	}
	
	@Bean
	@Qualifier("utc8")
	ZoneId createZoneOfUTC8() {
		return ZoneId.of("UTC+08:00");
	}
}
