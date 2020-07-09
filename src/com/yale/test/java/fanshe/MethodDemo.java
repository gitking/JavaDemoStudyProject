package com.yale.test.java.fanshe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Per {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
/**
 * 【强制】避免用Apache Beanutils进行属性的copy。 
 * 说明：Apache BeanUtils性能较差，可以使用其他方案比如Spring BeanUtils, Cglib BeanCopier，注意均是浅拷贝。《阿里巴巴Java开发手册（泰山版）.
 * @author dell
 */
public class MethodDemo {

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		//Class<?> cls = Per.class;这个更下面这个是一样的
		Class<?> cls = Class.forName("com.yale.test.java.fanshe.Per");
		/*
		 * Method getMethod(name, Class...)：获取某个public的Method（包括父类）
		 * Method getDeclaredMethod(name, Class...)：获取当前类的某个Method（不包括父类）
		 * Method[] getMethods()：获取所有public的Method（包括父类）
		 * Method[] getDeclaredMethods()：获取当前类的所有Method（不包括父类）
	     * getName()：返回方法名称，例如："getScore"；
	     * getReturnType()：返回方法返回值类型，也是一个Class实例，例如：String.class；
	     * getParameterTypes()：返回方法的参数类型，是一个Class数组，例如：{String.class, int.class}；
	     * getModifiers()：返回方法的修饰符，它是一个int，不同的bit表示不同的含义。
		 */
		Method[] met = cls.getMethods();
		for (int i=0;i<met.length;i++) {
			System.out.println("这里会把Object父类的方法也打印出来:" + met[i]);
		}
		String attribute = "name";
		String value = "MLDN";
		/**
		 * 取得setName这个方法的实例化对象,设置方法的名称和参数类型
		 * setName()是方法名称,但是这个方法名称是我根据给定的属性信息拼凑得来的,同时该方法
		 * 需要接收一个String类型的参数
		 */
		Method setMethod = cls.getMethod("set" + initcap(attribute), String.class);
		Object obj = cls.newInstance();//任何情况下调用类中的普通方法都必须有实例化对象
		//随后需要通过Method类对象调用指定的方法,调用方法必须有实例化对象,同时要传入一个参数
		setMethod.invoke(obj, value);//相当于:Person对象.setName(value)
		
		Method getMethod = cls.getMethod("get" + initcap(attribute));
		Object ret = getMethod.invoke(obj);//相当于Person对象.getName()
		System.out.println("通过反射调用对象的方法得到的返回值" + ret);
		
		/*
		 * 如果获取到的Method表示一个静态方法，调用静态方法时，由于无需指定实例对象，所以invoke方法传入的第一个参数永远为null。我们以Integer.parseInt(String)为例：
		 */
		Method m= Integer.class.getMethod("parseIne", String.class);
		Integer res = (Integer)m.invoke(null, "99");
		System.out.println(res);
		
		/*
		 * 和Field类似，对于非public方法，我们虽然可以通过Class.getDeclaredMethod()获取该方法实例，但直接对其调用将得到一个IllegalAccessException。
		 * 为了调用非public方法，我们通过Method.setAccessible(true)允许其调用：
		 * 使用反射调用方法时，仍然遵循多态原则：即总是调用实际类型的覆写方法（如果存在）。
		 */
	}
	public static String initcap(String str) {
		return str.substring(0, 1).toUpperCase() + str.substring(1);
		
	}
}
