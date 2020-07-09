package com.yale.test.java.fanshe;

import java.lang.reflect.Field;

class FieldTest {
	public String name;
}

class FieldTe extends FieldTest {
	public String school;
}
public class FiledDemo {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, InstantiationException {
		Class<?> cls = Class.forName("com.yale.test.java.fanshe.FieldTe");
		{//普通代码块
			Field[] fieldArr = cls.getFields();
			for (int i=0;i<fieldArr.length;i++) {
				System.out.println("getFields方法只能得到public的属性,包括父类的公开属性:" + fieldArr[i]);
			}
		}
		System.out.println("--------------------------------------------");
		{//普通代码块
			Field[] fieldArr = cls.getDeclaredFields();
			for (int i=0;i<fieldArr.length;i++) {
				System.out.println("getDeclaredFields只能得到本类声明的属性,private的属性也能得到:" + fieldArr[i]);
			}
		}
		
		System.out.println("--------------------------------------------");
		Field scField = cls.getDeclaredField("school");//得到指定name的属性
		System.out.println("得到属性的类型:" + scField.getType());
		System.out.println("得到属性的类型:" + scField.getType().getName());
		System.out.println("得到属性的类型:" + scField.getType().getSimpleName());

		Object obj = cls.newInstance();//通过反射实例化FieldTe对象
		/**
		 * 这里只能给公开的属性设置值,给private属性设置值会报错
		 * Exception in thread "main" java.lang.IllegalAccessException: 
		 * Class com.yale.test.java.fanshe.FiledDemo can not access a member of class 
		 * com.yale.test.java.fanshe.FieldTe with modifiers "private"
		 * 但是使用scField.setAccessible(true);
		 * 此外，setAccessible(true)可能会失败。如果JVM运行期存在SecurityManager，那么它会根据规则进行检查，有可能阻止setAccessible(true)。
		 * 例如，某个SecurityManager可能不允许对java和javax开头的package的类调用setAccessible(true)，这样可以保证JVM核心库的安全。
		 */
		scField.setAccessible(true);//设置为true就可以访问private属性
		scField.set(obj, "四中");//通过反射给FieldTe对象的属性设置值
		System.out.println("通过反射取的设置的属性:->" +  scField.get(obj));
	}
}
