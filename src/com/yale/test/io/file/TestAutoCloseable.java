package com.yale.test.io.file;

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
		try (Message msg = new Message()){//必须在try语句里面定义对象
			msg.print();
		} catch (Exception e) {
			 e.printStackTrace();
		}
	}
}
