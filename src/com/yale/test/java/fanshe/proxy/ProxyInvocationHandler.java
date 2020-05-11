package com.yale.test.java.fanshe.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

interface ISubjectSec {
	public void eat(String msg, int num);
}

class RealSubjectSec implements ISubjectSec {
	public void eat(String msg, int num) {
		System.out.println("我要吃" + num + "份量的:" + msg);
	}
}

/**
 * InvocationHandler动态代理实现的标识接口,只有实现此类接口的子类才具备有动态代理的功能。
 * InvocationHandler类里面只有一个方法invoke,
 * invoke方法表示的是调用执行方法,但是所有的代理类返回给用户的接口对象都属于代理对象,当用户执行接口方法的时候
 * 所调用的实例化对象就是改代理主题动态创建的一个接口对象
 * @author dell
 *
 */
class ProxySubjectSec implements InvocationHandler {//是一个动态代理类
	
	private Object target;//绑定任意的接口对象,使用Object接收
	
	/**
	 * 实现真实对象的绑定处理,同时返回代理对象
	 * @param target
	 * @return 返回一个代理对象(这个对象是根据接口定义动态创建形成的代理对象 )
	 */
	public Object bind(Object target) {
		this.target = target;//保存真实主题对象
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	public void prepare() {
		System.out.println("【ProxySubject】准备食材!");
	}
	
	public void clear() {
		System.out.println("【ProxySubject】收拾碗筷!");
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("这里的对象是由Proxy.newProxyInstance这个方法创建的代理对象:" + proxy.getClass());
		System.out.println("这个用户真实的方法:" + method);
		System.out.println("这个是真实传进来的方法参数:" +Arrays.toString(args));
		System.out.println("------------------------------------------");
		this.prepare();
		Object ret = method.invoke(this.target, args);
		this.clear();
		return ret;
	}
}
public class ProxyInvocationHandler {
	/**
	 * 动态代理实现完成了,但是所有的代理设计模式都会存在一个问题:离不开接口,也就是说所有的代理设计模式的核心就
	 * 是需要接口,于是有人提出了疑问:老子不要接口,也要用代理设计,而且要搞动态代理设计。
	 * 这个要求现在最关键的因素在于需要实现没有接口的动态代理设计模式,那么需要清楚一点,代理类对象动态创建的语法:
	 * 注意下面这个方法的第二个参数,必须传接口
	 * return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	 * 那么此如果要想实现这样的变态要求:就必须依靠另外的第三方组件包:cglib开发包,这个开发包才能够帮助用户实现这类的要求,这个开发包是由:sourceforge.net(sf.net) https://sourceforge.net/提供的开发包。
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//保存生成的代理类 class文件,会将JVM动态生成的代理类的class文件保存下来com.yale.test.java.fanshe.proxy.$Proxy0  
	    //JDK1.7是这样写的, System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");jdk8及之前：
		//jdk8之后：
		System.setProperty("jdk.proxy.ProxyGenerator.saveGeneratedFiles", "true");
		/**
		 * https://www.iteye.com/blog/rednaxelafx-727938
		 * https://www.iteye.com/blog/rednaxelafx-548536
		 * 上面这俩篇文章是讲,如果将JVM动态生成的class文件保存下来的
		 */
		ISubjectSec subject = (ISubjectSec)new ProxySubjectSec().bind(new RealSubjectSec());
		subject.eat("鱼香肉丝", 20);
	}
}

//InvocationHandler动态代理实现的标识接口,只有实现此类接口的子类才具备有动态代理的功能。
interface InvocationHandler1 {
	/**
	 * invoke方法表示的是调用执行方法,但是所有的代理类返回给用户的接口对象都属于代理对象,当用户执行接口方法的时候
	 * @param proxy 表示的是被代理的对象
	 * @param method 返回的是被调用的方法对象,取得了Method对象意味着可以使用invoke()反射调用方法
	 * @param args 表示方法中收到的参数
	 * @return 方法的返回值
	 * @throws Throwable 可能产生的各种类型的Exception或Error
	 */
   public Object invoke(Object proxy, Method method, Object[] args)
       throws Throwable;
}