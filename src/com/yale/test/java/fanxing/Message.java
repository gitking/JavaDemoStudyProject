package com.yale.test.java.fanxing;

import java.util.ArrayList;

/**
 * Java 泛型最早是在 JDK5 的时候才被引入，但是泛型思想最早来自来自 C++ 模板（template）
 * 泛型是一种“代码模板”，可以用一套代码套用各种类型。
 * 在子类实现接口的时候也可以明确给出具体类型
 * Java 需要做到严格的向后兼容性:这里强调一下，这里的向后兼容性指的是二进制兼容性，并不是源码兼容性。也不保证高版本的 Class 文件能够运行在低版本的 JDK 上。
 * 另外 Java 5之前，其实我们就已经有了两套集合容器，一套为 Vector/Hashtable 等容器，一套为 ArrayList/ HashMap。这两套容器的存在，
 * 其实已经引来一些不便，对于新接触的 Java 的开发人员来说，还得学习这两者的区别。
 * @author dell
 * @param <T>
 */
public class Message<T> implements IMessage<T> {
	@Override
	public void print(T t) {
		System.out.println(t.toString());
	}
	
	private T node;

	public T getNode() {
		return node;
	}

	public void setNode(T node) {
		this.node = node;
	}
	
	public static void main(String[] args) {
		/**
		 * 泛型自从 JDK1.5 引进之后，真的非常提高生产力。一个简单的泛型 T，寥寥几行代码， 就可以让我们在使用过程中动态替换成任何想要的类型，
		 * 再也不用实现繁琐的类型转换方法。Java 采用类型擦除（Type erasure generics）的方式实现泛型。用大白话讲就是这个泛型只存在源码(.java文件中)中，
		 * 编译器将源码编译成字节码之时，就会把泛型『擦除』，所以字节码中并不存在泛型。
		 * 编译之后，我们使用 javap -l -c -v Message.class 查看字节码。观察 setNode部分的字节码，从 descriptor 可以看到，泛型 T 已被擦除，最终替换成了 Object。
		 * 并不是每一个泛型参数被擦除类型后都会变成 Object 类，如果泛型类型为  T extends String 这种方式，最终泛型擦除之后将会变成 String。
		 * 同理getParam 方法，泛型返回值也被替换成了 Object。为了保证 String param = genericType.getParam(); 代码的正确性，
		 * 编译器还得在这里插入类型转换(checkcast)。除此之外，编译器还会对泛型安全性防御，如果我们往 ArrayList<String> 添加 Integer,程序编译期间就会报错。
		 * 类型擦除带来的缺陷:不支持基本数据类型
		 * 泛型参数被擦除之后，强制变成了 Object 类型。这么做对于引用类型来说没有什么问题，毕竟 Object 是所有类型的父类型。但是对于 int/long 等八个基本数据类型说，这就难办了。因为 Java 没办法做到int/long 与 Object 的强制转换。
		 * 如果要实现这种转换，需要进行一系列改造，改动难度还不小。所以当时 Java 给出一个简单粗暴的解决方案：既然没办法做到转换，那就索性不支持原始类型泛型了。
		 * 如果需要使用，那就规定使用相关包装类的泛型，比如 ArrayList<Integer>。另外为了开发人员方便，顺便增加了原生数据类型的自动拆箱/装箱的特性。
		 * 正是这种「偷懒」的做法，导致现在我们没办法使用原始类型泛型，又要忍受包装类装箱/拆箱带来的开销，从而又带来运行效率的问题。
		 * 运行效率
		 * 上面字节码例子我们已经看到，泛型擦除之后类型将会变成 Object。当泛型出现在方法输入位置的时候，由于 Java 是可以向上转型的，这里并不需要强制类型转换，所以没有什么问题。
		 * 但是当泛型参数出现在方法的输出位置（返回值）的时候，调用该方法的地方就需要进行向下转换，将 Object 强制转换成所需类型,所以编译器会插入一句 checkcast 字节码。
		 * 除了这个，上面我们还说到原始基本数据类型，编译器还需帮助我们进行装箱/拆箱。
		 * https://mp.weixin.qq.com/s/1cK5sWFyugGAtg3Be285Qg
		 */
		Message<String> genericType = new Message<String>();
		genericType.setNode("6666");
		String nodeStr = genericType.getNode();
		System.out.println(nodeStr);
		
		/**
		 * 为什么需要泛型擦除,因为需要向后兼容.下面这段代码是泛型出现之前的代码,要保证下面这段代码可以正确运行。
		 * 如果 Java 采用第一条路实现方式，那么现在我们可能就会有两套集合类型。以 ArrayList 为例,一套为普通的 java.util.ArrayList，
		 * 一套可能为 java.util.generic.ArrayList<T>。采用这种方案之后，如果开发中需要使用泛型特性，那么直接使用新的类型。另外旧的代码不改动，也可以直接运行在新版本 JDK 中。
		 * 这套方案看起来没什么问题，实际上C# 就是采用这套方案。但是为什么 Java  却没有使用这套方案那?
		 * 这是因为当时 C# 才发布两年，历史代码并不多，如果旧代码需要使用泛型特性，改造起来也很快。但是 Java 不一样，当时 Java 已经发布十年了，已经有很多程序已经运行部署在生产环境，可以想象历史代码非常多。
		 * 如果这些应用在新版本 Java 需要使用泛型，那就需要做大量源码改动，可以想象这个开发工作量。
		 * 另外 Java 5   之前，其实我们就已经有了两套集合容器，一套为 Vector(老)/ArrayList(新)等容器，一套为 Hashtable(老)/ HashMap(新)。这两套容器的存在，其实已经引来一些不便，对于新接触的 Java 的开发人员来说，还得学习这两者的区别。
		 * 如果此时为了泛型再引入新类型，那么就会有四套容器同时并存。想想这个画面，一个新接触开发人员，面对四套容器，完全不知道如何下手选择。如何 Java 真的这么实现了，想必会有更多人吐槽 Java。
		 * 所以 Java 选择第二条路，采用类型擦除，只需要改动 Javac 编译器，不需要改动字节码，不需要改动虚拟机，也保证了之前历史没有泛型的代码还可以在新的 JDK 中运行。
		 * https://mp.weixin.qq.com/s/1cK5sWFyugGAtg3Be285Qg
		 */
		ArrayList listObj = new ArrayList();
		listObj.add(10);//这里实际上被javac编译器自动装箱成Integer类型了
		listObj.add("String类型");
		listObj.add(new Object());
		System.out.println("增加泛型之后要保证上面这段代码可以正确运行.要知道泛型是直接在ArrayList这个类上面增加的功能,如果你不擦除上面这段代码就不能运行");
		System.out.println(",所以必须擦除。如果java不擦除,java就需要新增一个ArrayList类,如果新增加一种泛型ArrayList,java就要被开发人员吐槽死了,java迫于压力没有新增");
		
		ArrayList<String> al = new ArrayList<String>();
		ArrayList<Float> alf = new ArrayList<Float>();
		if (al.getClass() == alf.getClass()) {
			/**
			 * 运行期间无法获取泛型实际类型
			 * 由于编译之后，泛型就被擦除，所以在代码运行期间，Java 虚拟机无法获取泛型的实际类型。
			 * 上面这段代码，从源码上两个 List 看起来是不同类型的集合，但是经过泛型擦除之后，集合都变为 ArrayList。所以 if语句中代码将会被执行。
			 */
			System.out.println("运行期间无法获取泛型实际类型");
		}
		
		
		System.out.println("泛型总结:泛型的缺点:1,由于需要向后兼容,导致泛型会在编译后将泛型擦除。2,运行效率低,由于不支持");
		System.out.println("泛型总结:泛型的缺点:2,由于泛型擦除后,原来的泛型就变成Object类了,所以泛型没办法支持基本类型,因为基本类型int,long不能向上转型为Object类型。");
		System.out.println("泛型总结:泛型的缺点:3,运行效率低,泛型擦除后,原来的泛型就变成Object类了,但是我们在写代码的时候是感觉不到泛型擦除的,"
				+ "这时JVM还要帮我们自动做强制类型转换(泛型如果是Integer,在调用get方法并用int类型接收是,基本类型int这种就是自动装箱拆箱),导致效率低下。");
		System.out.println("泛型总结:泛型的缺点:4,运行期间无法获取泛型实际类型");
		/**
		 * 在泛型没出来之前ArrayList al = new ArrayList();
		 * int i=10;
		 * al.add(i);
		 * 这里的add方法不就是把int变量向上转型成Integer,又把Integer向上转成Object类型了吗？
		 * 按理来说jdk1.5之后泛型支持基本类型不是很自然的事情吗？为啥泛型不支持基本类型呢？
		 * ArrayList<int>这样实现的技术难点在哪里？这样实现有必要吗？我都怀疑我自己了。
		 * 作者回答:是这样的，泛型跟自动装箱/拆箱是 jdk5一起出来的新特性。所以1.5之前是不支持加入 基本数据类型的哈。 支持基本数据类型难点还挺多的，你可以搜下 泛型 rawtype ，挺复杂。
		 * https://mp.weixin.qq.com/s/ceoIJaT1HPtWdXiUPC74Qw
		 * java泛型为什么不支持基本类型呢？
		 * 我自己的理解是这样的,原因还是因为泛型擦除的原因,因为泛型擦除后ArrayList<int>最终一样会变成ArrayList<Object>,跟ArrayList<Integer>的结果是一样的。
		 * 而且ArrayList<int>变成ArrayList<Object>的过程是这样的ArrayList<int>->ArrayList<Integer>->ArrayList<Object>
		 * 比ArrayList<Integer>->ArrayList<Object>多了一步,所以没必要支持ArrayList<int>.
		 * 所以有了ArrayList<Integer>之后就没必要有ArrayList<int>了,反正有了ArrayList<Integer>之后也是可以直接调用add方法直接添加int类型的变量的。
		 * 如果你理解的是ArrayList<int>编译后还是ArrayList<int>,那这就违反了java的泛型擦除规则了,肯定是不支持的.实际上由于历史的原因这个问题非常复杂,一般人搞不懂.
		 * 你就知道java泛型不支持基本类型就行了,java泛型的发展史:Pizza -> Generic Java(GJ) -> Java5 ->Project Valhalla ->Scala
		 * 知乎上的R(RednaxelaFX)大说的是JAVA那帮人偷懒导致的不支持ArrayList<int>这种写法:https://www.zhihu.com/answer/118148143
		 * 
		 * Java所强调的兼容性是"二进制向后兼容性(binary backwards compatibility)"二进制兼容性并不等于源码兼容性,class文件就是java程序的二进制表现
		 * 这句话的意思是说,你用jdk1编译出来的class文件,放在jdk8的JVM里面一样可以运行.但是你基于jdk1写的java代码并不一定可以在jdk8的上面一定编译通过,不是源码兼容的.
		 * 比如说,你最早的源码里面使用了enum这个关键字作为变量,之前编译成class文件了,这个class文件是可以在JVM里面运行的,但你java代码在jdk8上编译不通过了
		 * raw type 这个概念怎么来的？
		 * 还有用java.util.List为例,在原地泛型化后,现在这个类型变成了java.util.ArrayList<E>。但是以前的代码直接用ArrayList,在新版本里必须还能继续用,
		 * 所以就引出了“raw type”的概念,—— 一个类型虽然被泛型化了,但还可以把它当作非泛型化的类型用。
		 * ArrayList  - raw type(原始类型)
		 * ArrayList<E> - open generic type(assuming E is type variable)(开放通用类型(假设E是类型变量))
		 * ArrayList<String> - closed generic type(封闭的通用类型)
		 * ArrayList<?> - unbounded wildcard type(无限通配符类型)
		 * 注意ArrayList作为raw type,与实例化泛型类型ArrayList<Object>,通配符泛型类型ArrayList<?>并不直接等价
		 * 于是对泛型实例化的ArrayList<Integer>和ArrayList<String>来说,非泛型的ArrayList必须是他们的共同超类型(super type)
		 */
		 
		ArrayList<Integer> ilist = new ArrayList<Integer>();
		ArrayList<String> slist = new ArrayList<String>();
		ArrayList list;//raw type(原始类型)
		list = ilist;//子类可以直接赋值给父类(Integer是Object的子类),assigning closed generic type to raw type(将封闭的泛型类型分配给原始类型)
		list = slist;//子类可以直接赋值给父类,ditto(同上)
		
		
		//ArrayList<int> intList = new ArrayList<int>();
		//ArrayList listRaw;//raw type(原始类型)
		//listRaw = intList;//子类可以直接赋值给父类(怎么让Object变成int的父类呢？这很麻烦,并且直接违反了直接的规则int是一个基本类型),assigning closed generic type to raw type(将封闭的泛型类型分配给原始类型)
		//上面如果要想把intList直接复制给listRaw,就需要int是Object的子类,呃,对泛型类型ArrayList<E>来说,泛型参数E被擦除后就变成了Object,那么这里我们要让int,long 与Object赋值兼容,
		//GJ/Java5说:这个问题有点麻烦,赶不及在这个版本发布前完成了,就先放着不管把,于是Java5的泛型就不支持原始类型,而我们不得不写恶心的ArrayList<int>,ArrayList<Long>
		//这就是一个偷懒了的地方
		
		int num = 99;
		Object obj = num;//基本类型可以直接赋值给Object
		Integer numIn = (Integer)obj;//向下转型需要强制转换
		System.out.println("int 和 Object 和 Integer 可以互相转换:" + numIn.intValue());
		
		Object numObj = numIn;//向上转型不需要强制转换
		System.out.println("向上转型不需要强制转换,向下转型需要强制转换,注意这个时候调用的是Integer的toString方法" + numObj);
		
		//Integer numInteger = new Integer(99999);
		//genericType.testParam(numInteger);
		
		/**
		 * num是int类型的,那么这时会调用下面的那一个方法呢？
		 */
		genericType.testParam(num);
	}
	
	public void testParam(Integer num) {
		System.out.println("参数是Integer类型的,传值时可以转int类型的吗?答案是可以的:" + num);
	}
	
	public void testParam(int num) {
		System.out.println("参数是int类型的,传值时可以转Integer类型的吗?答案是可以的:" + num);
	}
}
