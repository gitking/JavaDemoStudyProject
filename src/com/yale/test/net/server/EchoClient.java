package com.yale.test.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class EchoClient {
	private static final BufferedReader KEYBOARD_INPUT = new BufferedReader(new InputStreamReader(System.in));
	
	public static void main(String[] args) throws UnknownHostException, IOException {
		Socket client = new Socket("localhost", 9999);//定义服务端的链接信息
		//现在的客户端要输入与输出的操作支持,所以依然要准备出Scanner与PrintWriter
		Scanner scan = new Scanner(client.getInputStream());
		scan.useDelimiter("\n");
		PrintWriter out = new PrintWriter(client.getOutputStream());
		boolean flag = true;
		while (flag) {
			String input = getString("请输入要发送的内容");
			out.println(input);//加换行
			out.flush();//如果使用PrintStream就不用刷新
			if (scan.hasNext()) {
				System.out.println(scan.next());
			}
			if ("byebye".equalsIgnoreCase(input)) {
				flag = false;
			}
		}
		scan.close();
		out.close();
		client.close();
	}
	
	public static String getString(String prompt) throws IOException {
		System.out.println(prompt);
		String str = KEYBOARD_INPUT.readLine();
		return str;
	}
}
