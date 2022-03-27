package com.yale.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器
 * NIO Non Blocking io非阻塞IO JDK1.4有的
 * NIO网络编程缺陷
 * 麻烦:NIO类库和API繁杂
 * 心累:可靠性能力补齐,工作量和难度都非常大
 * 有坑:Selector空轮询,导致CPU100%
 * https://zhuanlan.zhihu.com/p/23488863  Java NIO浅析  美团技术团队
 * https://blog.csdn.net/u013096088/article/details/78774627#:~:text=DirectBy,%E5%BB%BA%E3%80%81%E4%BD%BF%E7%94%A8%E5%92%8C%E9%94%80%E6%AF%81%E3%80%82 Java NIO学习笔记三（堆外内存之 DirectByteBuffer 详解）
 * https://cloud.tencent.com/developer/article/1129904 今咱们来聊聊JVM 堆外内存泄露的BUG是如何查找的
 * https://club.perfma.com/article/1835499?from=groupmessage#/article/1835499?from=groupmessage
 * 难道是linux glibc 中经典的 64M 内存问题?一次 Java 进程 OOM 的排查分析（glibc 篇） https://club.perfma.com/article/1709425?last=1714757&type=parent#/article/1709425?last=1714757&type=parent
 * JAVA堆外内存排查小结 https://zhuanlan.zhihu.com/p/60976273
 * https://developer.aliyun.com/article/657160 一次堆外内存泄露的排查过程
 * Linux中使用gdb dump内存 https://blog.csdn.net/qq_36119192/article/details/96474833
 * netty 堆外内存泄露排查盛宴 https://mp.weixin.qq.com/s/fxx_AxH2mbtsr5BgdZ6uFA  https://github.com/mrniko/netty-socketio
 * Netty堆外内存泄漏排查盛宴 https://www.heapdump.cn/article/2956826
 * 一次netty"引发的"诡异old gc问题排查过程 https://www.heapdump.cn/article/2963029
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
				/**
				 * 这里有坑:Selector空轮询,导致CPU100%
				 * 主要出现在类Unix上面,这个问题java官方声称在jdk1.6的版本中已经修复,
				 * 但实际证明该问题在jdk1.8上面依然存在,只不过发生的概率低了一些.
				 * 如果要在Linux上面使用,要非常小心,最好不用,老师没说替代办法
				 */
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
				if (selectionKey.isAcceptable()) {
					acceptHandler(serverSocketChannel, selector);
				}
				//可读事件,根据就绪状态,调用对应的方法处理业务逻辑
				if (selectionKey.isReadable()) {
					readHandler(selectionKey, selector);
				}
			}
		}
	}
	
	//接入事件处理
	private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
		//如果是接入事件,创建SocketChannel
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);//将socketChannel设置为非阻塞工作模式
		//将channel注册到selector上,监听可读事件
		socketChannel.register(selector, SelectionKey.OP_READ);
		//回复客户端提示信息
		socketChannel.write(Charset.forName("UTF-8").encode("你与聊天室里其他人不是朋友关系,请注意隐私安全"));
	}
	
	//可读事件处理
	private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
		//要从selectionKey中获取到已经就绪的channel
		SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
		//创建Buffer
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		//循环读取客户端的请求信息
		String request = "";
		while (socketChannel.read(byteBuffer) > 0) {
			byteBuffer.flip();//切换到读模式
			request += Charset.forName("UTF-8").decode(byteBuffer);
		}
		//将channel再次注册到Selector上,监听他的可读事件
		socketChannel.register(selector, SelectionKey.OP_READ);
		if (request.length() > 0) {//将客户端发送的请求消息光博给其他客户端
			System.out.println(":::" +  request);
			broadCast(selector, socketChannel, request);
		}
	}
	//广播给其他所有客户端
	private void broadCast(Selector selector, SocketChannel sourceChannel, String request) {
		//获取到所有已接入的客户端channel
		Set<SelectionKey> selectionKeySet = selector.keys();
		//
		selectionKeySet.forEach(selectionKey ->{//循环向所有channel广播信息
			Channel targetChannel = selectionKey.channel();
			//删除发送消息的客户端
			if (targetChannel instanceof SocketChannel && targetChannel != sourceChannel) {
				try {//将消息发送到targetChannel客户端
					((SocketChannel)targetChannel).write(Charset.forName("UTF-8").encode(request));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer();
		nioServer.start();
	}
}
