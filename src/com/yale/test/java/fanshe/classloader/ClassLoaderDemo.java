package com.yale.test.java.fanshe.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * ClassLoader是一个抽象类,但是却没有一个抽象方法需要子类实现,
 * 这样的目的不想让别人直接实例化ClassLoader类
 * @author dell
 *
 */
class MyClassLoader extends ClassLoader {
	public Class<?> loadData(String className ) throws IOException{
		byte[] classData = this.loadCLassData();
		return super.defineClass(className, classData, 0, classData.length);
	}
	
	private byte[] loadCLassData() throws IOException {
		/**
		 * 当你不知道你要读的文件有多大时,需要借助内存流来帮助完成操作
		 */
		InputStream input = new FileInputStream(new File("D:" + File.separator + "JavaDemo" + File.separator + "Loader.class"));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] data = new byte[20];
		int temp =0;
		while((temp = input.read(data)) != -1) {
			bos.write(data, 0, temp);
		}
		byte[] ret = bos.toByteArray();
		input.close();
		bos.close();
		return ret;
	}
}
class Member {//自己编写的类肯定在CLASSPATH之中

	@Override
	public String toString() {
		return "**********我是通过类记载器被实例化的**********";
	}
}
public class ClassLoaderDemo {
		public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {
			Class<?> cls = Member.class;
			System.out.println("类加载器(AppClassLoader应用程序类加载器):" + cls.getClassLoader());
			/**
			 * 对于第三方程序类库(Ext加载)除了ClassPath之外，实际上在JAVA里面还有一个加载目录:D:\JDK8\jre\lib\ext
			 */
			System.out.println("类加载器的父类(ExtClassLoader扩展类加载器):" + cls.getClassLoader().getParent());
			System.out.println("类加载器的父类的父类:" + cls.getClassLoader().getParent().getParent());

			System.out.println(Class.forName("com.yale.test.java.fanshe.classloader.Member").getClassLoader().loadClass("com.yale.test.java.fanshe.classloader.Member").newInstance());
			/**
			 * 类加载器给我们用户最大的帮助在于可以通过动态的路径实现类的加载处理操作。
			 * 比如说你的核心代码不想给客户,防止他们反编译,这个时候就可以把这些类放到你自己的服务器上,让客户的类远程加载class,他就反编译不了。
			 */
			Class<?> diyCls = new MyClassLoader().loadData("cn.mldn.vo.Loader");
			System.out.println("*****自己实现的类加载器,加载硬盘上的Class文件:*****:-->" + diyCls.newInstance());
			System.out.println("这个输出的是我自己定义的ClassLoader" + diyCls.getClassLoader());
			System.out.println("这个输出的是我自己定义的ClassLoader的父类" + diyCls.getClassLoader().getParent());
			System.out.println("这个输出的是我自己定义的ClassLoader的父类的父类" + diyCls.getClassLoader().getParent().getParent());
		}
}
