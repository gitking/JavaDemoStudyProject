package com.yale.test.io.imooc.serializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 * 序列化是指把一个Java对象变成二进制内容，本质上就是一个byte[]数组。
 * 为什么要把Java对象序列化呢？因为序列化后可以把byte[]保存到文件中，或者把byte[]通过网络传输到远程，这样，就相当于把Java对象存储到文件或者通过网络传输出去了。
 * 有序列化，就有反序列化，即把一个二进制内容（也就是byte[]数组）变回Java对象。有了反序列化，保存到文件中的byte[]数组又可以“变回”Java对象，或者从网络上读取byte[]并把它“变回”Java对象。
 * 安全性
 * 因为Java的序列化机制可以导致一个实例能直接从byte[]数组创建，而不经过构造方法，因此，它存在一定的安全隐患。一个精心构造的byte[]数组被反序列化后可以执行特定的Java代码，从而导致严重的安全漏洞。
 * 实际上，Java本身提供的基于对象的序列化和反序列化机制既存在安全性问题，也存在兼容性问题。更好的序列化方法是通过JSON这样的通用数据结构来实现，只输出基本类型（包括String）的内容，而不存储任何与代码相关的信息。
 */
public class SerializableDemo {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("demo/obj1.dat"));
		Foo2 foo2 = new Foo2();
		oos.writeObject(foo2);
		oos.flush();
		oos.close();
		
		ObjectInputStream osi = new ObjectInputStream(new FileInputStream("demo/obj1.dat"));
		/*
		 * readObject()可能抛出的异常有：
		 * ClassNotFoundException：没有找到对应的Class；
		 * InvalidClassException：Class不匹配。
		 * 对于ClassNotFoundException，这种情况常见于一台电脑上的Java程序把一个Java对象，例如，Person对象序列化以后，通过网络传给另一台电脑上的另一个Java程序，但是这台电脑的Java程序并没有定义Person类，所以无法反序列化。
		 * 对于InvalidClassException，这种情况常见于序列化的Person对象定义了一个int类型的age字段，但是反序列化时，Person类定义的age字段被改成了long类型，所以导致class不兼容。
		 * 为了避免这种class定义变动导致的不兼容，Java的序列化允许class定义一个特殊的serialVersionUID静态变量，用于标识Java类的序列化“版本”，通常可以由IDE自动生成。
		 * 如果增加或修改了字段，可以改变serialVersionUID的值，这样就能自动阻止不匹配的class版本：
		 * 要特别注意反序列化的几个重要特点：
		 * 反序列化时，由JVM直接构造出Java对象，不调用构造方法，构造方法内部的代码，在反序列化时根本不可能执行
		 */
		Foo2 foo21 = (Foo2)osi.readObject();
		System.out.println(foo2);//反序列化的时候构造方法没有被执行
		osi.close();
		
		ObjectOutputStream ooss = new ObjectOutputStream(new FileOutputStream("demo/obj12.dat"));
		Bar3 bar2 = new Bar3();
		ooss.writeObject(bar2);
		ooss.flush();
		ooss.close();
		
		ObjectInputStream osis = new ObjectInputStream(new FileInputStream("demo/obj12.dat"));
		Bar3 bar21 = (Bar3)osis.readObject();
		System.out.println("反序列化开始了,注意构造方法的调用");
		System.out.println(bar21);//反序列化的时候构造方法没有被执行
		osis.close();
	}
}

class Foo implements Serializable {
	public Foo() {
		System.out.println("Foo....父类被实例化了");
	}
}

class Foo1 extends Foo {
	public Foo1() {
		System.out.println("Foo1.....子类被实例化了");
	}
}
class Foo2 extends Foo1 {
	public Foo2() {
		System.out.println("Foo2....被实例化了");
	}
}

/**
 * 对子类对象进行反序列化操作时
 * 如果其父类没有实现序列化接口
 * 那么其父类的构造函数会被调用
 * 为什么会这样呢,因为实现序列化的类,可以直接从文件中读到创建出来
 * @author dell
 *
 */
class Bar {
	public Bar() {
		System.out.println("bar...........");
	}
}

class Bar2 extends Bar {
	public Bar2() {
		System.out.println("Bar2.....父类没有实现序列化接口,反序列化的时候构造方法会被调用");
	}
}

class Bar3 extends Bar2 implements Serializable {
	public Bar3() {
		System.out.println("Bar3.....父类没有实现序列化接口,反序列化的时候构造方法会被调用。实现序列化接口的类,反序列化的时候构造方法不会被调用");
	}
}