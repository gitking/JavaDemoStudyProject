package com.yale.test.java.demo;

/**
 * 不写public，也能正确编译，但是这个类将无法从命令行执行。
 * 基本数据类型是CPU可以直接进行运算的类型。
 * Java定义的这些基本数据类型有什么区别呢？要了解这些区别，我们就必须简单了解一下计算机内存的基本结构。
 * 计算机内存的最小存储单元是字节（byte），一个字节就是一个8位二进制数，即8个bit。它的二进制表示范围从00000000~11111111，换算成十进制是0~255，换算成十六进制是00~ff。
 * 内存单元从0开始编号，称为内存地址。每个内存单元可以看作一间房间，内存地址就是门牌号。
 * 一个字节是1byte，1024字节是1K，1024K是1M，1024M是1G，1024G是1T。一个拥有4T内存的计算机的字节数量就是：
 * 对于整型类型，Java只定义了带符号的整型，因此，最高位的bit表示符号位（0表示正数，1表示负数）。各种整型能表示的最大范围如下：
    byte：-128 ~ 127
    short: -32768 ~ 32767
    int: -2147483648 ~ 2147483647
    long: -9223372036854775808 ~ 9223372036854775807
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1255883729079552
 * @author dell
 */
public class Person {
	public static void main(String[] args) {
		Person ps = new Person();
		Student stu = new Student("Person类");
		
		ps.testParam(stu);//传子类
		
		Person tempP = stu;//先将子类对象赋值给父类对象
	
		stu.testParam01((Student)tempP);
		
		Student stuTemp = (Student)tempP;//这行代码不会报错,因为tempP实际上就是一个Student对象,所以这里将父类强制转换为子类不会报错
		stuTemp.test();
		/**
		 * 向下转型
		 * 和向上转型相反，如果把一个父类类型强制转型为子类类型，就是向下转型（downcasting）。例如：
		 * Person p1 = new Student(); // upcasting, ok
		 * Person p2 = new Person();
		 * Student s1 = (Student) p1; // ok
		 * Student s2 = (Student) p2; // runtime error! ClassCastException!
		 * 如果测试上面的代码，可以发现：
		 * Person类型p1实际指向Student实例，Person类型变量p2实际指向Person实例。在向下转型的时候，把p1转型为Student会成功，因为p1确实指向Student实例，
		 * 把p2转型为Student会失败，因为p2的实际类型是Person，不能把父类变为子类，因为子类功能比父类多，多的功能无法凭空变出来。
		 * 因此，向下转型很可能会失败。失败的时候，Java虚拟机会报ClassCastException。
		 * 为了避免向下转型出错，Java提供了instanceof操作符，可以先判断一个实例究竟是不是某种类型：
		 * instanceof实际上判断一个变量所指向的实例是否是指定类型，或者这个类型的子类。如果一个引用变量为null，那么对任何instanceof的判断都为false。
		 * 
		 * 向下转型不安全,可以使用instanceof判断一个实例究竟是不是某种类型
		 * 如果一个引用变量为null，那么对任何instanceof的判断都为false。
		 */
		if (ps instanceof Student) {
			System.out.println("ps这个对象实际上是一个Student的实例");
		} else {
			System.out.println("ps这个对象不是Student的实例,不能向下转型,代码会报错");
		}
		
		
		Object obj = "字符串对象";
		if (obj instanceof String) {
		    String s = (String) obj;
		    System.out.println(s.toUpperCase());
		}
		/*
		 * 上面的代码
		 * 从Java 14开始，判断instanceof后，可以直接转型为指定变量，避免再次强制转型。例如，对于以下代码：
		 * Object obj = "hello";
        if (obj instanceof String s) {//用变量s接收
            // 可以直接使用变量s:
            System.out.println(s.toUpperCase());
        }
        使用instanceof variable这种判断并转型为指定类型变量的语法时，必须打开编译器开关--source 14和--enable-preview。
		 */
		
		Student temp = (Student)ps;//向下转型,这行代码会报错,因为ps这个对象实际上一个父类对象,所以不能将一个实际的父类对象强制转换为子类对象
		ps.testParam01(temp);//这里不强制会报错,但是强制调用testParam01方法有风险,如果testParam01方法内部调用了,子类特有的方法,代码运行就会报错
		
		//继承是is关系，组合是has关系。
	}
	
	
	public void testParam(Person per) {
		System.out.println("参数如果是父类,则实际传参时即可以传父类也可以传子类");
	}
	
	public void testParam01(Student stu) {
		System.out.println("参数如果是子类,则实际传参时不能传父类对象过来,编译会报错");
		System.out.println("但是可以把父类强制转为子类后,再传进来.");
		//stu.test();
	}
	
	public void superDemo() {
		System.out.println("Person类:顶层的父类,");
	}
	
	//静态方法也经常用于辅助方法
    public static void testStatic() {
    	System.out.println("静态方法也经常用于辅助方法");
    }
}
