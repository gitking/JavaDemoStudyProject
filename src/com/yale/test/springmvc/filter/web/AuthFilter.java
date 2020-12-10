package com.yale.test.springmvc.filter.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yale.test.springmvc.rest.entity.User;
import com.yale.test.springmvc.rest.service.UserService;
import com.yale.test.springmvc.rest.web.UserController;

/*
 * 集成Filter
 * 在Spring MVC中，DispatcherServlet只需要固定配置到web.xml中，剩下的工作主要是专注于编写Controller。
 * 但是，在Servlet规范中，我们还可以使用Filter。如果要在Spring MVC中使用Filter，应该怎么做？
 * 有的童鞋在上一节的Web应用中可能发现了，如果注册时输入中文会导致乱码，因为Servlet默认按非UTF-8编码读取参数。为了修复这一问题，我们可以简单地使用一个EncodingFilter，在全局范围类给HttpServletRequest和HttpServletResponse强制设置为UTF-8编码。
 * 可以自己编写一个EncodingFilter，也可以直接使用Spring MVC自带的一个CharacterEncodingFilter。配置Filter时，只需在web.xml中声明即可：
 * <web-app>
    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>

    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ...
</web-app>
 * 因为这种Filter和我们业务关系不大，注意到CharacterEncodingFilter其实和Spring的IoC容器没有任何关系，两者均互不知晓对方的存在，因此，配置这种Filter十分简单。
 * 我们再考虑这样一个问题：如果允许用户使用Basic模式进行用户验证，即在HTTP请求中添加头Authorization: Basic email:password，这个需求如何实现？
 * 编写一个AuthFilter是最简单的实现方式：
 * 现在问题来了：在Spring中创建的这个AuthFilter是一个普通Bean，Servlet容器并不知道，所以它不会起作用。
 * 如果我们直接在web.xml中声明这个AuthFilter，注意到AuthFilter的实例将由Servlet容器而不是Spring容器初始化，因此，@Autowire根本不生效，用于登录的UserService成员变量永远是null。
 * 所以，得通过一种方式，让Servlet容器实例化的Filter，间接引用Spring容器实例化的AuthFilter。Spring MVC提供了一个DelegatingFilterProxy，专门来干这个事情：
 * <web-app>
    <filter>
        <filter-name>authFilter</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>authFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
    ...
</web-app>
 * 我们来看实现原理：
 * 1.Servlet容器从web.xml中读取配置，实例化DelegatingFilterProxy，注意命名是authFilter；
 * 2.Spring容器通过扫描@Component实例化AuthFilter。
 * 当DelegatingFilterProxy生效后，它会自动查找注册在ServletContext上的Spring容器，再试图从容器中查找名为authFilter的Bean，也就是我们用@Component声明的AuthFilter。
 * DelegatingFilterProxy将请求代理给AuthFilter，核心代码如下：
 * public class DelegatingFilterProxy implements Filter {
	    private Filter delegate;
	    public void doFilter(...) throws ... {
	        if (delegate == null) {
	            delegate = findBeanFromSpringContainer();
	        }
	        delegate.doFilter(req, resp, chain);
	    }
	}
 * 这就是一个代理模式的简单应用。我们画个图表示它们之间的引用关系如下：
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐ ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
	  ┌─────────────────────┐        ┌───────────┐   │
	│ │DelegatingFilterProxy│─│─│─ ─>│AuthFilter │
	  └─────────────────────┘        └───────────┘   │
	│ ┌─────────────────────┐ │ │    ┌───────────┐
	  │  DispatcherServlet  │─ ─ ─ ─>│Controllers│   │
	│ └─────────────────────┘ │ │    └───────────┘
	                                                 │
	│    Servlet Container    │ │  Spring Container
	 ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─   ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * 如果在web.xml中配置的Filter名字和Spring容器的Bean的名字不一致，那么需要指定Bean的名字：
 * <filter>
	    <filter-name>basicAuthFilter</filter-name>
	    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
	    <!-- 指定Bean的名字 -->
	    <init-param>
	        <param-name>targetBeanName</param-name>
	        <param-value>authFilter</param-value>
	    </init-param>
	</filter>
 * 实际应用时，尽量保持名字一致，以减少不必要的配置。
 * 要使用Basic模式的用户认证，我们可以使用curl命令测试。例如，用户登录名是tom@example.com，口令是tomcat，那么先构造一个使用URL编码的用户名:口令的字符串：tom%40example.com:tomcat
 * 对其进行Base64编码，最终构造出的Header如下：
 * Authorization: Basic dG9tJTQwZXhhbXBsZS5jb206dG9tY2F0
 * 使用如下的curl命令并获得响应如下：
 * curl -v -H 'Authorization: Basic dG9tJTQwZXhhbXBsZS5jb206dG9tY2F0' http://localhost:8080/profile
	> GET /profile HTTP/1.1
	> Host: localhost:8080
	> User-Agent: curl/7.64.1
	> Accept: *\/*
	> Authorization: Basic dG9tJTQwZXhhbXBsZS5jb206dG9tY2F0
	> 
	< HTTP/1.1 200 
	< Set-Cookie: JSESSIONID=CE0F4BFC394816F717443397D4FEABBE; Path=/; HttpOnly
	< Content-Type: text/html;charset=UTF-8
	< Content-Language: en-CN
	< Transfer-Encoding: chunked
	< Date: Wed, 29 Apr 2020 00:15:50 GMT
	< 
	<!doctype html>
	...HTML输出...
 * 上述响应说明AuthFilter已生效。
 * 注意：Basic认证模式并不安全，本节只用来作为使用Filter的示例。  
 * 小结
 * 当一个Filter作为Spring容器管理的Bean存在时，可以通过DelegatingFilterProxy间接地引用它并使其生效。
 * 还有不要注入HttpServletRequest这种一次性短生命周期的资源，即使做了代理也最好别用，因为通常我们把它当作局部变量处理
 */
@Component
public class AuthFilter implements Filter{
	
	final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserService userService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		try {
			authenticateByHeader(req);
		} catch (Exception e) {
			e.printStackTrace();
			logger.warn("login by authorization header failed.", e);
		}
		chain.doFilter(request, response); // 继续处理请求:
	}
	
	private void authenticateByHeader(HttpServletRequest req) throws UnsupportedEncodingException {
		String authHeader = req.getHeader("Authorization"); // 获取Authorization头:
		if (authHeader != null && authHeader.startsWith("Basic ")) {
			logger.info("try authenticate by authorization header...");
			String up = new String(Base64.getDecoder().decode(authHeader.substring(6)), StandardCharsets.UTF_8);
			int pos = up.indexOf(':');
			if (pos > 0) {
				// 从Header中提取email和password:
				String email = URLDecoder.decode(up.substring(0, pos), "UTF-8");
				String password = URLDecoder.decode(up.substring(pos+1), "UTF-8");
				User user = userService.signin(email, password);// 登录:
	            // 放入Session:
				req.getSession().setAttribute(UserController.KEY_USER, user);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void destroy() {
		
	}
}
