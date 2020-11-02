package com.yale.test.spring.resource.prop;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/*
 * 注入配置
 * 在开发应用程序时，经常需要读取配置文件。最常用的配置方法是以key=value的形式写在.properties文件中。
 * 例如，MailService根据配置的app.zone=Asia/Shanghai来决定使用哪个时区。要读取配置文件，
 * 我们可以使用上一节讲到的Resource来读取位于classpath下的一个app.properties文件。但是，这样仍然比较繁琐。
 * Spring容器还提供了一个更简单的@PropertySource来自动读取配置文件。我们只需要在@Configuration配置类上再添加一个注解：
 * Spring容器看到@PropertySource("app.properties")注解后，自动读取这个配置文件，然后，我们使用@Value正常注入：
 * @Value("${app.zone:Z}")
 * String zoneId;
 * 注意注入的字符串语法，它的格式如下：
 * 1."${app.zone}"表示读取key为app.zone的value，如果key不存在，启动将报错；
 * 2."${app.zone:Z}"表示读取key为app.zone的value，但如果key不存在，就使用默认值Z。
 * 这样一来，我们就可以根据app.zone的配置来创建ZoneId。
 * 还可以把注入的注解写到方法参数中:
 * @Bean
	ZoneId createZoneId(@Value("${app.zone:Z}") String zoneId) {
	    return ZoneId.of(zoneId);
	}
 * 可见，先使用@PropertySource读取配置文件，然后通过@Value以${key:defaultValue}的形式注入，可以极大地简化读取配置的麻烦。
 * 另一种注入配置的方式是先通过一个简单的JavaBean持有所有的配置，例如，一个SmtpConfig：
 * 然后，在需要读取的地方，使用#{smtpConfig.host}注入：
 * 注意观察#{}这种注入语法，它和${key}不同的是，#{}表示从JavaBean读取属性。"#{smtpConfig.host}"的意思是，
 * 从名称为smtpConfig的Bean读取host属性，即调用getHost()方法。一个Class名为SmtpConfig的Bean，它在Spring容器中的默认名称就是smtpConfig，除非用@Qualifier指定了名称。
 * 使用一个独立的JavaBean持有所有属性，然后在其他Bean中以#{bean.property}注入的好处是，多个Bean都可以引用同一个Bean的某个属性。
 * 例如，如果SmtpConfig决定从数据库中读取相关配置项，那么MailService注入的@Value("#{smtpConfig.host}")仍然可以不修改正常运行。
 * 小结
 * Spring容器可以通过@PropertySource自动读取配置，并以@Value("${key}")的形式注入；
 * 可以通过${key:defaultValue}指定默认值；
 * 以#{bean.property}形式注入时，Spring容器自动把指定Bean的指定属性值注入。
 * 问:为啥SmtpConfig 可以不用注释@PropertySource("smtp.properties")就可以读取配置文件呢？
 * 答:@PropertySource读取的配置是针对IoC容器全局的，其他任何bean都可以直接引用已读取到的配置
 * 问:感觉在MailService中直接用@Autowired注入SmtpConfig更好一些，有编译器检查，更不容易出错。而使用#{}好像没有编译器检查吧
 * 答:这是为了演示*#{xxx.xxx}*语法，实际怎么注入，具体问题具体分析
 */
@Configuration
@ComponentScan
@PropertySource("app.properties")//表示读取的classpath的app.properties
public class AppConfig {
	
	@Value("${app.zone:Z}")
	String zoneId;
	
	@Bean
	ZoneId createZoneId() {
		return ZoneId.of(zoneId);
	}
	
	@Bean
	ZoneId newZoneId(@Value("${app.zone:Z}")String zoneId){
		return ZoneId.of(zoneId);
	}
}
