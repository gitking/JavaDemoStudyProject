package com.yale.test.net.server.socket;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SocketClient {
	/*
	 * 先运行SocketServer类,再运行SocketClient这个类,
	 * 这段程序十分简单,客户端发送消息,服务器端接收到后反馈给客户端,当客户端发送"bye"字符串时,服务器端想客户端发送"close"表示自己即将关闭,
	 * 客户端若再继续操作,就可能会报错。
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		Scanner scanner = new Scanner(System.in);
		SocketWrapper socket = new SocketWrapper(new Socket("localhost", 8888));
		try {
			System.out.println("已经连接上服务器端,现在可以输入数据开始通信了");
			String sendMsg = scanner.nextLine();
			socket.writeLine(sendMsg);//发送消息
			String recivedMsg = socket.readLine();
			while (!"close".equals(recivedMsg)) {
				System.out.println("===【服务器返回】===>>>" + recivedMsg);
				sendMsg = scanner.nextLine();
				socket.writeLine(sendMsg);
				recivedMsg = socket.readLine();
			}
			//下面注释掉的这部分代码,如果放开就会报错,错误原因自然明了
//			socket.writeLine("aaa");
//			socket.writeLine("aaa");
//			socket.writeLine("aaa");
			System.out.println("我是客户端,结束了!");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
}
