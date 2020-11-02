package com.yale.test.spring.conditional;

import java.time.ZoneId;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
/*
 * 使用条件装配
 * 开发应用程序时，我们会使用开发环境，例如，使用内存数据库以便快速启动。而运行在生产环境时，我们会使用生产环境，例如，使用MySQL数据库。
 * 如果应用程序可以根据自身的环境做一些适配，无疑会更加灵活。
 * Spring为应用程序准备了Profile这一概念，用来表示不同的环境。例如，我们分别定义开发、测试和生产这3个环境：
 * 1.native(本地)
 * 2.test(测试)
 * 3.production(生产环境)
 * 创建某个Bean时，Spring容器可以根据注解@Profile来决定是否创建。例如，以下配置：
 * 如果当前的Profile设置为test，则Spring容器会调用createZoneIdForTest()创建ZoneId，否则，调用createZoneId()创建ZoneId。
 * 注意到@Profile("!test")表示非test环境。
 * 在运行程序时，加上JVM参数-Dspring.profiles.active=test就可以指定以test环境启动。
 * 实际上，Spring允许指定多个Profile，例如：
 * -Dspring.profiles.active=test,master可以表示test环境，并使用master分支代码。
 * 要满足多个Profile条件，可以这样写：
 *  @Bean
	@Profile({ "test", "master" }) // 同时满足test和master
	ZoneId createZoneId() {
	    ...
	}
 * 使用Conditional
 * 除了根据@Profile条件来决定是否创建某个Bean外，Spring还可以根据@Conditional决定是否创建某个Bean。
 * 例如，我们对SmtpMailService添加如下注解：
 * @Component
	@Conditional(OnSmtpEnvCondition.class)
	public class SmtpMailService implements MailService {
	    ...
	}
 * 它的意思是，如果满足OnSmtpEnvCondition的条件，才会创建SmtpMailService这个Bean。OnSmtpEnvCondition的条件是什么呢？我们看一下代码：OnSmtpEnvCondition 
 * public class OnSmtpEnvCondition implements Condition {
	    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
	        return "true".equalsIgnoreCase(System.getenv("smtp"));
	    }
	}
 * 因此，OnSmtpEnvCondition的条件是存在环境变量smtp，值为true。这样，我们就可以通过环境变量来控制是否创建SmtpMailService。
 * Spring只提供了@Conditional注解，具体判断逻辑还需要我们自己实现。Spring Boot提供了更多使用起来更简单的条件注解，例如，如果配置文件中存在app.smtp=true，则创建MailService：
 * @Component
	@ConditionalOnProperty(name="app.smtp", havingValue="true")
	public class MailService {
	    ...
	}
 * 如果当前classpath中存在类javax.mail.Transport，则创建MailService：
 * @Component
	@ConditionalOnClass(name = "javax.mail.Transport")
	public class MailService {
	    ...
	}
 * 后续我们会介绍Spring Boot的条件装配。我们以文件存储为例，假设我们需要保存用户上传的头像，并返回存储路径，在本地开发运行时，我们总是存储到文件：
 * @Component
	@ConditionalOnProperty(name = "app.storage", havingValue = "file", matchIfMissing = true)
	public class FileUploader implements Uploader {
	    ...
	}
 * 在生产环境运行时，我们会把文件存储到类似AWS S3上：
 * @Component
	@ConditionalOnProperty(name = "app.storage", havingValue = "s3")
	public class S3Uploader implements Uploader {
	    ...
	}
 * 其他需要存储的服务则注入Uploader：
 * @Component
	public class UserImageService {
	    @Autowired
	    Uploader uploader;
	}
 * 当应用程序检测到配置文件存在app.storage=s3时，自动使用S3Uploader，如果存在配置app.storage=file，或者配置app.storage不存在，则使用FileUploader。
 * 可见，使用条件注解，能更灵活地装配Bean。
 * 小结
 * Spring允许通过@Profile配置不同的Bean；
 * Spring还提供了@Conditional来进行条件装配，Spring Boot在此基础上进一步提供了基于配置、Class、Bean等条件进行装配。
 */
@Configuration
@ComponentScan
public class AppConfig {
	
	public static void main(String[] args) {
		
	}
	
	@Bean
	@Profile("!test")//非测试环境
	ZoneId createZoneId() {
		return ZoneId.systemDefault();
	}
	
	@Bean
	@Profile("test")//测试环境
	ZoneId createZoneIdForTest() {
		return ZoneId.of("America/New_York");
	}
}
