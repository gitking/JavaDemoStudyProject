package com.yale.test.java.fanshe.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import com.sun.istack.internal.NotNull;

/**
 * Annotation这项技术可以说是颠覆性的开发技术
 * annotation这些注解可以定义在类活方法上,而且现在也知道有反射的概念了,可以通过反射取得所定义的annotation信息
 * 在java.lang.reflect.AccessibleObject类和java.lang.Class中都提供了有如下的与annotation有关的方法:
 * 取得全部的annotation: public Annotation[] getAnnotation();
 * 取得指定的annotation: public <T extends Annotation> T getAnnotation(Class<T> annotationClass)
 * jdk8的官方文档:https://docs.oracle.com/javase/8/docs/api/index.html
 * 阿里云 mldn 课时113：反射与Annotation（反射取得Annotation）
 * @author dell
 */
public class AnnotationFanshe {
	public static void main(String[] args) throws NoSuchMethodException, SecurityException {
		Annotation ant[] = Member.class.getAnnotations();
		for (int x=0;x<ant.length; x++) {
			System.out.println("这里只会出现一个annotation,Member类上定义了来个annotation,这其实跟annotation的范围有关系" + ant[x]);
		}
		System.out.println("********************");
		System.out.println("因为注解定义后也是一种class，所有的注解都继承自java.lang.annotation.Annotation，因此，读取注解，需要使用反射API");
		System.out.println("Annotation本身有自己的保存范围,不同的Annotation范围也不同。所以上面只出现了一个annotation信息");
		System.out.println("取得方法上面的annotation");
		
		System.out.println("********************");

		Annotation methodAnt[] = Member.class.getMethod("toString").getAnnotations();
		for (int x=0;x<methodAnt.length; x++) {
			System.out.println("这里只会出现一个annotation,Member类上定义了来个annotation,这其实跟annotation的范围有关系" + methodAnt[x]);
		}
		
		System.out.println("********************");

		System.out.println("反射可以取得结构上定义的Annotation,所以所谓的Annotation的设计是不可能离开反射的");
		
		/**
		 * 想自定义Annotation,那么首先要解决的就是Annotation的作用范围,这些范围就在一个枚举类(RetentionPolicy)定义:
		 * public static final RetentionPolicy CLASS:是在类定义的时候出现的Annotation,CLASS类型的注解仅保存在class文件中，它们不会被加载进JVM；
		 * public static final RetentionPolicy SOURCE:在源代码中出现的Annotation,SOURCE类型的注解在编译期就被丢掉了；
		 * public static final RetentionPolicy RUNTIME:是在类执行的时候允许出现的annotation
		 * 如果@Retention不存在，则该Annotation默认为CLASS。因为通常我们自定义的Annotation都是RUNTIME，RUNTIME类型的注解会被加载进JVM，并且在运行期可以被程序读取。
		 * 所以，务必要加上@Retention(RetentionPolicy.RUNTIME)这个元注解：
		 * 使用@Repeatable这个元注解可以定义Annotation是否可重复。这个注解应用不是特别广泛。
		 * 如何使用注解完全由工具决定。SOURCE类型的注解主要由编译器使用，因此我们一般只使用，不编写。CLASS类型的注解主要由底层工具库使用，
		 * 涉及到class的加载，一般我们很少用到。只有RUNTIME类型的注解不但要使用，还经常需要编写。
		 */

		//取一个类上面某一个具体的Annotation
		MyAnnotation myAnt = Member.class.getDeclaredAnnotation(MyAnnotation.class);
		System.out.println("自定义annotation:" + myAnt.name());
		System.out.println("自定义annotation:" + myAnt.age());
		/**
		 * 你可以在Annotation里面编写许多的属性信息,同时也可以定义许多有意义的Annotation,但是要请千万要记住一点:
		 * Annotation的使用需要特殊环境,不是随便编写的。
		 * Java提供的使用反射API读取Annotation的方法包括：
		 * 判断某个注解是否存在于Class、Field、Method或Constructor：
		    Class.isAnnotationPresent(Class)
		    Field.isAnnotationPresent(Class)
		    Method.isAnnotationPresent(Class)
		    Constructor.isAnnotationPresent(Class)
		    使用反射API读取Annotation：
		    Class.getAnnotation(Class)
		    Field.getAnnotation(Class)
		    Method.getAnnotation(Class)
		    Constructor.getAnnotation(Class)
		 */
		Class mc = Member.class;
		if (mc.isAnnotationPresent(MyAnnotation.class)) {//是否有MyAnnotation注解
			MyAnnotation mn =Member.class.getAnnotation(MyAnnotation.class);
			System.out.println("获取Person定义的@Report注解:" + mn.age());
		}
		
		MyAnnotation mn =Member.class.getAnnotation(MyAnnotation.class);
		if (mn != null) {
			System.out.println("获取Person定义的@Report注解:" + mn.age());
		}
		/*
		 * 读取方法、字段和构造方法的Annotation和Class类似。但要读取方法参数的Annotation就比较麻烦一点，因为方法参数本身可以看成一个数组，
		 * 而每个参数又可以定义多个注解，所以，一次获取方法参数的所有注解就必须用一个二维数组来表示。例如，对于以下方法定义的注解：
		 * 要读取方法参数的注解，我们先用反射获取Method实例，然后读取方法参数的所有注解：
		 */
		Method mt = AnnotationFanshe.class.getMethod("annoTest", String.class);
		Annotation[][] annos = mt.getParameterAnnotations();
		Annotation[] annosOfName = annos[0];
		for (Annotation ano: annosOfName) {
			if (ano instanceof NotNull) {
				NotNull n = (NotNull)ano;
				System.out.println("获取方法参数上面的注解:" + n.getClass());
			}
		}
	}
	
