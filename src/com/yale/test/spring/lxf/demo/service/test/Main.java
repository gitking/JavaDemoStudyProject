package com.yale.test.spring.lxf.demo.service.test;

import com.yale.test.spring.lxf.demo.service.ServiceBean;
import com.yale.test.spring.lxf.demo.service.impl.MyServiceBean;

/**
 * 现在，我们已经有了一个业务Bean。要测试它非常容易，因为到目前为止，我们还没有涉及到Spring容器，也没有涉及到任何Web容器（假定这是一个Web应用程序关于用户管理的业务Bean）。
 * 完全可以直接进行Unit测试，或者，简单地写个main方法测试：
 * @author issuser
 */
public class Main {
	public static void main(String[] args) {
		ServiceBean service = new MyServiceBean();
		service.addUser("bill", "hello");
		service.addUser("tom", "goodbye");
		service.addUser("tracy", "morning");
		System.out.println("tom's password is:" + service.getPassword("tom"));
		if (service.findUser("tom")) {
			service.deleteUser("tom");
		}
	}
}
