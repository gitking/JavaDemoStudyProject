package com.yale.test.springmvc.async.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.yale.test.springmvc.rest.entity.User;
import com.yale.test.springmvc.rest.service.UserService;

/*
 * 异步处理
 * 在Servlet模型中，每个请求都是由某个线程处理，然后，将响应写入IO流，发送给客户端。从开始处理请求，到写入响应完成，都是在同一个线程中处理的。
 * 实现Servlet容器的时候，只要每处理一个请求，就创建一个新线程处理它，就能保证正确实现了Servlet线程模型。在实际产品中，例如Tomcat，总是通过线程池来处理请求，它仍然符合一个请求从头到尾都由某一个线程处理。
 * 这种线程模型非常重要，因为Spring的JDBC事务是基于ThreadLocal实现的，如果在处理过程中，一会由线程A处理，一会又由线程B处理，那事务就全乱套了。此外，很多安全认证，也是基于ThreadLocal实现的，可以保证在处理请求的过程中，各个线程互不影响。
 * 但是，如果一个请求处理的时间较长，例如几秒钟甚至更长，那么，这种基于线程池的同步模型很快就会把所有线程耗尽，导致服务器无法响应新的请求。如果把长时间处理的请求改为异步处理，那么线程池的利用率就会大大提高。
 * Servlet从3.0规范开始添加了异步支持，允许对一个请求进行异步处理。
 * 我们先来看看在Spring MVC中如何实现对请求进行异步处理的逻辑。首先建立一个Web工程，然后编辑web.xml文件如下：
 * <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
    version="3.1">
    <display-name>Archetype Created Web Application</display-name>

    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>org.springframework.web.context.support.AnnotationConfigWebApplicationContext</param-value>
        </init-param>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.itranswarp.learnjava.AppConfig</param-value>
        </init-param>
        <load-on-startup>0</load-on-startup>
        <async-supported>true</async-supported>
    </servlet>

    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
</web-app>
 * 和前面普通的MVC程序相比，这个web.xml主要有几点不同：
 * 1.不能再使用<!DOCTYPE ...web-app_2_3.dtd">的DTD声明，必须用新的支持Servlet 3.1规范的XSD声明，照抄即可；
 * 2.对DispatcherServlet的配置多了一个<async-supported>，默认值是false，必须明确写成true，这样Servlet容器才会支持async处理。
 * 下一步就是在Controller中编写async处理逻辑。我们以ApiController为例，演示如何异步处理请求。
 * 第一种async处理方式是返回一个Callable，Spring MVC自动把返回的Callable放入线程池执行，等待结果返回后再写入响应：
 * 第二种async处理方式是返回一个DeferredResult对象，然后在另一个线程中，设置此对象的值并写入响应：
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserService userService;
	
	/*
	 * 第一种async处理方式是返回一个Callable，Spring MVC自动把返回的Callable放入线程池执行，等待结果返回后再写入响应：
	 */
	@GetMapping("/users")
	public Callable<List<User>> users() {
		return ()->{
			//模拟3秒耗时
			try {
				Thread.sleep(3000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			return userService.getUsers();
		};
	}
	
	/*
	 * 第二种async处理方式是返回一个DeferredResult对象，然后在另一个线程中，设置此对象的值并写入响应：
	 * 使用DeferredResult时，可以设置超时，超时会自动返回超时错误响应。在另一个线程中，可以调用setResult()写入结果，也可以调用setErrorResult()写入一个错误结果。
	 * 运行程序，当我们访问http://localhost:8080/api/users/1时，假定用户存在，则浏览器在1秒后返回结果：
	 * 访问一个不存在的User ID，则等待1秒后返回错误结果：
	 * 
	 * 落花不再戏流水4869 : 我的理解,为了防止出现线程耗尽这种局面，第二种async方法的策略是增加线程：在方法内部又new了一个线程，由这个新的线程去处理耗时的部分，方法本身不去等待线程执行完毕，
	 * 而是直接返回DefferedResult，异步就体现在这个地方。老铁们，我说的对吗？
	 * 廖雪峰:可以启动新线程，也可以扔到线程池执行
	 * 薄荷糖与红茶:我觉得不对。我理解的是如果你都丢给线程池去管了那么本身线程池就是有上限的。如果每次都去开一个新线程那还要线程池做什么？线程池的好处是减少每次new一个thread和destroy一个thread的代价。
	 * 廖雪峰:线程池是可以没有上限的，实际上Executors.newCachedThreadPool()默认创建的那个池上限是Integer.MAX.线程池的好处是可以保留一定数量的idle线程，防止频繁创建和销毁线程
	 * 薄荷糖与红茶:谢谢廖老师。我还是认为线程池是一个更好的方式呀。去开新的线程就失去了线程池的优点了。以及如果线程池没有上限那感觉就会把server拖垮（当然如果有这种级别的需求也没有办法）
	 * 
	 * 薄荷糖与红茶 :一点浅见:异步是异在CPU上。同步操作=不管干啥CPU陪着你等，比如一个长IO操作，尽管不需要CPU的参与它也在傻等。例子如：读文件，发了DB请求在等返回结果等
	 * 异步操作=你不需要CPU了，CPU把你搁在一边，这个thread去处理别的事情了。待会你的操作完成，哪个thread有空哪个回来理你，但是就不见得是原来同个thread了.
	 * 异步操作可以用DeferredResult来取到结果进行下一步操作。有点类似js promise这类（？）
	 * 举例两者差异：
	 * 如果getAllUsers()这个操作要花10分钟。线程池里面就三个线程。
	 * 同步：三个用户一起getAllUsers()，整个服务器CPU就陪着你干这事了尽管并不需要它。十分钟里面它对别的用户是死的。
	 * 异步：三个用户一起getAllUsers()，整个服务器CPU把你搁在一边，还可以响应人家login啊 register这类的请求。十分钟后你的结果返回来，CPU再处理你的结果。
	 * 但是如果长时间的是一个CPU 操作，比如calculateYearEndTax 要花十分钟 三个一起算，那就CPU真没空理其他人了
	 * 廖雪峰:异步适用于IO密集型任务，IO忙，CPU闲.计算密集型任务是CPU忙，异步就没啥用，只能加CPU.
	 * 
	 * 自己的思考:
	 * tomcat什么时候来取DeferredResult的返回结果呢？
	 * 我的理解是Tomcat，总是通过线程池来处理请求，它仍然符合一个请求从头到尾都由某一个线程处理。假如这个任务比较耗时,我们同通过DeferredResult来实现异步。但是,这个时候tomcat线程池里面的线程并不能立刻拿到最终结果响应给客户端,tomcat线程池里面的线程可以先去做别的事情。但是tomcat的线程池在什么时候来获取DeferredResult的返回结果,获取到返回结果之后，tomcat怎么知道把这个结果返回给哪个客户端呢？哦，我明白了。线程池可以把Callable或者DeferredResult对象返回给servlet请求,由servlet调用FutureTask.get()方法阻塞当前请求线程,一直到获取返回结果。然后tomcat线程池里面的线程可以继续处理别的请求。
     * 问题又来了DeferredResult调用哪个方法获取返回结果呢？
	 */
	public DeferredResult<User> user(@PathVariable("id")long id) {
		DeferredResult<User> result = new DeferredResult<>(3000L);//3秒延时
		new Thread(()->{
			try{//等待1秒
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			try {
				User user = userService.getUserById(id);
				//设置正常结果并由SpringMVC写入Response
				result.setResult(user);
			} catch(Exception e) {
				e.printStackTrace();
				Map<String, String> error = new HashMap<String, String>();
				error.put("error", e.getClass().getSimpleName());
				error.put("message", e.getMessage());
				result.setErrorResult(result);
			}
		}).start();
		return result;
	}
}
