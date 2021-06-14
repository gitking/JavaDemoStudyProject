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
	/*
	 * Spring是否支持对静态方法进行Aop增强 https://blog.csdn.net/Dax1n/article/details/105684685
	 * JVM实现代理类比较重要的类sun.misc.ProxyGenerator，生成代理类的方法为generateClassFile源码：注意观察ProxyGenerator里面的generateClassFile()方法
	 * 到此处，已经清楚JDK底层生成代理类时代理哪些方法，其中反射getMethods是可以获取到Class中所有public方法，包括静态方法。
	 * 由于JDK代理是基于接口的，而接口里面又不允许有静态方法，所以是无法代理静态方法的。
	 * 换个角度：基于接口的Jdk代理与基于继承Class的代理本质都是基于继承之后重写指定方法实现的代理，而static方法是属于class的，
	 * 而不是类实例的，无法被重写所以static方法无法代理。除此之外，JDK代理类是基于接口实现生成的，因此对于子类的final方法是可以代理的。
	 * 需要注意：Jdk8中的default方式是实例方法，而非静态方法。
	 */
	public final void eat(String msg, int num) {//这里用不用final都是可以被JDK代理的
		System.out.println("我要吃" + num + "份量的:" + msg);
	}
	
	public void bull(String msg, int num) {
		System.out.println("我要吃" + num + "份量的:" + msg);
	}
}

/**
 * InvocationHandler动态代理实现的标识接口,只有实现此类接口的子类才具备有动态代理的功能。
 * InvocationHandler类里面只有一个方法invoke,
 * invoke方法表示的是调用执行方法,但是所有的代理类返回给用户的接口对象都属于代理对象,当用户执行接口方法的时候
 * 所调用的实例化对象就是改代理主题动态创建的一个接口对象
 * 动态代理分为俩类:基于接口的动态代理(jdk的InvocationHandler)和基于类的动态代理(cglib)
 * 发展到现在用的比较多的是javasist来实现动态代理类,cglib用的也不太多了,主要用javasist
 * 静态代理只能代理一个接口下面的所有实现类,动态代理能代理所有接口的所有实现类
 * 代理的好处:使真实的类更纯净了(减轻了真实类的业务),不再关注一些公共的事情
 * 终于有人把 java 代理 讲清楚了，万字详解！https://xie.infoq.cn/article/9a9387805a496e1485dc8430f
 * Spring是否支持对静态方法进行Aop增强 https://blog.csdn.net/Dax1n/article/details/105684685
 * @author dell
 */
class ProxySubjectSec implements InvocationHandler {//是一个动态代理类
	
	private Object target;//绑定任意的接口对象,使用Object接收
	
	/**
	 * 实现真实对象的绑定处理,同时返回代理对象
	 * 可以看ProxyEasyDemo这个类，这个类显示了JDK动态代理的简单用法.
	 * @param target
	 * @return 返回一个代理对象(这个对象是根据接口定义动态创建形成的代理对象 )
	 */
	public Object bind(Object target) {
		this.target = target;//保存真实主题对象,JVM实现代理类比较重要的类sun.misc.ProxyGenerator，生成代理类的方法为generateClassFile源码：可以看下ProxyGenerator的源码,Spring是否支持对静态方法进行Aop增强 https://blog.csdn.net/Dax1n/article/details/105684685
		return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}
	
	public void prepare() {
		System.out.println("【ProxySubject】准备食材!");
	}
	
	public void clear() {
		System.out.println("【ProxySubject】收拾碗筷!");
	}
	
	//JVM实现代理类比较重要的类sun.misc.ProxyGenerator，生成代理类的方法为generateClassFile源码：
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
		System.out.println("代理类的真实样子:" + subject.getClass());
		subject.eat("鱼香肉丝", 20);
		//subject.bull("鱼香肉丝", 20);JDK的动态代理,只能代理目标类里面实现的接口方法,目标类(RealSubjectSec)自己的独有的方法,JDK动态代理类增强不了
		/*
		 * 如果换成下面这种写法会报错：java.lang.ClassCastException,但是不换成这种写法subject.bull("鱼香肉丝", 20)这个方法都调用不到,因为你引用的
		 * 是接口类ISubjectSec,而接口类ISubjectSec里面没有bull方法.
		 * RealSubjectSec subject = (RealSubjectSec)new ProxySubjectSec().bind(new RealSubjectSec());
		 * System.out.println("代理类的真实样子:" + subject.getClass());
		 * subject.eat("鱼香肉丝", 20);
		 * subject.bull("鱼香肉丝", 20);JDK的动态代理,只能代理目标类里面实现的接口方法,目标类(RealSubjectSec)自己的独有的方法,JDK动态代理类增强不了
		 * Exception in thread "main" java.lang.ClassCastException: com.yale.test.java.fanshe.proxy.$Proxy0 cannot be cast to com.yale.test.java.fanshe.proxy.RealSubjectSec
		 * at com.yale.test.java.fanshe.proxy.ProxyInvocationHandler.main(ProxyInvocationHandler.java:96)
		 * Spring是否支持对静态方法进行Aop增强 https://blog.csdn.net/Dax1n/article/details/105684685
		 */
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