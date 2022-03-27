package com.yale.test.java.classpath.rmi.cnblogs;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class PublicKnown extends Message implements Serializable{
	private static final long serialVersionUID = 2535402188251947889L;
	private String param;
	
	public void setParam(String param) {
		this.param = param;
	}
	
	/**
	 * 在服务端的readObject处下断点，即可看到调用栈，经过ConnectHandler后就能够确定服务端要反序列化的类名
	 * 接下来就是通过反射调用PublicKnown类的readObject方法 ，进而到达readObject内部的命令执行代码段
	 * 在服务端的readObject处下断点，即可看到调用栈，经过ConnectHandler后就能够确定服务端要反序列化的类名
	 * 接下来就是通过反射调用PublicKnown类的readObject方法 ，进而到达readObject内部的命令执行代码段
	 * 所以这里客户端肯定要知道服务端有哪些可以调用的方法，以及服务端被调用的方法入口参数要满足要求，这里在现实情况中应该很少能够遇到，这里肯定只作为例子来学习。
	 * 当然反序列化的类可以是本地的gadget，这个例子的测试没有jdk版本限制，在jdk1.8.202也可以成功，这些限制太大了。在这里实际上就是拿到ServicesImpl的引用，
	 * lookup函数查找的也一定是存在与rmi注册表中存在的对象并拿到引用，并不是直接拷贝了一份该类的对象到本地来，拿到引用之后再去调用该类的方法，传参到服务端，
	 * 最后反序列化执行在服务端。
	 * @param in
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
		/**
		 * readObject这个方法相关信息可以看JDK自带的jar包rt.jar里面的java.io.Serializable.class里面的Java doc源码。
		 * 结合com.yale.test.io.imooc.serializable.SerializableDemo.java一起看
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1298366845681698#0
		 * 
		 * private void readObject(ObjectInputStream in)这个方法会在反序列化的时候被JVM通过反射调用，调用路径为：
		 * ObjectInputStream.readObject()->ObjectInputStream.readObject0->ObjectInputStream.readOrdinaryObject()
		 * ->ObjectInputStream.readSerialData()->ObjectStreamClass.invokeReadObject(obj, this)->ObjectStreamClass.readObjectMethod.invoke(obj, new Object[]{ in });
		 * 那为啥要有这个方法呢？可以看下HashSet的源码,HashSet的源码里面也有private void readObject(ObjectInputStream in)这个方法。
		 * 注意在java.io.ObjectStreamClass.class的构造方法里面会通过反射获取"writeObject"和"readObject"和"readObjectNoData"和
		 * "writeReplace"和"readResolve"方法。
		 *  readObjectMethod = getPrivateMethod(cl, "readObject",
                            new Class<?>[] { ObjectInputStream.class },
                            Void.TYPE);
         * 最后，我们也知道源码里面，是在哪里获得了，我们单例时，所写的那个private 的 readResolve方法的引用了。那么又在整个流程中哪个步骤，通过那个引用，
         * 实际调用了我们的readResolve方法呢？答案就是在一开始很前面的ObjectInputStream类的readOrdinaryObject方法，在调用完readSerialData（）方法后，
         * 就调用了 ObjectStreamClass类的Object invokeReadResolve(Object obj)方法，通过反射调用了我们自己写的readResolve方法，这里不再展开述说了。
		 * 结合com.yale.test.io.imooc.serializable.Student.java一起看
		 * https://blog.csdn.net/u014653197/article/details/78114041
		 * 我们都知道，序列化不会自动保存static和transient变量，因此我们若要保存它们，则需要通过writeObject()和readObject()去手动读写。
		 * (01) 通过writeObject()方法，写入要保存的变量。writeObject的原始定义是在ObjectOutputStream.java中，我们按照如下示例覆盖即可：
		 * 
		 * in.defaultReadObject()这个方法,从此流中读取当前类的非静态和非瞬态字段。 这只能从被反序列化的类的 readObject 方法调用。 如果以其他方式调用，它将抛出 NotActiveException。 
		 */
		in.defaultReadObject();//这行代码必须有,jvm默认的反序列化操作
		System.out.println("这行代码会在服务端运行");
		System.out.println("this不为null:--->" + this + ",参数为null:--->" + this.param);
		if (this == null) {
			System.out.println("this不为null:" + this);
		}
		Runtime.getRuntime().exec(this.param);
	}
}
