package com.yale.test.net.server.headfirstjava;

/*
 * 建立对服务器的Socket链接
 * 1、Socket chatSocket = new Socket("127.0.0.1", 5000);
 * 2、建立连接到Socket上底层输入串流的InputStreamReader
 * InputStreamReader stream = new InputStreamReader(chatSocket.getInputStream());
 * InputStreamReader是底层和高层串流的桥梁，chatSocket.getInputStream()从Socket取得输入串流
 * 3、建立BufferedReader来读取
 * 将InputStreamReader转换成BufferedReader
 * BufferedReader reader = new BufferedReader(stream);
 * String message = reader.readLine();
 * 用PrintWriter写数据到Socket上:
 * 因为每次都是写入一个String,所以PrintWriter是最标准的做法.并且PrintWriter中有print()和println()方法就跟System.out里面的方法刚好一样。
 * 1建立对服务器的Socket连接
 * Socket chatSocket = new Socket("127.0.0.1",5000);
 * 2，建立连接到Socket的PrintWriter
 * PrintWriter writer = new PrintWriter(chatSocket.getOutputStream());
 * PrintWriter是字符数据和字节间的转换桥梁,可以衔接String和Socket俩端
 * 3、写入数据
 * writer.println("message to send");
 * writer.print("another message")
 */
public class Demo {

}
