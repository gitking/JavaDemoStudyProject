package com.yale.test.io.file;

import java.io.IOException;
import java.io.InputStream;

/*
 * 从classpath读取文件就可以避免不同环境下文件路径不一致的问题：如果我们把default.properties文件放到classpath中，
 * 就不用关心它的实际存放路径。在classpath中的资源文件，路径总是以／开头，我们先获取当前的Class对象，
 * 然后调用getResourceAsStream()就可以直接从classpath读取任意的资源文件：
 * 调用getResourceAsStream()需要特别注意的一点是，如果资源文件不存在，它将返回null。因此，我们需要检查返回的InputStream是否为null，
 * 如果为null，表示资源文件在classpath中没有找到：
 * 如果我们把默认的配置放到jar包中，再从外部文件系统读取一个可选的配置文件，就可以做到既有默认的配置文件，又可以让用户自己修改配置：
 * Properties props = new Properties();
 * props.load(inputStreamFromClassPath("/default.properties"));
 * props.load(inputStreamFromFile("./conf.properties"));
 * 这样读取配置文件，应用程序启动就更加灵活。
 * Class对象的getResourceAsStream()可以从classpath中读取指定资源；
 * JVM对一个类只会加载一次，存在多个版本的，classpath在前的先加载，在后的永远加载不了
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1298366384308257
 * @author dell
 */
public class ClassPathInput {
	public static void main(String[] args) throws IOException {
		InputStream is = ClassPathInput.class.getResourceAsStream("/city.xml");
		byte[] cont = new byte[1024];
		while ((is.read(cont, 0, cont.length)) != -1) {
			System.out.println("从classpath读取文件:" + new String(cont));
		}
		is.close();
		
		//也可以从getClassLoader调用getResourceAsStream方法
		InputStream iss = ClassPathInput.class.getClassLoader().getResourceAsStream("city.xml");
		if (iss != null) {
			byte[] cont1 = new byte[1024];
			while ((iss.read(cont1, 0, cont1.length)) != -1) {
				System.out.println("从classpathgetResourceAsStream参数前面不加斜杠默认读取当前类下面的路径:" + new String(cont1));
			}
		}
		iss.close();
	}
}
