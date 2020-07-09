package com.yale.test.java.fanshe.reflection;

/*
 * 
 */
public class ReflectionDemo {
	public static void main(String[] args) throws ClassNotFoundException {
		/*
		 * 由于JVM为每个加载的class创建了对应的Class实例，并在实例中保存了该class的所有信息，包括类名、包名、父类、实现的接口、所有方法、字段等，因此，如果获取了某个Class实例，我们就可以通过这个Class实例获取到该实例对应的class的所有信息。
		 * 这种通过Class实例获取class信息的方法称为反射（Reflection）。
		 * 如何获取一个class的Class实例？有三个方法：
		 * JVM在执行Java程序的时候，并不是一次性把所有用到的class全部加载到内存，而是第一次需要用到class时才加载。例如：
		 * 这就是JVM动态加载class的特性。
		 */
		Class cls = String.class;
		
		String s = "Hello";
		Class clsSec = s.getClass();
		
		Class clsforname = Class.forName("java.lang.String");
		
		System.out.println("因为Class实例在JVM中是唯一的，所以，上述方法获取的Class实例是同一个实例。可以用==比较两个Class实例：" + (cls == clsSec));
		System.out.println("因为Class实例在JVM中是唯一的，所以，上述方法获取的Class实例是同一个实例。可以用==比较两个Class实例：" + (cls == clsforname));
		Integer n = new Integer(123);
		boolean b1 = n instanceof Integer;
		boolean bn = n instanceof Number;
		System.out.println("用instanceof不但匹配指定类型，还匹配指定类型的子类。而用==判断class实例可以精确地判断数据类型，但不能作子类型比较。:" + b1);
		System.out.println("用instanceof不但匹配指定类型，还匹配指定类型的子类。而用==判断class实例可以精确地判断数据类型，但不能作子类型比较。:" + bn);
		
		boolean b3 = n.getClass() == Integer.class;
		//boolean b4 = (n.getClass() == Number.class);
		System.out.println(b3);
		/*
		 * 通常情况下，我们应该用instanceof判断数据类型，因为面向抽象编程的时候，我们不关心具体的子类型。
		 * 只有在需要精确判断一个类型是不是某个class的时候，我们才使用==判断class实例。
		 */
		printClassInfo("".getClass());
        printClassInfo(Runnable.class);
        printClassInfo(java.time.Month.class);
        System.out.println("String[],也是一种Class:而且不同于String.class，它的类名是[Ljava.lang.String");
        printClassInfo(String[].class);
        System.out.println("此外，JVM为每一种基本类型如int也创建了Class，通过int.class访问。");
        printClassInfo(int.class);
	}
	
	
	
	static void printClassInfo(Class cls) {
        System.out.println("Class name: " + cls.getName());
        System.out.println("Simple name: " + cls.getSimpleName());
        if (cls.getPackage() != null) {
            System.out.println("Package name: " + cls.getPackage().getName());
        }
        System.out.println("is interface: " + cls.isInterface());
        System.out.println("is enum: " + cls.isEnum());
        System.out.println("is array: " + cls.isArray());
        System.out.println("is primitive: " + cls.isPrimitive());
    }
}
