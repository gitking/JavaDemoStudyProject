package com.yale.test.java.fanshe;

class ForName {
	static {
		System.out.println("Class.forName相当于JVM加载类,加载类的时候会执行类的静态代码块");
		System.out.println("不过JDBC那个,把类加载到JVM里面有什么用呢?又不用他,jdbc为啥必须用Class.forname???");
    	try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("OracleDriver有啥用呢,又不用OracleDriver类上面的方法和属性");
	}
}
public class ClassForNameDemo {
	public static void main(String[] args) throws ClassNotFoundException {
		/**
		 * Class类描述的是整个类的信息,在Class类中提供的forName()方法它所能处理的只是通过CLASSPATH配置的路径进行类的
		 * 加载了,那么如果说现在你的类的加载路径可能是网络，文件、数据库。这个时候我们必须实现类加载器,也就是ClassLoader类的主要作用
		 */
		Class.forName("com.yale.test.java.fanshe.ForName");
		Class<?> cls = ForName.class;//把上面那个代码注释执行这个发现,上面的静态代码块没有被执行
	}
}
