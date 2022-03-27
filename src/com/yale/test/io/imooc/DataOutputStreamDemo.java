package com.yale.test.io.imooc;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 编写反应灵敏的联网提示界面
 * 由于无线设备所能支持的网络协议非常有限，仅限于HTTP，Socket，UDP等几种协议，不同的厂家可能还支持其他网络协议，但是，MIDP 1.0规范规定，HTTP协议是必须实现的协议，而其他协议的实现都是可选的。
 * 因此，为了能在不同类型的手机上移植，我们尽量采用HTTP作为网络连接的首选协议，这样还能重用服务器端的代码。但是，由于HTTP是一个基于文本的效率较低的协议，因此，必须仔细考虑手机和服务器端的通信内容，尽可能地提高效率。
 * 对于MIDP应用程序，应当尽量做到：
 *  发送请求时，附加一个User-Agent头，传入MIDP和自身版本号，以便服务器能识别此请求来自MIDP应用程序，并且根据版本号发送相应的相应。
    连接服务器时，显示一个下载进度条使用户能看到下载进度，并能随时中断连接。
 *  由于无线网络连接速度还很慢，因此有必要将某些数据缓存起来，可以存储在内存中，也可以放到RMS中。
 * 对于服务器端而言，其输出响应应当尽量做到：
 *  明确设置Content-Length字段，以便MIDP应用程序能读取HTTP头并判断自身是否有能力处理此长度的数据，如果不能，可以直接关闭连接而不必继续读取HTTP正文。
    服务器不应当发送HTML内容，因为MIDP应用程序很难解析HTML，XML虽然能够解析，但是耗费CPU和内存资源，因此，应当发送紧凑的二进制内容，用DataOutputStream直接写入并设置Content-Type为application/octet-stream。
    尽量不要重定向URL，这样会导致MIDP应用程序再次连接服务器，增加了用户的等待时间和网络流量。
    如果发生异常，例如请求的资源未找到，或者身份验证失败，通常，服务器会向浏览器发送一个显示出错的页面，可能还包括一个用户登录的Form，但是，向MIDP发送错误页面毫无意义，应当直接发送一个404或401错误，这样MIDP应用程序就可以直接读取HTTP头的响应码获取错误信息而不必继续读取相应内容。
 *  由于服务器的计算能力远远超过手机客户端，因此，针对不同客户端版本发送不同响应的任务应该在服务器端完成。例如，根据客户端传送的User-Agent头确定客户端版本。这样，低版本的客户端不必升级也能继续使用。
 * SUN的MIDP库提供了javax.microediton.io包，能非常容易地实现HTTP连接。但是要注意，由于网络有很大的延时，必须把联网操作放入一个单独的线程中，以避免主线程阻塞导致用户界面停止响应。
 * 事实上，MIDP运行环境根本就不允许在主线程中操作网络连接。因此，我们必须实现一个灵活的HTTP联网模块，能让用户非常直观地看到当前上传和下载的进度，并且能够随时取消连接。
 * https://www.liaoxuefeng.com/article/895889887461120
 * @author issuser
 */
public class DataOutputStreamDemo {

	public static void main(String[] args) throws IOException {
		String file = "demo/dos.txt";
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		dos.writeInt(10);
		dos.writeInt(-10);
		dos.writeLong(10L);
		dos.writeDouble(10.5);
		dos.writeUTF("中国");//采用UTF-8编码写出
		dos.writeChars("中国");//采用UTF-16be编码写出
		dos.close();
		IOUtil.printHex(file);
	}
}
