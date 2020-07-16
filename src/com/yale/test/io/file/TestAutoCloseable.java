package com.yale.test.io.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Message implements AutoCloseable{
	public Message() {
		System.out.println("AutoCloseable接口是JDK1.7才有的接口");
	}
	
	public void print() {
		System.out.println("www.mldn.cn");
	}
	@Override
	public void close() throws Exception {
		System.out.println("【AutoCloseable接口】可以进行自动关闭方法的处理,即使try里面的抛出异常也会自动关闭,相当于放在finally里面的代码");
		throw new Exception("老子就不关");
	}
}
public class TestAutoCloseable {

	public static void main(String[] args) {
		/*
		 * 编译器只看try(resource = ...)中的对象是否实现了java.lang.AutoCloseable接口，如果实现了，就自动加上finally语句并调用close()方法。
		 * InputStream和OutputStream都实现了这个接口，因此，都可以用在try(resource)中。
		 */
		try (Message msg = new Message()){//必须在try语句里面定义对象
			msg.print();
		} catch (Exception e) {
			 e.printStackTrace();
		}
		
		// 同时操作多个AutoCloseable资源时，在try(resource) { ... }语句中可以同时写出多个资源，用;隔开。例如，同时读写两个文件：
		try (InputStream input = new FileInputStream("input.txt");
		     OutputStream output = new FileOutputStream("output.txt"))
		{
		    input.read(); // transferTo的作用是?
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
