package com.yale.test.io.imooc.serializable;

import java.io.IOException;
import java.io.Serializable;

/**
 * java的serialization提供了一个非常棒的存储对象状态的机制，说白了serialization就是把对象的状态存储到硬盘上 去，等需要的时候就可以再把它读出来使用。
 * 有些时候像银行卡号这些字段是不希望在网络上传输的，transient的作用就是把这个字段的生命周期仅存于调用者的内存中而不会写到磁盘里持久化，意思是transient修饰的age字段，他的生命周期仅仅在内存中，不会被写到磁盘中。
 * Java序列化提供两种方式。
 * 一种是实现Serializable接口,
 * 另一种是实现Exteranlizable接口。 需要重写writeExternal和readExternal方法，它的效率比Serializable高一些，并且可以决定哪些属性需要序列化（即使是transient修饰的），但是对大量对象，或者重复对象，则效率低。
 * 从上面的这两种序列化方式，我想你已经看到了，使用Exteranlizable接口实现序列化时，我们自己指定那些属性是需要序列化的，即使是transient修饰的。下面就验证一下
 * 结果基本上验证了我们的猜想，也就是说，实现了Externalizable接口，哪一个属性被序列化使我们手动去指定的，即使是transient关键字修饰也不起作用。
 * 静态变量不管是不是transient关键字修饰，都不会被序列化
 * 序列化中如果父类已经实现了序列化接口,子类就不用实现这个接口,子类相当于已经实现了序列化接口
 * https://baijiahao.baidu.com/s?id=1636557218432721275&wfr=spider&for=pc
 * 慕课网教程 第6章 对象的序列化和反序列化
 *  介绍对象的序列化和反序列化，transient及ArrayList源码分析以及序列化中子父类构造函数问题。 
 * https://www.imooc.com/video/3645  《文件传输基础——Java IO流》 6-1序列化基本操作
 * @author dell
 */
public class Student implements Serializable{
	private String name;
	private String sex;
	private transient int age;//用transient修改的属性不会进行默认序列化,也可以自己强制序列化
	public Student() {
		
	}
	
	public Student(String name, String sex, int age) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", sex=" + sex + ", age=" + age + "]";
	}
	
	/**
	 * writeObject这个方法是ArrayList里面的一个方法
	 * arrayList为啥要这么做呢？因为ArrayList里面是一个数组,数组有可能没有放满,
	 * 只需要序列化数组里面有值的元素
	 * 我们都知道，序列化不会自动保存static和transient变量，因此我们若要保存它们，则需要通过writeObject()和readObject()去手动读写。
	 * (01) 通过writeObject()方法，写入要保存的变量。writeObject的原始定义是在ObjectOutputStream.java中，我们按照如下示例覆盖即可：
	 * @param s
	 * @throws java.io.IOException
	 */
	 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
        s.defaultWriteObject();//jvm默认的序列化,把jvm能默认序列化的元素进行序列化操作
        s.writeInt(age);//自己完成age的序列化,age是用transient修饰的,默认不序列化
        /**
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
		 * 结合com.yale.test.java.classpath.rmi.cnblogs.PublicKnown.java一起看
		 * https://blog.csdn.net/u014653197/article/details/78114041
         */
	 }
	 
	 
	 /**
	  * 可以看一下ArrayList的源码,ArrayList这个类的elementData这个属性就是用transient修饰的,但是不代表elementData这个属性就不能序列化
	  * ArrayList源码里面就有writeObject和readObject这俩个方法,在这俩个方法里面ArrayList完成了elementData属性的序列化
	  * ArrayList这样做是为了提高性能,因为elementData属性是一个数组,这个数组可能并没有被用完,序列化的时候只需要序列化有用到的元素就可以了.
	  * https://www.imooc.com/video/3646 慕课网 《文件传输基础——Java IO流》 6-2 transient及ArrayList源码分析 (12:41)
	  */
	 private void readObject(java.io.ObjectInputStream s) throws ClassNotFoundException, IOException {
		 s.defaultReadObject();//jvm默认的反序列化操作,把jvm能默认反序列化的元素进行反序列化操作
		 this.age = s.readInt();//自己完成age的反序列化操作
	 }
}
