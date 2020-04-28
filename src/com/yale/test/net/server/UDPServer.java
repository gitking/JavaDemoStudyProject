package com.yale.test.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPServer {
	public static void main(String[] args) throws IOException {
		DatagramSocket server = new DatagramSocket(9000);//服务器监听端口
		String str = "www.mldn.cn";//要发送的消息内容
		DatagramPacket packet = new DatagramPacket(str.getBytes(), 0, str.length(), InetAddress.getByName("localhost"), 9999);//发送数据
		server.send(packet);//发送消息
		System.out.println("消息发送完毕....");
		server.close();
	}
}
