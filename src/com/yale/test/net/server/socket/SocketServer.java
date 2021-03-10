package com.yale.test.net.server.socket;

import java.io.IOException;
import java.net.ServerSocket;

/*
 * 这个SocketServer建立了一个8888端口来对外提供服务,接下来就写一个SocketClient程序来调用服务器的8888端口.
 */
public class SocketServer {
	public static void main(String[] args) throws IOException {
		ServerSocket serSocket = new ServerSocket(8888);
		System.out.println("端口已经打开为8888,开始准备接收收据");
		SocketWrapper socket = null;
		try {
			socket = new SocketWrapper(serSocket.accept());
			String line = socket.readLine();
			while (!"bye".equals(line)) {
				System.out.println("客户端传来数据:" + line);
				socket.writeLine("我接收到你的数据:" + line);
				line = socket.readLine();
			}
			socket.writeLine("close");
		} finally {
			if (socket != null) {
				socket.close();
			}
		}
	}
}
