package com.yale.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class NioClient {
	public void start(String nickName) throws IOException {
		//连接服务器端
		SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8000));
		//新开线程,专门负责来接收服务器端的响应数据
		Selector selector = Selector.open();
		socketChannel.configureBlocking(false);//设置为非阻塞模式
		//将socketChannel注册到selector上面,监听selector的可读事件
		socketChannel.register(selector, SelectionKey.OP_READ);
		new Thread(new NioClientHandler(selector)).start();//新开线程
		
		Scanner scanner = new Scanner(System.in);
		//向服务器端发送数据
		while (scanner.hasNextLine()) {
			String request = scanner.nextLine();
			if (request != null && request.length() > 0) {
				socketChannel.write(Charset.forName("UTF-8").encode(nickName + ":" +request));
				
			}
		}
	}
	
	public static void main(String[] args) throws IOException {
		//new NioClient().start();
	}
}
