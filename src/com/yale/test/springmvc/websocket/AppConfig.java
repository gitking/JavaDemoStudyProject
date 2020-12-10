package com.yale.test.springmvc.websocket;

import java.io.File;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring.extension.SpringExtension;
import com.mitchellbosecke.pebble.spring.servlet.PebbleViewResolver;
import com.yale.test.springmvc.websocket.web.ChatHandler;
import com.yale.test.springmvc.websocket.web.ChatHandshakeInterceptor;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/*
 * 使用WebSocket
 * WebSocket是一种基于HTTP的长链接技术。传统的HTTP协议是一种请求-响应模型，如果浏览器不发送请求，那么服务器无法主动给浏览器推送数据。如果需要定期给浏览器推送数据，
 * 例如股票行情，或者不定期给浏览器推送数据，例如在线聊天，基于HTTP协议实现这类需求，只能依靠浏览器的JavaScript定时轮询，效率很低且实时性不高。
 * 因为HTTP本身是基于TCP连接的，所以，WebSocket在HTTP协议的基础上做了一个简单的升级，即建立TCP连接后，浏览器发送请求时，附带几个头：
 * GET /chat HTTP/1.1
 * Host: www.example.com
 * Upgrade: websocket
 * Connection: Upgrade
 * 就表示客户端希望升级连接，变成长连接的WebSocket，服务器返回升级成功的响应：
 * HTTP/1.1 101 Switching Protocols
 * Upgrade: websocket
 * Connection: Upgrade
 * 收到成功响应后表示WebSocket“握手”成功，这样，代表WebSocket的这个TCP连接将不会被服务器关闭，而是一直保持，服务器可随时向浏览器推送消息，浏览器也可随时向服务器推送消息。双方推送的消息既可以是文本消息，也可以是二进制消息，一般来说，绝大部分应用程序会推送基于JSON的文本消息。
 * 现代浏览器都已经支持WebSocket协议，服务器则需要底层框架支持。Java的Servlet规范从3.1开始支持WebSocket，所以，必须选择支持Servlet 3.1或更高规范的Servlet容器，才能支持WebSocket。最新版本的Tomcat、Jetty等开源服务器均支持WebSocket。
 * 我们以实际代码演示如何在Spring MVC中实现对WebSocket的支持。首先，我们需要在pom.xml中加入以下依赖：
 * org.apache.tomcat.embed:tomcat-embed-websocket:9.0.26
 * org.springframework:spring-websocket:5.2.0.RELEASE
 * 第一项是嵌入式Tomcat支持WebSocket的组件，第二项是Spring封装的支持WebSocket的接口。
 * 接下来，我们需要在AppConfig中加入Spring Web对WebSocket的配置，此处我们需要创建一个WebSocketConfigurer实例：
 * 此实例在内部通过WebSocketHandlerRegistry注册能处理WebSocket的WebSocketHandler，以及可选的WebSocket拦截器HandshakeInterceptor。我们注入的这两个类都是自己编写的业务逻辑，后面我们详细讨论如何编写它们，这里只需关注浏览器连接到WebSocket的URL是/chat。
 * 处理WebSocket连接
 * 和处理普通HTTP请求不同，没法用一个方法处理一个URL。Spring提供了TextWebSocketHandler和BinaryWebSocketHandler分别处理文本消息和二进制消息，这里我们选择文本消息作为聊天室的协议，因此，ChatHandler需要继承自TextWebSocketHandler：
 * @Component
	public class ChatHandler extends TextWebSocketHandler {
	    ...
	}
 * 当浏览器请求一个WebSocket连接后，如果成功建立连接，Spring会自动调用afterConnectionEstablished()方法，任何原因导致WebSocket连接中断时，
 * Spring会自动调用afterConnectionClosed方法，因此，覆写这两个方法即可处理连接成功和结束后的业务逻辑：
 * @Component
	public class ChatHandler extends TextWebSocketHandler {
	    // 保存所有Client的WebSocket会话实例:
	    private Map<String, WebSocketSession> clients = new ConcurrentHashMap<>();
	
	    @Override
	    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
	        // 新会话根据ID放入Map:
	        clients.put(session.getId(), session);
	        session.getAttributes().put("name", "Guest1");
	    }
	
	    @Override
	    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
	        clients.remove(session.getId());
	    }
	}
 * 每个WebSocket会话以WebSocketSession表示，且已分配唯一ID。和WebSocket相关的数据，例如用户名称等，均可放入关联的getAttributes()中。
 * 用实例变量clients持有当前所有的WebSocketSession是为了广播，即向所有用户推送同一消息时，可以这么写：
 * String json = ...
	TextMessage message = new TextMessage(json);
	for (String id : clients.keySet()) {
	    WebSocketSession session = clients.get(id);
	    session.sendMessage(message);
	}
 * 我们发送的消息是序列化后的JSON，可以用ChatMessage表示：
 * public class ChatMessage {
		public long timestamp;
		public String name;
	    public String text;
	}
 * 每收到一个用户的消息后，我们就需要广播给所有用户：
 * @Component
	public class ChatHandler extends TextWebSocketHandler {
	    ...
	    @Override
	    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
	        String s = message.getPayload();
	        String r = ... // 根据输入消息构造待发送消息
	        broadcastMessage(r); // 推送给所有用户
	    }
	}
 * 如果要推送给指定的几个用户，那就需要在clients中根据条件查找出某些WebSocketSession，然后发送消息。
 * 注意到我们在注册WebSocket时还传入了一个ChatHandshakeInterceptor，这个类实际上可以从HttpSessionHandshakeInterceptor继承，它的主要作用是在WebSocket建立连接后，
 * 把HttpSession的一些属性复制到WebSocketSession，例如，用户的登录信息等：
 * @Component
	public class ChatHandshakeInterceptor extends HttpSessionHandshakeInterceptor {
	    public ChatHandshakeInterceptor() {
	        // 指定从HttpSession复制属性到WebSocketSession:
	        super(List.of(UserController.KEY_USER));
	    }
	}
 * 这样，在ChatHandler中，可以从WebSocketSession.getAttributes()中获取到复制过来的属性。
 * 客户端开发
 * 在完成了服务器端的开发后，我们还需要在页面编写一点JavaScript逻辑：
 * // 创建WebSocket连接:
	var ws = new WebSocket('ws://' + location.host + '/chat');
	// 连接成功时:
	ws.addEventListener('open', function (event) {
	    console.log('websocket connected.');
	});
	// 收到消息时:
	ws.addEventListener('message', function (event) {
	    console.log('message: ' + event.data);
	    var msgs = JSON.parse(event.data);
	    // TODO:
	});
	// 连接关闭时:
	ws.addEventListener('close', function () {
	    console.log('websocket closed.');
	});
	// 绑定到全局变量:
	window.chatWs = ws;
 * 用户可以在连接成功后任何时候给服务器发送消息：
 * var inputText = 'Hello, WebSocket.';
 * window.chatWs.send(JSON.stringify({text: inputText}));
 * 最后，连调浏览器和服务器端，如果一切无误，可以开多个不同的浏览器测试WebSocket的推送和广播：
 * 和上一节我们介绍的异步处理类似，Servlet的线程模型并不适合大规模的长链接。基于NIO的Netty等框架更适合处理WebSocket长链接，我们将在后面介绍。
 * 小结
 * 在Servlet中使用WebSocket需要3.1及以上版本；
 * 通过spring-websocket可以简化WebSocket的开发。
 * HTTP 2.0可以支持浏览器同时发出多个请求，但每个请求需要唯一标识，服务器可以不按请求的顺序返回多个响应，由浏览器自己把收到的响应和请求对应起来。可见，HTTP 2.0进一步提高了传输效率，因为浏览器发出一个请求后，不必等待响应，就可以继续发下一个请求。
 * HTTP 2.0与WebSocket的区别是什么？
 */
