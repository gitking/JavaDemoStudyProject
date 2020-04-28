package com.yale.test.net.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoServer {
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(9999);//设置服务器端的监听端口
		System.out.println("等待客户端连接......");
		Socket client = server.accept();//接收客户端连接
		//首先需要先接收客户端发送来的信息,而后才可以将信息处理之后发送回客户端
		Scanner scan = new Scanner(client.getInputStream());
		scan.useDelimiter("\n");//设置分隔符
		PrintWriter out = new PrintWriter(client.getOutputStream());
		boolean flag = true;
		while (flag) {
			if (scan.hasNext()) {
				String val = scan.next().trim();//接收发送的数据
				if ("byebye".equalsIgnoreCase(val)) {
					out.println("ByeByeBye.....");
					flag = false;
				} else {
					out.println("【Echo】" + val);
					out.flush();//如果使用PrintStream就不用刷新
				}
			}
		}
		scan.close();
		out.close();
		client.close();
		server.close();
		
	}
}
