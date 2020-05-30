package com.yale.test.lambda;

/**
 * Lambda是从JDK1.8推出的重要新特性,函数式编程。
 * Lambda实际上简化了方法引用
 * @author dell
 */
public class Test {
	public static void main(String[] args) {
		IMessage msgName = new IMessage(){
			@Override
			public void print() {
				System.out.println("这个是一个接口的匿名实现类,要写这么多行代码,就为了打印这一句话。Java面向对象的缺点就是结构太完整了,必须写这么整齐");
			}
		};
		msgName.print();
		
		//这行代码相当于实现了一个匿名接口,不过比匿名接口少写了很多代码。函数式编程虽然少写代码,但是一般人不容易看懂
		//Scala是行业内基于java做了一套自己的函数式编程语言
		IMessage msg = () -> System.out.println("Lambda表达式,是一种函数式编程,使用Lambda表达式时,接口IMessage只能有一个方法");
		msg.print();
		
		IMessage msgSec = () -> {
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
				System.out.println("Lambda表达式,是一种函数式编程,当方法有多行代码时,这样写");
			};
		msgSec.print();
		
		IMath math = (m1,m2) -> m1 + m2;//只有一行代码,可以不写return
		System.out.println("Lambda表达式,当方法只有返回值时,可以这么写,(这里面实际上参数)->" + math.add(10, 20));
		
		//方法引用,从最初开始只要是进行引用基本上都是针对引用类型完成的,也就是说只有数组、类、接口、具备引用的操作。但是现在开始追加了方法引用的概念。
		//实际上引用的本质就是别名。所以方法的引用也是别名的使用。而方法引用的类型可以有四种形式:
		//1、引用静态方法:类名称::static方法名称
		//2、引用某个对象的方法:实例化对象::普通方法
		//3、引用某个特定类的方法:类名称::普通方法
		//4、引用构造方法:类名称::new
		/**
		 * Lambda实际上简化了方法引用,但是Lambda核心在于函数式接口,而函数式接口的核心在于只有一个方法,如果你细心去观察的话会发现,实际上
		 * 在函数式编程里面只需要有四类接口(java.util.function):
		 * 1、功能型函数式接口(方法无参无返回值):
		 * 2、供给型函数式接口(方法无参有返回值):
		 * 3、消费型函数式接口(方法有参数没返回值):public interface Consumer<T>{public void accept(T t)}
		 * 4、断言型函数式接口(方法有参数又返回值):public inteface Predicate<T> {boolean test(T t)}
		 */
		IUtil<Integer,String> util = String :: valueOf;//引用静态方法,这行代码的意思是,接口IUtil的方法changeType引用String类里面的valueOf方法
		String result = util.changeType(100);//相当于String.valueof(100)
		System.out.println("方法引用:" + result + "，长度:" + result.length());
		
		IUtilSec utilSec = String :: valueOf;
		String resultSec = utilSec.changeType(100);
		System.out.println("方法引用:" + resultSec.length());
		
		IUtilThrid utilThrid = "hello" :: toUpperCase;//引用某一个对象中的方法
		System.out.println(utilThrid.changeWord());

		IUtilFour<Person,String,Integer> utilFour = Person :: new;//引用构造方法
		System.out.println("构造方法引用:" +  utilFour.createPer("王亚乐", 12));
		
		IUtilSix<Integer, String,String> utilSix = String :: compareTo;//引用类中的普通方法
		System.out.println(utilSix.compare("H", "h"));
		
		IUtilSec2<Integer, String> iu = String :: compareTo;//引用类中的普通方法
		System.out.println(iu.比较("H", "h"));//注意方法是中文的,阿里云 课时24 方法引用 里面看到的
		
	}
}
