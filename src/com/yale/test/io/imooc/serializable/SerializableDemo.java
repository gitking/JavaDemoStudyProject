package com.yale.test.io.imooc.serializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableDemo {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("demo/obj1.dat"));
		Foo2 foo2 = new Foo2();
		oos.writeObject(foo2);
		oos.flush();
		oos.close();
		
		ObjectInputStream osi = new ObjectInputStream(new FileInputStream("demo/obj1.dat"));
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