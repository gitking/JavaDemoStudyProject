package com.yale.test.web.listener;

import java.io.Serializable;

import javax.servlet.http.HttpSessionActivationListener;
import javax.servlet.http.HttpSessionEvent;

/**
 * 当tomcat钝化(序列化)httpSesion的时候会触发这个监听器
 * HttpSessionSer 这个类必须要实现序列化接口Serializable,要不然tomcat序列化的时候,不会序列化这个类,
 * 这个Listener不用配置到web.xml里面
 * @author dell
 */
public class HttpSessionSer implements HttpSessionActivationListener, Serializable {

	@Override
	public void sessionWillPassivate(HttpSessionEvent se) {
		System.out.println("我这个httpSession已经很久没活动了,tomcat要钝化(序列化)我");
	}

	@Override
	public void sessionDidActivate(HttpSessionEvent se) {
		System.out.println("我这个httpSession现在恢复活动了,tomcat要活化(反序列化)我");
	}
}
