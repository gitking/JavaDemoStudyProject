package com.yale.test.net.server.headfirstjava;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class DailyAdviceServer {
	String[] adviceList = {"Take smaller bites", "Go for the tight jeans. No they do Not make you look fat.",
			"One word: inappropriate", "Just for today, be honest. Tell your boss what you *really* think",
			"You might want to rethink that haircut."};
	public void go(){
		//服务器应用程序对特定端口创建出ServerSocket,这会让服务器应用程序开始监听来自4242端口的客户端请求。
		try {
			ServerSocket serverSock = new ServerSocket(4242);
			while (true) {
				Socket sock = serverSock.accept();
				PrintWriter writer = new PrintWriter(sock.getOutputStream());
				String advice = getAdvice();
				writer.println(advice);
				writer.close();
				System.out.println(advice);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getAdvice() {
		int random = (int)(Math.random() * adviceList.length);
		return adviceList[random];
	}
	
	public static void main(String[] args) {
		DailyAdviceServer server  = new DailyAdviceServer();
		server.go();
	}
}
