package com.yale.test.io.imooc.serializable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectOutputStreamDemo {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		String objFile = "demo/obj.dat";//序列化到文件obj.dat里面
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(objFile));
		Student stu = new Student("10001", "张三", 20);
		oos.writeObject(stu);
		oos.flush();
		oos.close();
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(objFile));
		Student stu1 = (Student)ois.readObject();
		System.out.println("反序列化出来的结果:" + stu1);
		ois.close();
	}

}
