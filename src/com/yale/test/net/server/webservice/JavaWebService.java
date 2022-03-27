package com.yale.test.net.server.webservice;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

/**
 * WebService接口超时时间设置
 * https://blog.csdn.net/elim168/article/details/78090768 Java Webservice指定超时时间
 * Java Webservice指定超时时间,使用JDK对Webservice的支持进行Webservice调用时通常的操作步骤如下：
 * //1、创建一个javax.xml.ws.Service实例
 *	javax.xml.ws.Service service = javax.xml.ws.Service.create(wsdl, serviceName);
 *	//2、通过Service实例获取对应的服务接口的代理
 *	HelloService helloService = service.getPort(portName, HelloService.class);
 *	//3、通过获取到的Webservice服务接口的代理调用对应的服务方法
 * helloService.sayHello("Elim")
 * 在上述的步骤一在构建Service实例的同时，在Service内部会构建一个ServiceDelegate类型的对象赋给属性delegate，内部持有。然后在第二步会利用delegate创建一个服务接口的代理对象，同时还会代理BindingProvider和Closeable接口。
 * 然后在第三步真正发起接口请求时，内部会发起一个HTTP请求，发起HTTP请求时会从BindingProvider的getRequestContext()返回结果中获取超时参数，分别对应com.sun.xml.internal.ws.connection.timeout和com.sun.xml.internal.ws.request.timeout参数，前者是建立连接的超时时间，后者是获取请求响应的超时时间，单位是毫秒。
 * 如果没有指定对应的超时时间或者指定的超时时间为0都表示永不超时。所以为了指定超时时间我们可以从BindingProvider下手。比如：
 * @author issuser
 */
public class JavaWebService {
	public static void main(String[] args) throws MalformedURLException {
		String targetNamespace = "http://test.elim.com/ws";
		QName serviceName = new QName(targetNamespace, "helloService");
		QName portName = new QName(targetNamespace, "helloService");
		URL wsdl = new URL("http://localhost:8888/hello");
		//内部会创建一个ServiceDelegate类型的对象赋给属性delegate
		Service service = Service.create(wsdl, serviceName);
		//会利用delegate创建一个服务接口的代理对象，同时还会代理BindingProvider和Closeable接口。
		HelloService helloService = service.getPort(portName, HelloService.class);
		
		BindingProvider bindingProvider = (BindingProvider) helloService;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		
		/**
		 * com.sun.xml.internal.ws.developer.JAXWSProperties类中，
		 * String CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
		 * requestContext.put("com.sun.xml.internal.ws.connection.timeout", 10 * 1000);//connect,不是connection
		 * String REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";
		 */
		requestContext.put("com.sun.xml.internal.ws.connection.timeout", 10 * 1000);//建立连接的超时时间为10秒
		requestContext.put("com.sun.xml.internal.ws.request.timeout", 15 * 1000);//指定请求的响应超时时间为15秒
		
		//在调用接口方法时，内部会发起一个HTTP请求，发起HTTP请求时会从BindingProvider的getRequestContext()返回结果中获取超时参数，
		//分别对应com.sun.xml.internal.ws.connection.timeout和com.sun.xml.internal.ws.request.timeout参数，
		//前者是建立连接的超时时间，后者是获取请求响应的超时时间，单位是毫秒。如果没有指定对应的超时时间或者指定的超时时间为0都表示永不超时。
		System.out.println(helloService.sayHello("Elim"));
		
        try {
        	ZSGenericRulePojoServiceImplService iLogUnderClient = new ZSGenericRulePojoServiceImplService();
            RuleService rsl = iLogUnderClient.getZSGenericRulePojoServiceImplPort();
            
            /**
             * 这一点更奇怪:rsl这个对象com.zsins.ruleapp.biz.service.impl.RuleService为啥能强制转换为javax.xml.ws.BindingProvider
             * org.apache.cxf.jaxws.JaxWsClientProxy@7f961ee3
             * org.apache.cxf.jaxws.JaxWsClientProxy@7f961ee3
             * org.apache.cxf.jaxws.context.WrappedMessageContext@5ddf1a72
             * 上面这个三个对象是哪来的？难道是tomcat的鬼,用的是tomcat的jar包？
             */
            BindingProvider bindingProvider1 = (BindingProvider) rsl;
    		Map<String, Object> requestContext1 = bindingProvider1.getRequestContext();
    		
    		/**
    		 * //requestContext.put("com.sun.xml.internal.ws.connection.timeout", 1);
		     * //requestContext.put("com.sun.xml.internal.ws.request.timeout", 1);
			 * requestContext.put("javax.xml.ws.client.connectionTimeout", 1);
			 * requestContext.put("javax.xml.ws.client.receiveTimeout", 1);
			 *	//    		requestContext.put("com.sun.xml.internal.ws.request.timeout", 1);
			 *	//    		requestContext.put("com.sun.xml.internal.ws.connect.timeout", 1);
			 *	    		//requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 1);
			 *	    		//requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 1);
			 * 可以看下com.sun.xml.internal.ws.developer.JAXWSProperties这个类的源码
			 * com.sun.xml.ws.developer.JAXWSProperties
			 * 还有这个com.sun.xml.ws.client.BindingProviderProperties类的源码
			 * com.sun.xml.internal.ws.client.BindingProviderProperties
			 * JDK1.6:com.sun.xml.ws.client.BindingProviderProperties 继承自 com.sun.xml.ws.developer.JAXWSProperties
			 * JDK1.8:com.sun.xml.internal.ws.client.BindingProviderProperties  继承自 com.sun.xml.internal.ws.developer.JAXWSProperties
			 * 但是JAXWSProperties这个类里面定义的超时时间是下面这俩个
			 * public static final java.lang.String CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
    		 * public static final java.lang.String REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";
    		 * 但实际上起作用的是下面这俩个(我们项目用的是jdk1.6),好像是WebService的协议不一样导致的？用的是JBoss的版本？JBossWS,关键我怎么看我代码用的那个版本的WebService啊？
    		 * https://cloud.tencent.com/developer/ask/94978
    		 * 使用一组不同的属性（可在JBoss JAX-WS用户指南中找到https://docs.jboss.org/author/index.html）使其工作：难道是tomcat的鬼,用的是tomcat的jar包？
    		 * requestContext1.put("javax.xml.ws.client.connectionTimeout", 10000);
    		 * requestContext1.put("javax.xml.ws.client.receiveTimeout", 10000);
    		 */
    		//工作给WebService设置超时时间就是用这俩个参数设置的
    		requestContext1.put("javax.xml.ws.client.connectionTimeout", 10000);
    		requestContext1.put("javax.xml.ws.client.receiveTimeout", 10000);
            MapElements mapElement = new MapElements();
            mapElement.setKey("bom");
            mapElement.setValue(application);
            MapElementsArray elements = new MapElementsArray();

            elements.getItem().add(mapElement);
            Result result = rsl.execute("zsins-udwr-ruleapp", elements);
        } catch (WebServiceException e) {
        	System.out.println("WebService超时了");
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}
