package com.yale.test.net.server.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/*
 * UDP编程
 * 和TCP编程相比，UDP编程就简单得多，因为UDP没有创建连接，数据包也是一次收发一个，所以没有流的概念。
 * 在Java中使用UDP编程，仍然需要使用Socket，因为应用程序在使用UDP时必须指定网络接口（IP）和端口号。
 * 注意：UDP端口和TCP端口虽然都使用0~65535，但他们是两套独立的端口，即一个应用程序用TCP占用了端口1234，不影响另一个应用程序用UDP占用端口1234。
 * 服务器端
 * 在服务器端，使用UDP也需要监听指定的端口。Java提供了DatagramSocket来实现这个功能，代码如下：
 */
public class UDPServer {
	public static void main(String[] args) throws IOException {
		//服务器端首先使用如下语句在指定的端口监听UDP数据包：
		DatagramSocket ds = new DatagramSocket(6666);//监听指定端口
		//如果没有其他应用程序占据这个端口，那么监听成功，我们就使用一个无限循环来处理收到的UDP数据包：
		for (;;) {//无限循环
			byte[] buffer = new byte[1024];
			//要接收一个UDP数据包，需要准备一个byte[]缓冲区，并通过DatagramPacket实现接收：
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			ds.receive(packet);//收取一个UDP数据包
			/*
			 * 收取到的数据存储在buffer中,由packet.getOffset(), packet.getLength()指定起始位置和长度
			 * 将其按UTF-8编码转换为String
			 * 假设我们收取到的是一个String，那么，通过DatagramPacket返回的packet.getOffset()和packet.getLength()确定数据在缓冲区的起止位置：
			 */
			String cmd = new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
			/*
			 * 发送数据
			 * 当服务器收到一个DatagramPacket后，通常必须立刻回复一个或多个UDP包，因为客户端地址在DatagramPacket中，每次收到的DatagramPacket可能是不同的客户端，如果不回复，客户端就收不到任何UDP包。
			 * 发送UDP包也是通过DatagramPacket实现的，发送代码非常简单：
			 */
			String resp = "bad command";
			switch (cmd) {
			case "date":
				resp = LocalDate.now().toString();
				break;
			case "time":
				resp = LocalTime.now().withNano(0).toString();
				break;
			case "datetime":
				resp = LocalDateTime.now().withNano(0).toString();
				break;
			case "weather":
				resp = "sunny,10~15 C.";
				break;
			}
			System.out.println(cmd + ">>>" + resp);
			byte[] data = resp.getBytes(StandardCharsets.UTF_8);
			packet.setData(data);
			ds.send(packet);
		}
	}
}
