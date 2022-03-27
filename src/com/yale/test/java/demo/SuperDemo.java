package com.yale.test.java.demo;

/*
 * 如果一个类不希望任何其他类继承自它，那么可以把这个类本身标记为final。用final修饰的类不能被继承
 * 在OOP的术语中，我们把Student称为超类（super class），父类（parent class），基类（base class），把SuperDemo称为子类（subclass），扩展类（extended class）。
 * 继承有个特点，就是子类无法访问父类的private字段或者private方法。
 */
public  class SuperDemo extends Student{
	public final String strss;//这里不初始化赋值,变量会报错,解决这个报错的办法是在构造方法里面给strss初始化赋值
	public final String name = "用final修饰的字段在初始化后不能被修改";

	public final static String strStatic = "";//用final static修饰的变量,必须直接初始化,否则编译报错
	
	private String testStr;
	public String testStr2 = "调用的是子类的SuperDemo属性:testStr2";
	public String stuName;// 注意：子类自动获得了父类的所有字段，严禁定义与父类重名的字段！ 

	public SuperDemo(){
		/*
    	 * this()和super()只能用在构造函数中,且它必须是第一行语句。
    	 * 每个构造函数可以选择调用super()或this(),但不能同时调用,super()与this()不能兼得。
    	 */
		//super("Student类的stuName");
		this("Student类的stuName");
		/*
		 * 如果调用了this("Student类的stuName");下面再调用
		 * strss = "111";
		 * 就会报错"The final field strss may already have been assigned",
		 * 报错的意思是,final类型的字段strss,可能已经被赋值过了。为什么可能被赋值过了？
		 * 因为你上面已经调用了this("Student类的stuName");这个构造方法了,这个构造方法里面肯定已经给
		 * strss赋值过了,所以strss不能再次赋值了。
		 */
		//strss = "111";//这里不给strss初始化赋值,编译会报错
		this.stuName = "子类SuperDemo的stuName";
	}
	public SuperDemo(String name) {
		super(name);
		strss = "";//这里不给strss初始化赋值,编译一样会报错
		this.stuName = "子类SuperDemo的stuName";
	}
	
	public void superDemo() {
		System.out.println("SuperDemo类:我是子类,我继承了Student类,Student类继承了Person类。");
		//在子类的覆写方法中，如果要调用父类的被覆写的方法，可以通过super来调用。例如：
		super.superDemo();//调父类Student的superDemo方法
	}
	
	public final void method() {
		System.out.println("继承可以允许子类覆写父类的方法。如果一个父类不允许子类对它的某个方法进行覆写，"
				+ "可以把该方法标记为final。用final修饰的方法不能被Override：");
	}

	public String getName() {
		return name;
	}
	
//	public void setName(String name){
//		this.name = name; name是用final修改的,不能重新赋值了
//	}
	
	
	public String getTestStr() {
		return testStr;
	}
	public void setTestStr(String testStr) {
		this.testStr = "调用的是子类的SuperDemo的testStr属性:" + testStr;
	}
	
	public void setServices(String services) {
		this.services = services;
	}
	
	public static void main(String[] args) {
		SuperDemo.testStatic();//静态方法也可以被继承
		SuperDemo sd = new SuperDemo("test");
		sd.superDemo();
		System.out.println();
		System.out.println("子类的对象地址" + sd);
		System.out.println("这里输出的是子类的stuName：-》" + sd.stuName);
		System.out.println("这里获取的是父类的的stuName属性因为调的是父类getStuName的方法,所以要用父类的stuName属性:-->" + sd.getStuName());

		//sd.name="121";这里会报错,因为name,是用final修饰的,并且JVM会针对final修饰的字段做优化,详情参考PerfmaDemo类.
		//并且用final修饰的属性,也不能提供set方法
		System.out.println();
		
		sd.setServices("子类set父类get");
		
		/*
		 * 这里实际上设置的是父类的属性services,然后设置为被子类继承了,所以sd.services
		 * 能取到值,你可以反编译看一下setServices这个方法
		 */
		System.out.println("子类的services属性:-->" + sd.services);
		sd.printServices();
		
		System.out.println();
		System.out.println("***********************************");
		
		/*
		 * Student stu = new SuperDemo();
		 * 这是因为SuperDemo继承自Student，因此，它拥有Student的全部功能。Student类型的变量，如果指向SuperDemo类型的实例，对它进行操作，是没有问题的！
		 * 这种把一个子类类型安全地变为父类类型的赋值，被称为向上转型（upcasting）。
		 * 向上转型实际上是把一个子类型安全地变为更加抽象的父类型：
		 * 注意到继承树是SuperDemo > Student > Object，所以，可以把SuperDemo类型转型为Student，或者更高层次的Object。
		 * 向下转型
		 * 和向上转型相反，如果把一个父类类型强制转型为子类类型，就是向下转型（downcasting）。例如：
		 */
		Student stu = new SuperDemo();
		stu.superDemo();
		
		stu.setTestStr("测试");
		System.out.println("这里调用的是子类的getTestStr,所以打印的是子类的testStr属性->>:" + stu.getTestStr());//这里调用的子类的
		/*
		 * 注意如果通过父类.属性调用,那么调用的就是父类的属性,
		 * 如果通过get方法调用那调用的就是子类的属性
		 */
		System.out.println("……父类……->" + stu.testStr2);
		System.out.println();
		System.out.println("子类的对象地址" + stu);
		System.out.println("这里输出的是父类的：-》" + stu.stuName);
		System.out.println("这里获取的是谁的属性:因为调用的是父类的getStuName方法->" + stu.getStuName());
	}
}
