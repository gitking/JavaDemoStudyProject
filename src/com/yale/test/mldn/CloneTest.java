package com.yale.test.mldn;

public class CloneTest {
	public static void main(String[] args) throws CloneNotSupportedException {
		PersonTest pt = new PersonTest("张三", "10");
		PersonTest pb = (PersonTest)pt.clone();
		pb.setAge("30");
		System.out.println(pt);
		System.out.println(pb);
	}
}

/**
 * 如果对象需要实现克隆,必须实现Cloneable接口,
 * 有趣的是Cloneable接口是一个空接口,该接口没有任何抽象方法.
 * 该接口Cloneable只是一个标识,代表一种能力
 * @author dell
 */
class PersonTest implements Cloneable {
	private String name;
	private String age;
	
	public PersonTest(String name, String age) {
		super();
		this.name = name;
		this.age = age;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	
	/**
	 * protected的访问范围:
	 * PersonTest是Object的子类,但是PersonTest跟Object不在同一个包里面.
	 * 所以用权限修饰的clone方法只能在PersonTest内部调用
	 * 但是子类重写父类的方法,可以扩大方法的访问权限,所以下面的代码可以改成public的
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		//该方法什么都不用改,父类复制克隆
		return super.clone();
	}

	@Override
	public String toString() {
		return "PersonTest [name=" + name + ", age=" + age + "]";
	}
}