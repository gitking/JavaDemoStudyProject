package com.yale.test.java.fanshe.classloader;

/**
 * https://mp.weixin.qq.com/s?__biz=MzU2NzAzMjQyOA==&mid=2247483988&idx=1&sn=5b34b96a5312f2687dd28bce39d542f4&chksm=fca22d57cbd5a441db81097d265556c18cdb386e1ea456e607e78f9a629a4ce9033947d19721&scene=21#wechat_redirect
 * 本题考查的知识点为【类加载的顺序】。一个类从被加载至 JVM 到卸载出内存的整个生命周期为：
 * 加载->连接(验证->准备->解析)->初始化->使用->卸载
 * 加载:查找并加载类文件的二进制数据
 * 链接：将已经读入内存的类的二进制数据合并到 JVM 的运行时环境中去，包含如下几个步骤：
 * 	  验证：确保被加载类的正确性
 *    准备：为类的静态变量分配内存，赋默认值；例如：public static int a = 1; 在准备阶段对静态变量 a 赋默认值 0
 *    解析：把常量池中的符号引用转换成直接引用
 * 初始化<clinit>：为类的静态变量赋初始值；例如：public static int a = 1;这个时候才对静态变量 a 赋初始值 1
 * 我们可以从 Java 类加载的这个过程中看到，类的静态变量在类加载时就已经被加载到内存中并完成赋值了！
 * 对于ClassLoaderDemo2 来说：
 * 首先，在链接的准备阶段，JVM 会为类的静态变量分配内存，并赋默认值，这里面我们也可以使用更加专业的计算机词汇——“缺省值”来形容，即：
 * cld = null；
 * a = 0;
 * b = 0;
 * 接着，在类的初始化阶段，JVM 会为这些静态变量真正地赋初始值。
 * private static ClassLoaderDemo2 cld = new ClassLoaderDemo2();
 * 对静态变量 cld 赋初始值时会回调构造器，构造器中执行 a++ 与 b++，使得静态变量 a 与 b 的结果均为 1 。
 * 对 cld 这个静态变量赋值完毕后，接下来代码会继续执行，对 a 和 b 这两个静态变量赋初始值，继而又将 a 变为了 0，而 b 则没有初始值，所以其结果仍然为 1。
 * 综上所示，ClassLoaderDemo2的输出结果为：
 * a为:0,b为:1
 * 
 * 当 JVM 遇到一条 new 指令时，首先会去检查该指令的参数是否能在常量池中定位到一个类的符号引用（Symbolic Reference），并检查这个符号引用代表的类是否已经被加载，解析，初始化过。如果该类是第一次被使用，那么就会执行类的加载过程。
 * 注：符号引用是指，一个类中引入了其他的类，可是 JVM 并不知道引入其他类在什么位置，所以就用唯一的符号来代替，等到类加载器去解析时，就会使用符号引用找到引用类的具体地址，这个地址就是直接引用
 * 谈到了类加载，就不得不提类加载器（ClassLoader）。以 HotSpot VM 举例，从 JDK 9 开始，其自带的类加载器如下：
 * BootstrapClassLoader、PlatformClassLoader、AppClassLoader
 * 而 JDK 8 虚拟机自带的加载器为：
 * BootstrapClassLoader、ExtensionClassLoader、AppClassLoader
 * 除了虚拟机自带的类加载器以外，用户也可以自定义类加载器（UserClassLoader）。这些类加载器的加载顺序具有一定的层级关系：
 * BootstrapClassLoader-》PlatformClassLoader-》AppClassLoader-》UserClassLoader
 * JVM 中的 ClassLoader 会按照这样的层级关系，采用一种叫做双亲委派模型的方式去加载一个类：
 * 那么什么是双亲委派模型呢？
 * 双亲委托模型就是：如果一个类加载器（ClassLoader）收到了类加载的请求，它首先不会自己去尝试加载这个类，而是把这个请求委托给父类加载器去完成，每一个层次的类加载器都是如此，因此所有的加载请求最终都应该传送到顶层的启动类加载器（BootstrapClassLoader）中，只有当父类加载器反馈自己无法完成这个加载请求（它的搜索范围中没有找到所需要加载的类）时，子加载器才会尝试自己去加载。
 * 使用双亲委托机制的好处是：能够有效确保一个类的全局唯一性，当程序中出现多个限定名相同的类时，类加载器在执行加载时，始终只会加载其中的某一个类。
 * 为对象分配空间的任务等同于把一块确定大小的内存从 JVM 堆中划分出来，目前常用的有两种方式（根据使用的垃圾收集器的不同而使用不同的分配机制）：
 * Bump the Pointer（指针碰撞）:所谓的指针碰撞是指：假设 JVM 堆内存是绝对规整的，所有用过的内存都放在一边，空闲的内存放在另一半，中间有一个指针指向分界点，那新的对象分配的内存就是把那个指针向空闲空间挪动一段与对象大小相等的距离。
 * Free List（空闲列表）:而如果 JVM 堆内存并不是规整的，即：已用内存空间与空闲内存相互交错，JVM 会维护一个空闲列表，记录哪些内存块是可用的，在为该对象分配空间时，JVM 会从空闲列表中找到一块足够大的空间划分给对象使用。
 * 对象在内存中存储的布局（以 HotSpot 虚拟机为例）分为：对象头，实例数据以及对齐填充。
 * 对象头:对象头包含两个部分：Mark Word：存储对象自身的运行数据，如：Hash Code,GC 分代年龄，锁状态标志等等.类型指针：对象指向它的类的元数据的指针.
 * 实例数据:实例数据是真正存放对象实例的地方
 * 对齐填充:这部分不一定存在，也没有什么特别含义，仅仅是占位符。因为 HotSpot 要求对象起始地址都是 8 字节的整数倍，如果不是就对齐
 * JVM 会为所有实例数据赋缺省值，例如整型的缺省值为 0，引用类型的缺省值为 null 等等。
 * 并且，JVM 会为对象头进行必要的设置，例如这个对象是哪个类的实例，如何才能找到类的元数据信息，对象的 Hash Code,对象的 GC 分带年龄等等，这些信息都存放在对象的对象头中。
 * 调用对象的实例化方法 <init>
 * 在 JVM 完善好对象内存布局的信息后，会调用对象的 <init> 方法，根据传入的属性值为对象的变量赋值。
 * 我们在上面介绍了类加载的过程（加载 -> 链接 -> 初始化），在初始化这一步骤，JVM 为类的静态变量进行赋值，并且执行了静态代码块。实际上这一步骤是由 JVM 生成的 <clinit> 方法完成的。
 * <clinit> 的执行的顺序为：
 * 1.父类静态变量初始化
   2.父类静态代码块
   3.子类静态变量初始化
   4.子类静态代码块
 * 而我们在创建实例 new 一个对象时，会调用该对象类构造器进行初始化，这里面就会执行 <init> 方法。<init>的执行顺序为：
 *  1.父类变量初始化
    2.父类普通代码块
    3.父类构造函数
    4.子类变量初始化
    5.子类普通代码块
    6.子类构造函数
 * 关于<init> 方法：1.有多少个构造器就会有多少个 <init> 方法。2.<init> 具体执行的内容包括非静态变量的赋值操作，非静态代码块的执行，与构造器的代码。3.非静态代码赋值操作与非静态代码块的执行是从上至下顺序执行，构造器在最后执行.
 * 关于 <clinit> 与 <init> 方法的差异：
 * <clinit>方法在类加载的初始化步骤执行，<init> 在进行实例初始化时执行
 * <clinit> 执行静态变量的赋值与执行静态代码块，而<init> 执行非静态变量的赋值与执行非静态代码块以及构造器
 * 
 * https://www.heapdump.cn/article/2571333 https://www.heapdump.cn/article/2571333
 * @author issuser
 */
public class ClassLoaderDemo2 {
	private static ClassLoaderDemo2 cld = new ClassLoaderDemo2();
	private static int a = 0;
	private static int b;
	private ClassLoaderDemo2() {
		a++;
		b++;
	}
	
	public static ClassLoaderDemo2 getInstance() {
		return cld;
	}
	
	public int getA() {
		return a;
	}
	
	public int getB() {
		return b;
	}
	
	public static void main(String[] args) {
		ClassLoaderDemo2 cld = ClassLoaderDemo2.getInstance();
		System.out.println("类加载原理，A的值为:" + cld.getA());
		System.out.println("类加载原理，B的值为:" + cld.getB());
	}
}
