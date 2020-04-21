package com.yale.test.io.imooc.serializable;

import java.io.IOException;
import java.io.Serializable;

/**
 * 序列化中如果父类已经实现了序列化接口,子类就不用实现这个接口,子类相当于已经实现了序列化接口
 * @author dell
 *
 */
public class Student implements Serializable{
	private String name;
	private String sex;
	private transient int age;//用transient修改的属性不会进行默认序列化,也可以自己强制序列化
	public Student() {
		
	}
	
	public Student(String name, String sex, int age) {
		super();
		this.name = name;
		this.sex = sex;
		this.age = age;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return "Student [name=" + name + ", sex=" + sex + ", age=" + age + "]";
	}
	
	/**
	 * writeObject这个方法是ArrayList里面的一个方法
	 * arrayList为啥要这么做呢？因为ArrayList里面是一个数组,数组有可能没有放满,
	 * 只需要序列化数组里面有值的元素
	 * @param s
	 * @throws java.io.IOException
	 */
	 private void writeObject(java.io.ObjectOutputStream s) throws java.io.IOException{
        s.defaultWriteObject();//jvm默认的序列化
        s.writeInt(age);//自己完成age的序列化,age是用transient修饰的,默认不序列化
	 }
	 
	 private void readObject(java.io.ObjectInputStream s) throws ClassNotFoundException, IOException {
		 s.defaultReadObject();//jvm默认的反序列化操作
		 this.age = s.readInt();
	 }
}
