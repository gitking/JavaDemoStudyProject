package com.yale.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器
 * NIO Non Blocking io非阻塞IO JDK1.4有的
 * @author dell
 */
public class NioServer {
	public void start() throws IOException {
		/**
		 * 1,创建Selector
		 * 2,通过ServerSocketChannel创建channel通道
		 * 3,为channel通道绑定监听端口
		 * 4,设置channel为非阻塞模式
		 * 5,将channel注册到selector上,监听连接事件
		 * 6,循环等待新接入的链接
		 * 7,根据就绪状态,调用对应的方法处理业务逻辑
		 */
		Selector selector = Selector.open();
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.bind(new InetSocketAddress(8000));
		serverSocketChannel.configureBlocking(false);//4,设置channel为非阻塞模式
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("服务器启动成功!");
		for (;;) {//相当于while(true)循环,for;;JVM会优化成一条语句,while(true)会优化成3条语句,JAVA源码很多都是for;;
			/**
			 * 获取可用的Channel数量
			 */
			int readyChannels = selector.select();
			if (readyChannels == 0) {
				continue;
			}
			
			Set<SelectionKey> selectorKeys = selector.selectedKeys();
			Iterator iterator = selectorKeys.iterator();
			while(iterator.hasNext()) {
				//获取SelectionKey实例
				SelectionKey selectionKey = (SelectionKey)iterator.next();
				//移除Set中的当前SelectionKey
				iterator.remove();
				//接入事件,根据就绪状态,调用对应的方法处理业务逻辑
				
				//可读事件,根据就绪状态,调用对应的方法处理业务逻辑
			}
		}
	}
}
