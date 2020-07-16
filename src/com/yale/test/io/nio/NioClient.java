package com.yale.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/*
 * 同步和异步
	同步IO是指，读写IO时代码必须等待数据返回后才继续执行后续代码，它的优点是代码编写简单，缺点是CPU执行效率低。
	而异步IO是指，读写IO时仅发出请求，然后立刻执行后续代码，它的优点是CPU执行效率高，缺点是代码编写复杂。
	Java标准库的包java.io提供了同步IO，而java.nio则是异步IO。上面我们讨论的InputStream、OutputStream、Reader和Writer都是同步IO的抽象类，对应的具体实现类，以文件为例，有FileInputStream、FileOutputStream、FileReader和FileWriter。
	本节我们只讨论Java的同步IO，即输入/输出流的IO模型。
 */
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
