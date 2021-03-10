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
	
	/*
	 * 在网络通信中,参数非常多,不过得知道一些比较重点的参数,例如是否能连通,心跳,对方是否已经关闭程序等.
	 * 对应的手段通常是连接超时,读取数据超时,他们在socket中都提供了参数,在Socket原始的连接方法中,有一个方法是:
	 * connect(SocketAddress endpoint, int timeout)
	 * 当设置了timeout参数后,就可以达到连接超时的效果,而通过调用方法setSoTimeout(int timeout),可以设置每次读取超时.
	 * 如果不设置这些超时参数,那么当发起网络连接时,网络是通的,但是协议不通(指对应的端口未打开,如果网络不通,则通常直接抛错：No route to host:connect),
	 * 则可能很长时间(通常是180s)才会报出错误:connect timed out,而发生read()操作若未设置超时,则将会永远等下去,直到进程关闭或被OS的Socket自动超时所触发,
	 * 所以超时对于程序来讲非常重要.
	 * 《Java特种兵》第67页
	 * 如果程序发送read()操作若未设置超时,程序会永久等下去,直到进程关闭或被OS的Socket自动超时所触发,所以超时对于程序来讲是十分重要的。
	 * Socket是通信的基础,胖哥在这里再补充几个API。
	 * setReuseAddress(true|false),该参数设置为true,允许将多个Socket绑定到同一个端口上,通过getReuseAdree()方法获取当前值即可。
	 * 但是在发生socket.bind()之前必须先设置才会生效,这个参数很少会考虑使用。
	 * setTcpNoDelay(true|false),该参数默认为false,会启用Nagle算法。启用这种算法是为了避免网络阻塞,当Socket交互双方存在大量的小数据交互时,
	 * 例如1个字节的网络交互,还会携带IPv4中20个字节的包头(IPv6中为40个字节),还不包括数据链路层对网络包的进一步封装。在这样情况下大量发生时,就会导致
	 * 网络负载增加几十倍。Nagle算法为了解决这一问题,将较小的包延迟发送(最长200ms),若200ms内有多次小数据发送,则会合并在一起发送,若数据达到一定范围,也会
	 * 立即发送。这虽然解决了网络堵塞的问题,但是同时给我们带来的问题是Socket双方的许多小包交互最少会延迟200ms以上,尤其是一些状态交换,因此发现这类简单的小包
	 * 通信,也需要200ms以上的时间。将该参数设置为true,即可瞬间提升性能,在网络较好的情况下应该为1~2ms。同样,该参数打开后可能会导致网络堵塞,所以对于许多小数据
	 * 的发送可以在本地做Buffer,一次性发送既可以得到速度上的提升,又可以减少网络负载。
	 * setSoLinger(true|false, int linger),该参数决定Socket关闭时是否尝试继续发送Kernel缓冲区中还未发送出去的数据,
	 * 若设置为true,则由第二个int类型的参数决定发送Kernel缓冲区中还未发送的内容最长的等待时间,单位为秒,通过getSoLinger()可以获取到设置的值。
	 * setSendBufferSize(int),设置发送缓冲区的大小,默认值为8192字节,通过getSendBufferSize()得到当前值,一般保持默认就OK。
	 * setReceiveBufferSize(int),设置接收数据的缓冲区大小,默认值为8192字节,通过getReceiveBufferSize()来获取。
	 * setKeepAlive(true|false),它和前端的keepAlive是有区别的,它的原理是每隔一段时间(很长的一段时间,例如2小时)会将数据包发送到对方响应,
	 * 则认为连接依然存活;如果为响应,则在十多分钟后再发送一个数据包,如果对方还未响应,则再过十多分钟继续发送一个数据包,如果对方还是未响应,则会将客户端
	 * 的Socket关闭,在某些情况下设置改参数后可以避免客户端的一些垃圾Socket永远无法关闭的情况。该参数的默认值为false,可以通过getKeepAlive()
	 * 获取到当前值.
	 * setOOBInline(true|false),这个参数默认为false,若开启,则允许通过Socket的方法sendUrgentData(int)发送,这个API是直接
	 * 发送,不会经过缓冲区。入口参数为int类型,不过只有最低8位被发送,这些数据如果同时发送到接收方的缓冲区中,则将与普通的数据混合在一起。换句话说,
	 * 如果通过OutputStream发送一段文本再发送几个数字,那么接收方读取时会认为是一串数据,它不会经过缓冲区而直接发送,所以有可能代码是写在OutputStream发送
	 * 数据之后,但是接收方会先接收到这部分数据(除非先进行了flush操作),因为对方与本地是通过同一个套接字在通信,不会因为API不同而区分处理。
	 * 常见的异常情况:
	 * 1,如果服务方(这里指提供数据的一方)已经将对应的Socket关闭,而这里是正常关闭Socket操作,请求方再发起多次writeLine()操作时,通常会
	 * 抛出异常:java.net.SocketException:Software caused connection abort:socket write error.这是一类十分
	 * 常见的错误,当客户端关闭时,服务器端尝试读取客户端的一行数据也会报出这样的错误。如果此时发起readLine()操作,则通常读取到的都是null.
	 * 2,如果服务方的进程直接退出,请求方使用了BufferedReader或BufferWriter,请求方发起readLine(),writeLine()操作时则会
	 * 出现java.net.SocketException异常,异常的message通常有俩种:请求方发起writeLine()操作得到的结果是"Connection reset by peer: socket write error"
	 * 而发起readLine()操作得到的结果是"Connection reset"
	 * 3,当通过read()方法读取一个字节时,如果对方已经关闭,则会返回-1,如果通过一些java提供的流包装处理类中类似于readByte()的方法时(如果:DataInputStream)
	 * 则会抛出java.io.EOFException异常.
	 * 4,如果BufferedReader/BufferedWriter自己本身已经关闭了,但还在做read,write操作,就会从内部的一个ensureOpen()方法中抛出
	 * java.io.IOException异常,异常的message信息为"Stream closed"
	 * 抛出的异常还有很多,某些异常是JVM级别本身检测抛出的,所以就与具体的实现类相关,如果大家看到一种没有见过的异常,则要学会去看源代码
	 * 这样就会知道异常是在哪里抛出的,是在什么情况下抛出的。
	 * 《Java特种兵》第68页
	 */
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
