package com.yale.test.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketReadContext {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		System.out.println("监听IP:" + serverSocket.getInetAddress().getLocalHost().getHostAddress());
		System.out.println("监听端口:" + serverSocket.getLocalPort());
		Socket accpetSocket = serverSocket.accept();
		InputStream in = accpetSocket.getInputStream();
		InputStreamReader ins = new InputStreamReader(in);
		BufferedReader br = new BufferedReader(ins);
		boolean isLoop = true;
		while (isLoop){
			String context = br.readLine();
			if (context != null) {
				System.out.println("接收到客户端" + accpetSocket.getLocalAddress().getLocalHost().getHostAddress());
				System.out.println("内容为:" + context);
			}
		}
	}

}
