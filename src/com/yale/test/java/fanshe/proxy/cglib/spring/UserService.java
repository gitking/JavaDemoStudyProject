package com.yale.test.java.fanshe.proxy.cglib.spring;

import java.time.ZoneId;

import org.springframework.stereotype.Component;


@Component
public class UserService {
	/*
	 * https://zhuanlan.zhihu.com/p/131584403
	 * 实际上，成员变量的初始化是在构造方法中完成的。这是我们看到的代码：
	 * public class UserService {
		    public final ZoneId zoneId = ZoneId.systemDefault();
		    public UserService() {
		    }
		}
		这是编译器实际编译的代码：编译后的代码
		public class UserService {
		    public final ZoneId zoneId;
		    public UserService() {
		        super(); // 构造方法的第一行代码总是调用super()
		        zoneId = ZoneId.systemDefault(); // 继续初始化成员变量
		    }
		}
	 * 然而，对于Spring通过CGLIB动态创建的UserService$$EnhancerBySpringCGLIB代理类，它的构造方法中，并未调用super()，因此，从父类继承的成员变量，包括final类型的成员变量，统统都没有初始化。
	 * 有的童鞋会问：Java语言规定，任何类的构造方法，第一行必须调用super()，如果没有，编译器会自动加上，怎么Spring的CGLIB就可以搞特殊？
	 * 这是因为自动加super()的功能是Java编译器实现的，它发现你没加，就自动给加上，发现你加错了，就报编译错误。但实际上，如果直接构造字节码，一个类的构造方法中，不一定非要调用super()。Spring使用CGLIB构造的Proxy类，是直接生成字节码，并没有源码-编译-字节码这个步骤，因此：
	 * Spring通过CGLIB创建的代理类，不会初始化代理类自身继承的任何成员变量，包括final类型的成员变量！
	 */
	public final ZoneId zoneId = ZoneId.systemDefault();//初始化成员变量
	
	public UserService() {//构造方法
		System.out.println("UserService():init.....");
		System.out.println("UserService():zoneId = " + this.zoneId);
	}
	
	public ZoneId getZoneId() {//public 方法
		return zoneId;
	}
	
	public final ZoneId getFinalZoneId() {//public final 方法
		return zoneId;
	}
}
