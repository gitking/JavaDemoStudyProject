package com.yale.test.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 实现servlet有三种方式
 * 	实现javax.servlet.Servlet接口
 *  继承javax.servlet.GenericServlet类
 *  继承:javax.servlet.http.HttpServlet类(HttpServlet类继承了GenericServlet抽象类)
 *  开发中经常用的是继承HttpServlet类,但是学习还得从Servlet接口开始学
 *  
 *  注意Servlet中的方法,99%的情况下是由tomcat来调用的,我们自己不调用.
 *  tomat的源码需要自己去https://tomcat.apache.org/download-80.cgi官网下载
 *  apache-tomcat-8.5.56-src.zip
 *  注意由于tomcat是通过反射调用Servlet的,所以Servlet必须有无参构造方法,否则tomcat就会调用失败,非常严重
 *  域对象就是用来在多个servlet中传递数据的,很显然ServletContext就是域对象
 *  jsp中包含四个域对象,servlet包含三个域对象
 *  既然是传递数据,那么域对象就必须要有存数据和取数据的方法
 *  其实你如果想学习filter和servlet在web.xml里面有什么配置,你就在web.xml的servlet节点下面按alt + / 看提示,
 * alt + / 会把里面的所有key配置的信息都提示出来,这个是xml schema的特性,xml和.xsd我之前好像在哪里学过?好像是慕课网把
 * 使用注解没法定义监听器或者过滤器的顺序
 * 过滤器也可以使用注解@WebFilter，过滤器可以通过注解@Order控制执行顺序,通过@Order控制过滤器的基本,值越小级别越高越先执行.
 * https://developer.ibm.com/zh/articles/j-lo-servlet30/
 * https://developer.ibm.com/zh/tutorials/j-javaee8-servlet4/
 * https://developer.ibm.com/zh/articles/j-lo-servlet/
 * http://v.youku.com/v_show/id_XMTY2NjYzMTMzMg==.html
	http://www.bilibili.com/video/av52476891/
	http://www.bilibili.com/video/av52477378/
	http://v.youku.com/v_show/id_XMjU3MDA3MTY4.html
	https://baijiahao.baidu.com/s?id=1672666159954931466&wfr=content
	http://v.youku.com/v_show/id_XMTI5MTg4NjMzMg==.html
 * @author dell
 */
public class ServletDeom implements Servlet {

	/**
	 * destroy方法会在Servlet被tomcat注销之前调用,并且只会调用一次
	 */
	@Override
	public void destroy() {
		System.out.println("destroy方法会在tomcat停止服务的时候调用");
	}

	/**
	 * 可以用来获取servlet在web.xml里面的配置信息
	 */
	@Override
	public ServletConfig getServletConfig() {
		return null;
	}

	/**
	 * 获取servlet本身自己的信息,这个方法几乎没用
	 */
	@Override
	public String getServletInfo() {
		System.out.println("别人调用这个方法的时候,我可以告诉他我这个servlet是用来处理什么的？");
		return "我是一个快乐的servlet";
	}

	/**
	 * init方法会在Servlet对象创建(Servlet对象由tomcat创建)之后马上执行,并只执行一次。
	 * tomcat会在你第一次访问这个servlet的时候调用init方法
	 * 注意tomcat调用init方法时,tomcat会传一个ServletConfig对象过来
	 */
	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		System.out.println("tomcat会在你第一次访问这个servlet的时候调用一次init方法,这说明servlet是单例模式,多个用户同时访问的时候,会有线程安全问题");
		System.out.println("你可以在这个方法里面记录用户是什么时候开始用这个功能的");
		//还有一种方式
		System.out.println("web.xml中load-on-startup的意思是在tomcat服务启动的时候创建servlet的对象,而不是在第一次方法servlet的时候创建load-on-startup的值必须为非负整数,值越小的越先创建");
		/**
		 * ServletConfig是什么?
		 * tomcat会将web.xml里面的servlet节点里面的内容解析出来放到ServletConfig里面
		 * ServletConfig只代表本Servlet的配置信息
		 */
		String name = servletConfig.getServletName();
		System.out.println("获取web.xml里面配置的servlet-name:" + name);
		
		String initParam = servletConfig.getInitParameter("p1");//获取
		System.out.println("获取在web.xml里面servlet下面配置的init-param下面的param-value的参数值:" + initParam);
		
		Enumeration<String> params = servletConfig.getInitParameterNames();
		while (params.hasMoreElements()) {
			System.out.println("获取在web.xml里面servlet下面配置的init-param下面的param-name:" + params.nextElement());
		}
		
		ServletContext sc = servletConfig.getServletContext();//获取servlet上下文对象
		System.out.println("一个项目只有一个ServletContext对象,所以我们可以在N多个Servlet中来获取这个唯一的对象,使用他可以给多个");
		System.out.println("servlet传递数据,所以大多数项目中都给ServletContext对象起名叫applicationContext");
		System.out.println("ServletContext这个对象在tomcat启动时就创建,在tomcat关闭时才会死去");
		
		/**
		 * 域对象就是用来在多个servlet中传递数据的,很显然ServletContext就是域对象
		 *  jsp中包含四个域对象,servlet包含三个域对象
		 *  既然是传递数据,那么域对象就必须要有存数据和取数据的方法
		 */
		sc.setAttribute("name", "张三");
		String str = (String)sc.getAttribute("name");
		System.out.println("利用ServletContext对象可以在多个servlet之间传递数据" + str);
		Enumeration<String> enumer = sc.getAttributeNames();//取出ServletContext中的所有key值
		while (enumer.hasMoreElements()) {
			String key = enumer.nextElement();
			System.out.println("ServletContext对象中存储的所有数据" + key);
			System.out.println("ServletContext对象中存储的所有数据" + (String)sc.getAttribute(name));
		}
		String initContextParam = sc.getInitParameter("context-param");
		System.out.println("getInitParameter获取的是在Web.xml里面配置的context-param参数");
		
		//getRealPath得到请求路径,得到的是有盘符的路径,F:/XX/11/FSS
		String path = sc.getRealPath("/index.jsp");
		//通过这个真实路径可以创建一个file对象,也可以获取一个InputStream
		
		//获取资源的路径后,在创建出输入流对象,/index.jsp这个的意思就是项目跟目录下面的index.jsp文件
		InputStream is = sc.getResourceAsStream("/index.jsp");
		
		//getResourcePaths("/WEB-INF")的意思是得到项目跟目录下面WEB-INF的的所有文件,但是只是WEB-INF下面的一层而已
		//getResourcePaths和getResourceAsStream必须以/开头
		Set<String> setStr = sc.getResourcePaths("/WEB-INF");
	}

	/**
	 * service方法会被调用多次,每次处理请求都是在调用这个方法
	 */
	@Override
	public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
		System.out.println("service方法被调用了,我现在拦截的是/*,/*的意思就是当前servlet是一个默认的servlet,当请求没人处理时我就处理");
		System.out.println("/*的相当于tomcat的conf/web.xm里面配置的DefaultServlet,DefaultServlet配置的url-pattern是一个斜杠/,拦截所有请求,但是匹配优先级最低");
	}
}
