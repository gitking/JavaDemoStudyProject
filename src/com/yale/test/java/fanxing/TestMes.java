package com.yale.test.java.fanxing;

public class TestMes {
	public static void main(String[] args) {
		
		IMessage<String> mes = new Message<String>();
		mes.print("泛型实现类依然为泛型");
		
		IMessage<String> mesStr = new Message<String>();
		mes.print("泛型实现类在实现的时候,明确指定了实现的类型");
		
		IMessage<Integer> mesInt = new MessageNumberImpl<Integer>();
		mesInt.print(10);
	}
}
