package com.yale.test.other;

import java.util.HashMap;
import java.util.HashSet;

public class Stu {
	private String name;
	private String age;
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
	
	public static void main(String[] args){
		Stu stu = new Stu();
		stu.setAge("12");
		HashMap<String,Stu> sf = new HashMap<String,Stu>();
		sf.put("yy", stu);
		stu = new Stu();
		stu.setAge("20");
		
		Stu sd = sf.get("yy");
		System.out.println(sd.getAge());
		
		//其实HashSet内部实现就是一个HashMap,只用了HashMap的键值
		HashSet sdf = new HashSet();
		sdf.add(1);
	}
}
