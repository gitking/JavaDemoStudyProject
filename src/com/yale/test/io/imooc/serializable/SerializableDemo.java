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
 * 父类没有实现序列化接口,子类实现了序列化接口,那么子类可以被序列化吗？子类被序列化还原的时候,父类的构造方法会被调用吗？
 * 答:不会,反序列化时JVM会把新的对象配置在堆上,但构造函数不会执行。很明显的,如果执行了构造函数对象的状态就会被抹去(初始化),而这不是我们想要的结果。
 * 我们需要对象回到存储时的状态。如果对象在继承树上有个不可序列化的祖先类,则该不可序列化类以及在它之上的类的构造函数(就算是可序列化也一样)就会执行。一旦构造函数连锁启动之后将无法停止。
 * 也就是说,从第一个不可序列化的父类开始,全部都会重新初始化状态。
 * 对象的实例变量会被还原成序列化时点的状态值。transient变量会被赋值null的对象引用或primitive主数据类型的默认为0,false等值。
 * 静态变量不会被序列化,因为静态变量是属于类的不是属于对象的，static代表"每个类一个"而不是每个对象一个。当对象被还原时,静态变量会维持类中原本的样子,而不是存储时的样子。
 * 问:为什么类不会存储成对象的一部分？这样就不会出现找不到类的问题了？
 * 答:当然也可以设计成这个样子,但这样是非常的浪费且会有很多额外的工作。虽然把对象序列化写在本机的硬盘上面不是什么很困难的工作,但序列化也有将对象送到网络联机上的用途。
 * 如果每个序列化对象都带有类,宽度的消耗可能就是个大问题。
 * 对于通过网络传送序列化对象来说,事实上是有一种机制可以让类使用URL来指定位置。该机制用在java的Remote Method Invocation(RMI,远程程序调用机制),
 * 让你可以把序列化的对象当作参数的一部分来传送,若接收此调用的java虚拟机没有这个类的话,它可以自动地使用URL来取回并加载该类(第17章会讨论RMI)。
 * Version ID:序列化的识别,使用serialVersionUID
 * 会损害解序列化的修改:
 * 删除实例变量。
 * 改变实例变量的类型。
 * 将非瞬时的实例变量改为瞬时的。
 * 改变类的继承层次。
 * 将类从可序列化改成不可序列化。
 * 将实例变量改成静态的。
 * 通常不会有事的修改:
 * 加入新的实例变量(还原时会使用默认值)。
 * 在继承层次中加入新的类。
 * 从继承层次中删除类。
 * 不会影响解序列化程序设定变量值的存取层次修改。
 * 将实例变量从瞬时改成非瞬时(会使用默认值)。
 * 要想知道某个类的serialVersionUID,可以使用Java Development Kit里面所带的serialver工具来查询。
 * 比如在命令行上面输入:serialver Dog
 * 当你的类可能会在产生序列化对象之后继续演进时......
 * 先使用serialver工具来取得版本ID,把输出拷贝到类上。在修改类的时候要确定修改程序的后果!例如,新的Dog要能够处理旧的Dog解序列化之后新加入变量的默认值。
 * 《Head First Java》
 */
public class SerializableDemo {

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("demo/obj1.ser"));
		Foo2 foo2 = new Foo2();
		oos.writeObject(foo2);
		oos.flush();
		oos.close();
		
		ObjectInputStream osi = new ObjectInputStream(new FileInputStream("demo/obj1.ser"));
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