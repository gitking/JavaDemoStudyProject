package com.yale.test.net.server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class EchoBioServer {
	private static class ClientThread implements Runnable {
		
		private Socket client = null;
		private Scanner scan = null;
		private PrintStream out = null;
		private boolean flag = true;
		public ClientThread(Socket client) throws IOException {
			this.client = client;
			this.scan = new Scanner(client.getInputStream());
			this.scan.useDelimiter("\n");
			this.out = new PrintStream(client.getOutputStream());
		}
		@Override
		public void run() {
			while (this.flag) {
				if (scan.hasNext()) {
					String val = scan.next().trim();//接收发送的数据
					if ("byebye".equalsIgnoreCase(val)) {
						out.println("ByeByeBye.....");
						this.flag = false;
					} else {
						out.println("【Echo】" + val);
						out.flush();//如果使用PrintStream就不用刷新
					}
				}
			}
			
			try {
				this.scan.close();
				this.out.close();
				this.client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) throws IOException {
		ServerSocket server = new ServerSocket(9999);//设置服务器端的监听端口
		System.out.println("等待客户端连接......");
		//首先需要先接收客户端发送来的信息,而后才可以将信息处理之后发送回客户端
		boolean flag = true;
		while (flag) {//服务器多线程接收客户端发送过来的信息
			Socket client = server.accept();//接收客户端连接
			new Thread(new ClientThread(client)).start();
		}
		server.close();
	}
}
