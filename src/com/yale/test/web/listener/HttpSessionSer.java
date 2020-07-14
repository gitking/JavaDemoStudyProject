package com.yale.test.web.listener;

import java.io.Serializable;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

/**
 * 当tomcat钝化(序列化)httpSesion的时候会触发这个监听器
 * HttpSessionSer 这个类必须要实现序列化接口Serializable,要不然tomcat序列化的时候,不会序列化这个类,
 * 这个Listener不用配置到web.xml里面
 * http://localhost:8080/pcis/session/sessionDemo.jsp
 * Tomcat中俩种Session钝化管理器:
 * 	Session钝化机制由SessionManager管理:
 * org.apache.catalina.session.StandardManager
 * 	当Tomcat服务器被关闭或重启时,tomcat服务器会将当前内存中的Session对象钝化到服务器文件系统中;
 *  另一种情况是Web应用程序被重新加载时(重新加载是指:重新覆盖了web.xml或者重新覆盖了Spring.xml或者我们在Tomcat管理器里面点击了reload这个重新加载按钮的时候就会触发重新加载),
 *  内存中的Session对象也会被钝化到服务器文件系统中。
 *  钝化后的文件被保存到Tomcat安装路径:/work/Catalina/hostname/applicantionname/SESSION.ser
 * org.apache.catalina.session.PersistentManager
 * 	首先在钝化的基础上进行了扩充,第一种情况如上面的1.第二种情况如上面2,第三种情况,可以配置主流内存的Session对象数目,将长时间不活动的
 * Session对象保存到文件或者数据库中,当用时再重新加载.默认情况下,tomcat提供俩个钝化驱动类,
 * org.apache.catalina.session.FileStore和org.apache.catalina.session.JDBCStore
 * HttpSessionBindingListener和HttpSessionActivationListener这俩个监听器不需要在web.xml里面注册
 * @author dell
 */
public class HttpSessionSer implements HttpSessionActivationListener, Serializable {

	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		//http://localhost:8080/pcis/session/sessionDemo.jsp
		System.out.println("我这个httpSession已经很久没活动了,tomcat要钝化(序列化)我");
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		//http://localhost:8080/pcis/session/sessionDemoGet.jsp
		System.out.println("我这个httpSession现在恢复活动了,tomcat要活化(反序列化)我");
	}
}
