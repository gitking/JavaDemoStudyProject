package com.yale.test.spring.aop.lxf.metrics;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.yale.test.spring.aop.lxf.metrics.service.UserService;

/*
 * 使用注解装配AOP
 * 上一节我们讲解了使用AspectJ的注解，并配合一个复杂的execution(* xxx.Xyz.*(..))语法来定义应该如何装配AOP。
 * 在实际项目中，这种写法其实很少使用。假设你写了一个SecurityAspect：
 * @Aspect
	@Component
	public class SecurityAspect {
	    @Before("execution(public * com.itranswarp.learnjava.service.*.*(..))")
	    public void check() {
	        if (SecurityContext.getCurrentUser() == null) {
	            throw new RuntimeException("check failed");
	        }
	    }
	}
 * 基本能实现无差别全覆盖，即某个包下面的所有Bean的所有方法都会被这个check()方法拦截。
 * 还有的童鞋喜欢用方法名前缀进行拦截：
 * @Around("execution(public * update*(..))")
	public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
	    // 对update开头的方法切换数据源:
	    String old = setCurrentDataSource("master");
	    Object retVal = pjp.proceed();
	    restoreCurrentDataSource(old);
	    return retVal;
	}
 * 这种非精准打击误伤面更大，因为从方法前缀区分是否是数据库操作是非常不可取的。
 * 我们在使用AOP时，要注意到虽然Spring容器可以把指定的方法通过AOP规则装配到指定的Bean的指定方法前后，但是，如果自动装配时，因为不恰当的范围，容易导致意想不到的结果，即很多不需要AOP代理的Bean也被自动代理了，并且，后续新增的Bean，如果不清楚现有的AOP装配规则，容易被强迫装配。
 * 使用AOP时，被装配的Bean最好自己能清清楚楚地知道自己被安排了。例如，Spring提供的@Transactional就是一个非常好的例子。如果我们自己写的Bean希望在一个数据库事务中被调用，就标注上@Transactional：
 * @Component
	public class UserService {
	    // 有事务:
	    @Transactional
	    public User createUser(String name) {
	        ...
	    }
	
	    // 无事务:
	    public boolean isValidName(String name) {
	        ...
	    }
	
	    // 有事务:
	    @Transactional
	    public void updateUser(User user) {
	        ...
	    }
	}
 * 或者直接在class级别注解，表示“所有public方法都被安排了”：
 * @Component
	@Transactional
	public class UserService {
	    ...
	}
 * 通过@Transactional，某个方法是否启用了事务就一清二楚了。因此，装配AOP的时候，使用注解是最好的方式。
 * 我们以一个实际例子演示如何使用注解实现AOP装配。为了监控应用程序的性能，我们定义一个性能监控的注解：
 * @Target(METHOD)
	@Retention(RUNTIME)
	public @interface MetricTime {
	    String value();
	}
 * 在需要被监控的关键方法上标注该注解：
 * @Component
	public class UserService {
	    // 监控register()方法性能:
	    @MetricTime("register")
	    public User register(String email, String password, String name) {
	        ...
	    }
	    ...
	}
 * 然后，我们定义MetricAspect：
 * 小结
 * 使用注解实现AOP需要先定义注解，然后使用@Around("@annotation(name)")实现装配；
 * 使用注解既简单，又能明确标识AOP装配，是使用AOP推荐的方式。
 */
@Configuration
@ComponentScan
@EnableAspectJAutoProxy
public class AppConfig {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		UserService userService = context.getBean(UserService.class);
		userService.register("test@example.com", "password", "test");
		userService.login("bob@example.com", "password");
		System.err.println(userService.getClass().getName());
	}
}
