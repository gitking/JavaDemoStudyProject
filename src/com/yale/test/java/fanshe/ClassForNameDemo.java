package com.yale.test.java.fanshe;

class ForName {
	public ForName() {
		System.out.println("构造方法先执行还是,static块先执行");
	}
	//static属性和static代码块是按照代码的先后顺序执行的
	private static ForName forNam = new ForName();
	static {
		System.out.println("Class.forName相当于JVM加载类,加载类的时候会执行类的静态代码块");
		System.out.println("不过JDBC那个,把类加载到JVM里面有什么用呢?又不用他,jdbc为啥必须用Class.forname???");
    	try {
    		//这里用的是一个参数版的forName()，也就是使用当前方法所在类的ClassLoader来加载
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("OracleDriver有啥用呢,又不用OracleDriver类上面的方法和属性");
	}
	private static ClassForNameDemo classForNameDemo = new ClassForNameDemo();
}
public class ClassForNameDemo {
	public ClassForNameDemo() {
		System.out.println("ClassForNameDemo构造方法先执行还是,static块先执行");
	}
	public static void main(String[] args) throws ClassNotFoundException {
		/**
		 * Class类描述的是整个类的信息,在Class类中提供的forName()方法它所能处理的只是通过CLASSPATH配置的路径进行类的
		 * 加载了,那么如果说现在你的类的加载路径可能是网络，文件、数据库。这个时候我们必须实现类加载器,也就是ClassLoader类的主要作用
		 * 这里涉及JVM加载类的场景,在什么情况下JVM才会加载class文件。
		 * https://www.jianshu.com/p/3a3edbcd8f24
		 * https://zhuanlan.zhihu.com/p/28909673
		 * https://juejin.im/post/5e47b8bd6fb9a07c7c2d56cd
		 * 你项目如果没引入  Driver 的 lib。你写不了  Driver.class ，你import不到。  
		 * 但是有可能这个lib 是外部，容器已经加载了，那么是有这个类的。比如tomcat  共用的一个 lib
		 * 想啥呢兄弟，你如果想动态加载类的话要么classloader.loadClass要么Class.forName，但是你还要执行static代码块，
		 * 所以你只加载是不够的，你得初始化才能执行static块。classloader.loadClass和Class.forName用的都是native方法forName0，
		 * 具体到forName0其实有这样一段代码
		 * classloader.loadClass传的jboolean init是false，Class.forName是true，简单的说classloader.loadClass只有生命周期里的加载阶段，
		 * 而Class.forName完成了加载、验证、准备、解析和初始化
		 */
		Class.forName("com.yale.test.java.fanshe.ForName");//所以那个地方要用Class.forName，至于不用也行大家也说了SPI
		System.out.println();
		ForName fn = new ForName();//Class.forName和new对象的时候JVM才会加载这个类,并且执行类中的static块代码
		Class<?> cls = ForName.class;//这种叫静态加载类,静态加载类不会初始化类,把上面那个代码注释执行这个发现,上面的静态代码块没有被执行
		Class test = ForName.class.getClass();//这种叫静态加载类,静态加载类不会初始化类,这行代码也不能让JVM记载类,上面的静态代码块也不会被执行
	}
}
