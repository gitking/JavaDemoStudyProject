package com.yale.test.io.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/*
 * https://zhuanlan.zhihu.com/p/23488863  Java NIO浅析  美团技术团队
 * https://blog.csdn.net/u013096088/article/details/78774627#:~:text=DirectBy,%E5%BB%BA%E3%80%81%E4%BD%BF%E7%94%A8%E5%92%8C%E9%94%80%E6%AF%81%E3%80%82 Java NIO学习笔记三（堆外内存之 DirectByteBuffer 详解）
 */
public class NioClientHandler implements Runnable{
	
	private Selector selector;
	public NioClientHandler(Selector selector) {
		this.selector = selector;
	}
	
	@Override
	public void run() {
		try {
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
					//可读事件,根据就绪状态,调用对应的方法处理业务逻辑
					if (selectionKey.isReadable()) {
						readHandler(selectionKey, selector);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//可读事件处理
	private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
		//要从selectionKey中获取到已经就绪的channel
		SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
		//创建Buffer
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		//循环读取服务器端的响应信息
		String response = "";
		while (socketChannel.read(byteBuffer) > 0) {
			byteBuffer.flip();//切换到读模式
			response += Charset.forName("UTF-8").decode(byteBuffer);
		}
		//将channel再次注册到Selector上,监听他的可读事件
		socketChannel.register(selector, SelectionKey.OP_READ);
		if (response.length() > 0) {//将服务器端响应信息打印到本地 
			System.out.println(response);
		}
	}
		
}
