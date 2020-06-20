package com.yale.test.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

public class AttributeListener implements ServletContextAttributeListener {

	/**
	 * ServletContextAttributeEvent继承ServletContextEvent
	 * 
	 * @param scae
	 */
	@Override
	public void attributeAdded(ServletContextAttributeEvent scae) {
		ServletContext sc = scae.getServletContext();//可以通过ServletContextAttributeEvent得到ServletContext
		System.out.println("您项servletContext中添加了一个名为:" + scae.getName() + ",值为:" + scae.getValue() + ",的属性");
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent scae) {
		System.out.println("del*******servletContext中删除了一个名为:" + scae.getName() + ",值为:" + scae.getValue() + ",的属性");
	}

	/**
	 * 替换属性事件
	 */
	@Override
	public void attributeReplaced(ServletContextAttributeEvent scae) {
		//替换的时候,scae.getValue()返回的是老值
		String key = scae.getName();
		Object obj = scae.getValue();
		if (obj instanceof String) {
			String oldVal = obj.toString();
			System.out.println("servletContext中替换了了一个名为:" + key + ",替换前的值为:" + oldVal + ",的属性");
			System.out.println("那怎么获取最新的值呢？");
			String newVal = (String)scae.getServletContext().getAttribute(key);
			System.out.println("获取新值:" + newVal);
		}
		
	}
}
