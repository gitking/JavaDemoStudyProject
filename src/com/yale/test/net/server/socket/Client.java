package com.yale.test.net.server.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
 * 客户端
 * 相比服务器端，客户端程序就要简单很多。一个典型的客户端程序如下：
 * TCP端口只是一个16位宽,用来识别服务器上特定程序的数字。
 * HTTP的端口号是80,Telnet服务器的端口是23,POP3邮件服务器的端口是110,SMTP邮局交换服务器的端口是25，
 * 每个服务器上面都有65536(0~65535)个端口,0~1023都已经保留给已知的特定服务使用了,你不应该使用0~1023的这些端口。
 * 在java里面所有网络运作的底层细节都已经由java.net函数库处理掉了。java的一项好处是传送与接收网络上的数据只不过是链接上
 * 使用不同链接串流的输入/输出而已。如果有BufferedReader就可以读取,若数据来自文件或网络另一端,则BufferedReader不用花费很多精力去照顾。也就是说你可以使用BufferedReader而不管是串流来自文件或Socket。
 */
public class Client {
	public static void main(String[] args) throws UnknownHostException, IOException {
		/*
		 * 连接到服务器端，注意下述代码的服务器地址是"localhost"，表示本机地址，端口号是6666。如果连接成功，将返回一个Socket实例，用于后续通信。
		 * Socket流
		 * 当Socket连接创建成功后，无论是服务器端，还是客户端，我们都使用Socket实例进行网络通信。因为TCP是一种基于流的协议，因此，Java标准库使用InputStream和OutputStream来封装Socket的数据流，这样我们使用Socket的流，和普通IO流类似：
		 */
		Socket sock = new Socket("localhost", 6666);
		try (InputStream input = sock.getInputStream()) {// 用于读取网络数据:
			try(OutputStream output = sock.getOutputStream()) {// 用于写入网络数据:
				handle(input, output);
			}
		}
		sock.close();
		System.out.println("disconnected.");
	}
	
	private static void handle(InputStream input, OutputStream output) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
		Scanner scanner = new Scanner(System.in);
		System.out.println("[server]" + reader.readLine());
		for(;;) {
			System.out.println(">>>");//打印提示
			String s = scanner.nextLine();//读取一行输入
			writer.write(s);
			writer.newLine();
			/*
			 * 最后我们重点来看看，为什么写入网络数据时，要调用flush()方法。
			 * 如果不调用flush()，我们很可能会发现，客户端和服务器都收不到数据，这并不是Java标准库的设计问题，
			 * 而是我们以流的形式写入数据的时候，并不是一写入就立刻发送到网络，而是先写入内存缓冲区，直到缓冲区满了以后，
			 * 才会一次性真正发送到网络，这样设计的目的是为了提高传输效率。如果缓冲区的数据很少，而我们又想强制把这些数据发送到网络，
			 * 就必须调用flush()强制把缓冲区数据发送出去。
			 */
			writer.flush();
			String resp = reader.readLine();
			System.out.println("<<<" + resp);
			if ("bye".equals(resp)) {
				break;
			}
		}
	}
}
