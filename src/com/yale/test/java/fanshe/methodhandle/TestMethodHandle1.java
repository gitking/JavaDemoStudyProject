package com.yale.test.java.fanshe.methodhandle;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

/**
 * 相比之下JDK 7里新的MethodHandle则更有潜力，在其功能完全实现后能达到比普通反射调用方法更高的性能。在使用MethodHandle来做反射调用时，
 * MethodHandle.invoke()的形式参数与返回值类型都是准确的，所以只需要在链接方法的时候才需要检查类型的匹配性，而不必在每次调用时都检查。
 * 而且MethodHandle是不可变值，在创建后其内部状态就不会再改变了；JVM可以利用这个知识而放心的对它做激进优化，
 * 例如将实际的调用目标内联到做反射调用的一侧。
 * 到本来Java的安全机制使得不同类之间不是任意信息都可见，但Sun的JDK里开了个口，有一个标记类专门用于开后门：MagicAccessorImpl 
 * https://www.iteye.com/blog/rednaxelafx-477934
 * https://www.iteye.com/blog/rednaxelafx-548536
 * https://www.iteye.com/blog/rednaxelafx-464071
 * @author dell
 */
public class TestMethodHandle1 {

	public static void main(String[] args) {
		try {
			/**
			 * 调用MethodHandle的速度接近调用接口方法的速度。
			 * MethodHandle对许多JVM的内部实现来说并不是一个全新的概念。要实现JVM，在内部总会保留一些指向方法的指针。J
			 * DK 7只是把它（和其它许多JVM里原本就支持的概念）具体化为Java类型暴露给Java代码用而已；这就是所谓的“reification”。
			 * 流程是：
			 * 0、调用MethodHandles.lookup()方法，遍历调用栈检查访问权限，然后得到一个MethodHandles.Lookup实例；该对象用于确认创建MethodHandle的实例的类对目标方法的访问权限是否满足要求，并提供搜索目标方法的逻辑；
			 * 1、指定目标方法的“方法类型”，得到一个MethodType实例；
			 * 2、通过MethodHandles.lookup()静态方法得到一个类型为MethodHandles.Lookup的工厂，然后靠它搜索指定的类型、指定的名字、指定的方法类型的方法，得到一个MethodHandle实例；
			 * 3、调用MethodHandle上的invoke方法。
			 * 
			 * 其中，第1步中调用的MethodType.make()方法接收的参数是一组类型，第一个参数是返回类型，后面依次是各个参数的类型。
			 * 上例中MethodType.make(void.class)得到的就是一个返回类型为void，参数列表为空的方法类型。如果熟悉Java字节码的话，
			 * 这个方法类型的描述符就是()V。关于方法描述符的格式，可以参考JVM规范第二版4.3.3小节(https://docs.oracle.com/javase/specs/#7035)。
			 * MethodType的实例只代表所有返回值与参数类型匹配的一类方法的方法类型，自身没有名字；在检查某个方法是否与某个MethodType匹配时只考虑结构，
			 * 可以算是一种特殊的structural-typing。
			 * 第2步看起来跟普通的反射很像，但通过反射得到的代表方法的对象是java.lang.reflect.Method的实例，它含有许多跟“执行”没有直接关系的信息，比较笨重；通过Method对象调用方法只是正常方法调用的模拟，所有参数会被包装为一个数组，开销较大。而MethodHandle则是个非常轻量的对象，主要目的就是用来引用方法并调用；通过它去调用方法不会导致参数被包装，原始类型的参数也不会被自动装箱。
			 * MethodHandles.Lookup上有三个find方法，包括findStatic、findVirtual、findSpecial，分别对应invokestatic、invokevirtual/invokeinterface、invokespecial会对应的调用逻辑。注意到findVirtual方法所返回的MethodHandle的方法类型会包含一个显式的“this”参数作为第一个参数；调用这样的MethodHandle要显式传入“receiver”。这个看起来就跟.NET的开放委托相似，可以参考我之前的一帖。由于JDK 7的MethodHandle支持currying，可以把receiver保存在MethodHandle里，所以也可以创建出类似.NET的闭合委托的MethodHandle实例。
			 * MethodHandles.Lookup上还有一组方法可以从通过反射API得到的Constructor、Field或Method对象创建出对应的MethodHandle
			 * 第3步调用的MethodHandle.invoke()看似是一个虚方法，实际上并不是MethodHandle上真的存在的方法，而只是标记用的虚构出来的方法。上例中第13行对应的Java字节码是：invokevirtual java/dyn/MethodHandle.invoke:()V 
			 * 也就是假装MethodHandle上有一个描述符为()V且名为invoke的虚方法，通过invokevirtual指令去调用它。
			 * Java编译器为它做特殊处理：返回值类型如同泛型参数在<>内指定，不写的话默认为返回Object类型；参数列表的类型则由Java编译器根据实际参数的表达式推断出来。与正常的泛型方法不同，MethodHandle.invoke指定返回值类型可以使用void和所有原始类型，不必像使用泛型方法时需要把原始类型写为对应的包装类型。
			 * MethodHandle的方法类型不是Java语言的静态类型系统的一部分。虽然它的实例在运行时带有方法类型信息（MethodType），
			 * 但在编译时Java编译器却不知道这一点。所以在编译时，调用invoke时传入任意个数、任意类型的参数都可以通过编译；但在运行时要成功调用，
			 * 由Java编译器推断出来的返回值类型与参数列表必须与运行时MethodHandle实际的方法类型一致，
			 * 否则会抛出WrongMethodTypeException(http://download.java.net/jdk7/docs/api/java/dyn/WrongMethodTypeException.html)。
			 * John Rose(http://blogs.sun.com/jrose)把MethodHandle.invoke的多态性称为“签名多态性（signature polymorphism）(http://blogs.sun.com/jrose/entry/method_handles_in_a_nutshell)”。
			 * 用户可以自行继承java.dyn.JavaMethodHandle(http://download.java.net/jdk7/docs/api/java/dyn/JavaMethodHandle.html)来创建自定义的MethodHandle子类，可以添加域或方法等，并可以指定该类型看作MethodHandle时的“入口点”——实际指向的方法。
			 * 许多JVM实现在JIT编译的时候会做激进的优化，包括常量传播、内联、逃逸分析、无用代码削除等许多。JDK 7的MethodHandle的一个好处是它就像它所指向的目标方法的替身一样，JVM原本可以做的优化对MethodHandle也一样支持，特别是有需要的时候可以把目标方法内联到调用处。相比之下，通过反射去调用方法则无法被JVM有效的优化。
			 */
			MethodType type = MethodType.methodType(void.class);//指明方法的返回类型为void
			Lookup lookUp = MethodHandles.lookup();
			MethodHandle methodHandle = lookUp.findStatic(TestMethodHandle1.class, "hello", type);
			methodHandle.invoke();//第3步调用的MethodHandle.invoke()看似是一个虚方法，实际上并不是MethodHandle上真的存在的方法，而只是标记用的虚构出来的方法。上例中第13行对应的Java字节码是：
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		/**
		 * 基本上跟第一组例子一样，只是让hello()多了个参数而已。留意一下创建MethodType实例的代码如何对应的改变。
		 * 第15行对应的Java字节码是：invokevirtual java/dyn/MethodHandle.invoke:(Ljava/lang/String;)V  
		 * 留意Java编译器是如何根据调用invoke时传入的参数的静态类型（编译时类型）来决定invoke的方法描述符。(Ljava/lang/String;)V的意思是返回值类型为void，参数列表有一个参数，类型为java.lang.String。
	     * 如果把代码稍微修改，使MethodHandle的方法类型与Java编译器推断的调用类型不相符的话：
		 */
		MethodType type1 = MethodType.methodType(void.class, String.class);//指明方法的返回类型为void,方法的参数类型为String并且方法只有一个参数
		try {
			MethodHandle methodHandle = MethodHandles.lookup().findStatic(TestMethodHandle1.class, "hello", type1);
			methodHandle.invoke("MethodHandle");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		/**
		 * 这演示了Java编译器将invoke的方法类型推断为(Ljava/lang/String;)V，而被调用的MethodHandle实例实际的方法类型却是(Ljava/lang/Object;)V，JVM便认为这个调用
		 * 不匹配并拒绝执行。关键点是：调用invoke时，参数表达式的静态类型（编译时类型）必须与MethodHandle的方法类型中对于位置的参数类型“准确一致”；
		 * 虽然String类型的引用可以隐式转换为Object类型的，但不满足“准确一致”的要求。
		 * 要想让修改过的TestMethodHandle2再次正确运行，可以把第15行改为：method.<void>invoke((Object)args[0]);，也就是加个类型转换，
		 * 使Java编译器推断出来的方法描述符为(Ljava/lang/Object;)V。或者也可以加一个适配器：
		 * 
		 * 这里演示了MethodHandle的可组装性：通过给实际调用目标装一个转换参数类型的适配器，方法调用就又可以成功了。
		 */
		MethodType type2 = MethodType.methodType(void.class, Object.class);//指明方法的返回类型为void,方法的参数类型为Object并且方法只有一个参数
		MethodType type3 = MethodType.methodType(void.class, String.class);//指明方法的返回类型为void,方法的参数类型为String并且方法只有一个参数
		try {
			MethodHandle methodHandle = MethodHandles.lookup().findStatic(TestMethodHandle1.class, "helloObj", type2);
			MethodHandle adaptedMethod = MethodHandles.explicitCastArguments(methodHandle, type3);
			adaptedMethod.invoke("adaptedMethod");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static void hello() {
		System.out.println("Hello world!");
	}
	
	private static void hello(String name) {
		System.out.printf("Hello %s!\n", name);
	}
	
	private static void helloObj(String number) {
		System.out.printf("Hello %s!\n", number);
	}
}
