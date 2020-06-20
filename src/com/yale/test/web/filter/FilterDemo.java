package com.yale.test.web.filter;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * 过滤器
 * 	他会在一组资源(jsp,servlet,.css,.html等等)的前面执行,它可以让请求达到目标资源,也可以不让请求达到目标资源。
 * 过滤器的四种拦截方式:
 * 	请求 <dispatcher>REQUEST</dispatcher> 默认的,在web.xml不写就是默认这个
 *  转发 <dispatcher>FORWARD</dispatcher>
 *  包含<dispatcher>INCLUDE</dispatcher>
 *  错误<dispatcher>ERROR</dispatcher>
 * 多个过滤器的执行顺序:
 * 	按照<filter-mapping>在Web.xml里面的配置顺序
 * 其实你如果想学习filter和servlet在web.xml里面有什么配置,你就在web.xml的servlet节点下面按alt + / 看提示,
 * alt + / 会把里面的所有key配置的信息都提示出来,这个是xml schema的特性,xml和.xsd我之前好像在哪里学过?好像是慕课网把
 * @author dell
 *
 */
public class FilterDemo implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		System.out.println("filter跟servlet不一样,filter在tomcat启动的时候就创建Filter的对象了,创建成功之后立即调用init方法");
		System.out.println("filter跟servlet一样都是由tomcat创建的,并且都是单例的");
		String filterName = filterConfig.getFilterName();
		System.out.println("filterName在Web.xml里面的名字:" + filterName);
		ServletContext sc =  filterConfig.getServletContext();
		System.out.println("通过FilterConfig也可以获取ServletContext对象" + sc);
		String paVal = filterConfig.getInitParameter("pa");
		System.out.println("获取在web.xm里面配置的参数" + paVal);
		Enumeration<String> allParams = filterConfig.getInitParameterNames();
		while (allParams.hasMoreElements()) {
			System.out.println("获取所有的参数" + allParams.nextElement());
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		System.out.println("filter我拦截你.............");
		/**
		 * FilterChain是过滤器链,chain就是链
		 * doFilter的意思是执行目标资源或者下一个目标过滤器
		 * 如果没有下一个过滤器那么执行的是目标资源,如果有,那么执行下一个过滤器
		 */
		chain.doFilter(request, response);//放行,放行就相当于调用了目标servlet的services方法,如果有多个filter的话doFilter就是放给下一个filter
		System.out.println("filter我放行之后,我还会执行............");
	}

	@Override
	public void destroy() {
		System.out.println("服务器关闭的时候调用");
	}

}