	public void annoTest(@NotNull String name) {
		
	}
}
@SuppressWarnings("serial")
@Deprecated
@MyAnnotation(name="mldn", age = 10)
class Member implements Serializable {
	@Override
	@Deprecated
	public String toString() {
		return super.toString();
	}
}
/**
 * @interface用来定义自己的Annotation
 * @author dell
 * 定义一个注解时，还可以定义配置参数。配置参数可以包括：
	    所有基本类型；
	    String；
	    枚举类型；
	    基本类型、String、Class以及枚举的数组。
	    因为配置参数必须是常量，所以，上述限制保证了注解在定义时就已经确定了每个参数的值。
	 注解的配置参数可以有默认值，缺少某个配置参数时将使用默认值。
	 此外，大部分注解会有一个名为value的配置参数，对此参数赋值，可以只写常量，相当于省略了value参数。
	 如果只写注解，相当于全部使用默认值。
 */
@Retention(RetentionPolicy.RUNTIME)//表示此Annotation在运行时生效
@interface MyAnnotation{
	//注解的参数类似无参数方法，可以用default设定一个默认值（强烈推荐）。最常用的参数应当命名为value。
	/*
	 * 有一些注解可以修饰其他注解，这些注解就称为元注解（meta annotation）。Java标准库已经定义了一些元注解，我们只需要使用元注解，通常不需要自己去编写元注解。
	 * 最常用的元注解是@Target。使用@Target可以定义Annotation能够被应用于源码的哪些位置：
	 * 类或接口：ElementType.TYPE；
	 * 字段：ElementType.FIELD；
	 * 方法：ElementType.METHOD；
	 * 构造方法：ElementType.CONSTRUCTOR；
	 * 方法参数：ElementType.PARAMETER。
	 */
	public String name() default "mldn";//定义属性
	public int age();//这里如果不用default的话,使用MyAnnotation这个自定义的Annotation的时候,就必须给age赋值,不赋值会报错
}

@Target(ElementType.METHOD)//定义注解@MyAnnotationDemo可用在方法上
@interface MyAnnotationDemo{
	//注解的参数类似无参数方法，可以用default设定一个默认值（强烈推荐）。最常用的参数应当命名为value。
	/*
	 * 有一些注解可以修饰其他注解，这些注解就称为元注解（meta annotation）。Java标准库已经定义了一些元注解，我们只需要使用元注解，通常不需要自己去编写元注解。
	 * 最常用的元注解是@Target。使用@Target可以定义Annotation能够被应用于源码的哪些位置：
	 * 类或接口：ElementType.TYPE；
	 * 字段：ElementType.FIELD；
	 * 方法：ElementType.METHOD；
	 * 构造方法：ElementType.CONSTRUCTOR；
	 * 方法参数：ElementType.PARAMETER。
	 */
	public String name() default "mldn";//定义属性
	public int age();//这里如果不用default的话,使用MyAnnotation这个自定义的Annotation的时候,就必须给age赋值,不赋值会报错
}

@Target({
	ElementType.METHOD,
	ElementType.FIELD
})//定义注解@MyAnnotationDe可用在方法或字段上,实际上@Target定义的value是ElementType[]数组，只有一个元素时，可以省略数组的写法。
@interface MyAnnotationDe{
	//注解的参数类似无参数方法，可以用default设定一个默认值（强烈推荐）。最常用的参数应当命名为value。
	/*
	 * 有一些注解可以修饰其他注解，这些注解就称为元注解（meta annotation）。Java标准库已经定义了一些元注解，我们只需要使用元注解，通常不需要自己去编写元注解。
	 * 最常用的元注解是@Target。使用@Target可以定义Annotation能够被应用于源码的哪些位置：
	 * 类或接口：ElementType.TYPE；
	 * 字段：ElementType.FIELD；
	 * 方法：ElementType.METHOD；
	 * 构造方法：ElementType.CONSTRUCTOR；
	 * 方法参数：ElementType.PARAMETER。
	 * 总结:其中，必须设置@Target和@Retention，@Retention一般设置为RUNTIME，因为我们自定义的注解通常要求在运行期读取。一般情况下，不必写@Inherited和@Repeatable。
	 */
	public String name() default "mldn";//定义属性
	public int age();//这里如果不用default的话,使用MyAnnotation这个自定义的Annotation的时候,就必须给age赋值,不赋值会报错
}


@Repeatable(Reports.class)//使用@Repeatable这个元注解可以定义Annotation是否可重复。这个注解应用不是特别广泛。
@Target(ElementType.TYPE)
@interface Report {
    int type() default 0;
    String level() default "info";
    String value() default "";
}

@Target(ElementType.TYPE)
@interface Reports {
    Report[] value();
}

@Report(type=1, level="debug")
@Report(type=2, level="warning")//经过@Repeatable修饰后，在某个类型声明处，就可以添加多个@Report注解：
class HelloAn {
	
}

/*
 * 使用@Inherited定义子类是否可继承父类定义的Annotation。@Inherited仅针对@Target(ElementType.TYPE)类型的annotation有效，
 * 并且仅针对class的继承，对interface的继承无效：
 */
@Inherited
@Target(ElementType.TYPE)
@interface ReportRe {
    int type() default 0;
    String level() default "info";
    String value() default "";
}

@ReportRe(type=1)//在使用的时候，如果一个类用到了@ReportRe：,则它的子类默认也定义了该注解：
class Pser {
	
}

class Student extends Pser {
	
}