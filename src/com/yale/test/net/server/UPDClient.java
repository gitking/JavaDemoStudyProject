package com.yale.test.net.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UPDClient {
	public static void main(String[] args) throws IOException {
		DatagramSocket client = new DatagramSocket(9999);
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);
		System.out.println("客户端等待接收发送的消息....");
		client.receive(packet);
		System.out.println("接收到消息内容为:" + new String(data, 0, packet.getLength()));
		client.close();
	}
}