@Configuration
@ComponentScan
@EnableWebMvc
@EnableWebSocket
@EnableTransactionManagement
@PropertySource("classpaty:/jdbc.properties")
public class AppConfig {
	
	@Value("${jdbc.url}")
	String jdbcUrl;
	
	@Value("${jdbc.username}")
	String jdbcUsername;
	
	@Value("${jdbc.password}")
	String jdbcPassword;
	
	@Bean
	WebSocketConfigurer createWebSocketConfigurer(@Autowired ChatHandler chatHandler, @Autowired ChatHandshakeInterceptor chatInterceptor) {
		return new WebSocketConfigurer() {
			public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
				//把URL与指定的WebSocketHandler管理,可以关联多个
				registry.addHandler(chatHandler, "/chat").addInterceptors(chatInterceptor);
			}
		};
	}
	
	@Bean
	WebMvcConfigurer createWebMvcConfigurer(@Autowired HandlerInterceptor[] interceptors) {
		return new WebMvcConfigurer() {
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				for (HandlerInterceptor interceptor: interceptors) {
					registry.addInterceptor(interceptor);
				}
			}
			
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/static/**").addResourceLocations("/static/");
			}
		};
	}
	
	@Bean
	ObjectMapper createObjectMapper() {
		ObjectMapper om = new ObjectMapper();
		om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		return om;
	}
	
	@Bean
	JdbcTemplate createJdbcTemplate(@Autowired DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
	
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
	PlatformTransactionManager createTxManager(@Autowired DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
	
	@Bean
	ViewResolver createViewResolver(@Autowired ServletContext servletContext) {
		PebbleEngine engine = new PebbleEngine.Builder().autoEscaping(true)
				.cacheActive(false)
				.loader(new ServletLoader(servletContext))
				.extension(new SpringExtension())
				.build();
		PebbleViewResolver viewResolver = new PebbleViewResolver();
		viewResolver.setPrefix("/WEB-INF/templates/websocket");
		viewResolver.setSuffix("");
		viewResolver.setPebbleEngine(engine);
		return viewResolver;
	}
	
	public static void main(String[] args) throws LifecycleException {
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(Integer.getInteger("port", 8080));
		tomcat.getConnector();
		Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(ctx);
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", new File("target/classes").getAbsolutePath(), "/"));
		ctx.setResources(resources);
		tomcat.start();
		tomcat.getServer().await();
	}
}
