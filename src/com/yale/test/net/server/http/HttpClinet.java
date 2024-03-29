package com.yale.test.net.server.http;

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
//import java.net.http.HttpClient;
//import java.net.http.HttpClient.Version;
//import java.net.http.HttpRequest;
//import java.net.http.HttpRequest.BodyPublishers;
//import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/*
 * HTTP编程
 * 什么是HTTP？HTTP就是目前使用最广泛的Web应用程序使用的基础协议，例如，浏览器访问网站，手机App访问后台服务器，都是通过HTTP协议实现的。
 * HTTP是HyperText Transfer Protocol的缩写，翻译为超文本传输协议，它是基于TCP协议之上的一种请求-响应协议。
 * 我们来看一下浏览器请求访问某个网站时发送的HTTP请求-响应。当浏览器希望访问某个网站时，浏览器和网站服务器之间首先建立TCP连接，且服务器总是使用80端口和加密端口443，
 * 然后，浏览器向服务器发送一个HTTP请求，服务器收到后，返回一个HTTP响应，并且在响应中包含了HTML的网页内容，这样，浏览器解析HTML后就可以给用户显示网页了。一个完整的HTTP请求-响应如下：
 *         GET / HTTP/1.1
            Host: www.sina.com.cn
            User-Agent: Mozilla/5 MSIE
            Accept: *//*                ┌────────┐
┌─────────┐ Accept-Language: zh-CN,en  │░░░░░░░░│
│O ░░░░░░░│───────────────────────────>├────────┤
├─────────┤<───────────────────────────│░░░░░░░░│
│         │ HTTP/1.1 200 OK            ├────────┤
│         │ Content-Type: text/html    │░░░░░░░░│
└─────────┘ Content-Length: 133251     └────────┘
  Browser   <!DOCTYPE html>              Server
            <html><body>
            <h1>Hello</h1>
            ...
 * HTTP请求的格式是固定的，它由HTTP Header和HTTP Body两部分构成。第一行总是请求方法 路径 HTTP版本，例如，GET / HTTP/1.1表示使用GET请求，路径是/，版本是HTTP/1.1。
 * 后续的每一行都是固定的Header: Value格式，我们称为HTTP Header，服务器依靠某些特定的Header来识别客户端请求，例如：
 * Host：表示请求的域名，因为一台服务器上可能有多个网站，因此有必要依靠Host来识别用于请求；
    User-Agent：表示客户端自身标识信息，不同的浏览器有不同的标识，服务器依靠User-Agent判断客户端类型；
    Accept：表示客户端能处理的HTTP响应格式，*//*表示任意格式，text/*表示任意文本，image/png表示PNG格式的图片；
    Accept-Language：表示客户端接收的语言，多种语言按优先级排序，服务器依靠该字段给用户返回特定语言的网页版本。
 * 如果是GET请求，那么该HTTP请求只有HTTP Header，没有HTTP Body。如果是POST请求，那么该HTTP请求带有Body，以一个空行分隔。一个典型的带Body的HTTP请求如下：
 * POST /login HTTP/1.1
 *	Host: www.example.com
 *	Content-Type: application/x-www-form-urlencoded
 *	Content-Length: 30
 *	username=hello&password=123456
 * POST请求通常要设置Content-Type表示Body的类型，Content-Length表示Body的长度，这样服务器就可以根据请求的Header和Body做出正确的响应。
 * 此外，GET请求的参数必须附加在URL上，并以URLEncode方式编码，例如：http://www.example.com/?a=1&b=K%26R，参数分别是a=1和b=K&R。因为URL的长度限制，
 * GET请求的参数不能太多，而POST请求的参数就没有长度限制，因为POST请求的参数必须放到Body中。并且，POST请求的参数不一定是URL编码，可以按任意格式编码，只需要在Content-Type中正确设置即可。常见的发送JSON的POST请求如下：
 * POST /login HTTP/1.1
	Content-Type: application/json
	Content-Length: 38
	{"username":"bob","password":"123456"}
 * HTTP响应也是由Header和Body两部分组成，一个典型的HTTP响应如下：
 * HTTP/1.1 200 OK
	Content-Type: text/html
	Content-Length: 133251
	
	<!DOCTYPE html>
	<html><body>
	<h1>Hello</h1>
 * 响应的第一行总是HTTP版本 响应代码 响应说明，例如，HTTP/1.1 200 OK表示版本是HTTP/1.1，响应代码是200，响应说明是OK。客户端只依赖响应代码判断HTTP响应是否成功。HTTP有固定的响应代码：
 *  1xx：表示一个提示性响应，例如101表示将切换协议，常见于WebSocket连接；
    2xx：表示一个成功的响应，例如200表示成功，206表示只发送了部分内容；
    3xx：表示一个重定向的响应，例如301表示永久重定向，303表示客户端应该按指定路径重新发送请求；
    4xx：表示一个因为客户端问题导致的错误响应，例如400表示因为Content-Type等各种原因导致的无效请求，404表示指定的路径不存在；
    5xx：表示一个因为服务器问题导致的错误响应，例如500表示服务器内部故障，503表示服务器暂时无法响应。
 * 当浏览器收到第一个HTTP响应后，它解析HTML后，又会发送一系列HTTP请求，例如，GET /logo.jpg HTTP/1.1请求一个图片，服务器响应图片请求后，会直接把二进制内容的图片发送给浏览器：
 * HTTP/1.1 200 OK
	Content-Type: image/jpeg
	Content-Length: 18391
	????JFIFHH??XExifMM?i&??X?...(二进制的JPEG图片)
 * 因此，服务器总是被动地接收客户端的一个HTTP请求，然后响应它。客户端则根据需要发送若干个HTTP请求。
 * 对于最早期的HTTP/1.0协议，每次发送一个HTTP请求，客户端都需要先创建一个新的TCP连接，然后，收到服务器响应后，关闭这个TCP连接。由于建立TCP连接就比较耗时，因此，为了提高效率，HTTP/1.1协议允许在一个TCP连接中反复发送-响应，这样就能大大提高效率：
 *                         ┌─────────┐
	┌─────────┐            │░░░░░░░░░│
	│O ░░░░░░░│            ├─────────┤
	├─────────┤            │░░░░░░░░░│
	│         │            ├─────────┤
	│         │            │░░░░░░░░░│
	└─────────┘            └─────────┘
	     │      request 1       │
	     │─────────────────────>│
	     │      response 1      │
	     │<─────────────────────│
	     │      request 2       │
	     │─────────────────────>│
	     │      response 2      │
	     │<─────────────────────│
	     │      request 3       │
	     │─────────────────────>│
	     │      response 3      │
	     │<─────────────────────│
	     ▼                      ▼
 * 因为HTTP协议是一个请求-响应协议，客户端在发送了一个HTTP请求后，必须等待服务器响应后，才能发送下一个请求，这样一来，如果某个响应太慢，它就会堵住后面的请求。
 * 所以，为了进一步提速，HTTP/2.0允许客户端在没有收到响应的时候，发送多个HTTP请求，服务器返回响应的时候，不一定按顺序返回，只要双方能识别出哪个响应对应哪个请求，就可以做到并行发送和接收：
 *                         ┌─────────┐
	┌─────────┐            │░░░░░░░░░│
	│O ░░░░░░░│            ├─────────┤
	├─────────┤            │░░░░░░░░░│
	│         │            ├─────────┤
	│         │            │░░░░░░░░░│
	└─────────┘            └─────────┘
	     │      request 1       │
	     │─────────────────────>│
	     │      request 2       │
	     │─────────────────────>│
	     │      response 1      │
	     │<─────────────────────│
	     │      request 3       │
	     │─────────────────────>│
	     │      response 3      │
	     │<─────────────────────│
	     │      response 2      │
	     │<─────────────────────│
	     ▼                      ▼
 * 可见，HTTP/2.0进一步提高了效率。
 * HTTP编程
 * 既然HTTP涉及到客户端和服务器端，和TCP类似，我们也需要针对客户端编程和针对服务器端编程。
 * 本节我们不讨论服务器端的HTTP编程，因为服务器端的HTTP编程本质上就是编写Web服务器，这是一个非常复杂的体系，也是JavaEE开发的核心内容，我们在后面的章节再仔细研究。
 * 本节我们只讨论作为客户端的HTTP编程。
 * 因为浏览器也是一种HTTP客户端，所以，客户端的HTTP编程，它的行为本质上和浏览器是一样的，即发送一个HTTP请求，接收服务器响应后，获得响应内容。只不过浏览器进一步把响应内容解析后渲染并展示给了用户，而我们使用Java进行HTTP客户端编程仅限于获得响应内容。
 * 我们来看一下Java如果使用HTTP客户端编程。
 * Java标准库提供了基于HTTP的包，但是要注意，早期的JDK版本是通过HttpURLConnection访问HTTP，典型代码如下：
 * URL url = new URL("http://www.example.com/path/to/target?a=1&b=2");
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setUseCaches(false);
	conn.setConnectTimeout(5000); // 请求超时5秒
	// 设置HTTP头:
	conn.setRequestProperty("Accept", "*//*");
	conn.setRequestProperty("User-Agent", "Mozilla/5.0 (compatible; MSIE 11; Windows NT 5.1)");
	// 连接并发送HTTP请求:
	conn.connect();
	// 判断HTTP响应是否200:
	if (conn.getResponseCode() != 200) {
	    throw new RuntimeException("bad response");
	}		
	// 获取所有响应Header:
	Map<String, List<String>> map = conn.getHeaderFields();
	for (String key : map.keySet()) {
	    System.out.println(key + ": " + map.get(key));
	}
	// 获取响应内容:
	InputStream input = conn.getInputStream();
 * 上述代码编写比较繁琐，并且需要手动处理InputStream，所以用起来很麻烦。
 * 从Java 11开始，引入了新的HttpClient，它使用链式调用的API，能大大简化HTTP的处理。
 * 我们来看一下如何使用新版的HttpClient。首先需要创建一个全局HttpClient实例，因为HttpClient内部使用线程池优化多个HTTP连接，可以复用：
 * static HttpClient httpClient = HttpClient.newBuilder().build();
 * 使用GET请求获取文本内容代码如下：
 * 如果我们要获取图片这样的二进制内容，只需要把HttpResponse.BodyHandlers.ofString()换成HttpResponse.BodyHandlers.ofByteArray()，就可以获得一个HttpResponse<byte[]>对象。如果响应的内容很大，不希望一次性全部加载到内存，可以使用HttpResponse.BodyHandlers.ofInputStream()获取一个InputStream流。
 * 要使用POST请求，我们要准备好发送的Body数据并正确设置Content-Type：
 * 可见发送POST数据也十分简单。
 * 小结
 * Java提供了HttpClient作为新的HTTP客户端编程接口用于取代老的HttpURLConnection接口；
 * HttpClient使用链式调用并通过内置的BodyPublishers和BodyHandlers来更方便地处理数据。
 * 使用POST发送数据
 * 以POST方式发送数据主要是为了向服务器发送较大量的客户端的数据，它不受URL的长度限制。
 * POST请求将数据以URL编码的形式放在HTTP正文中，字段形式为fieldname=value，用&分隔每个字段。注意所有的字段都被作为字符串处理。
 * 实际上我们要做的就是模拟浏览器POST一个表单。以下是IE发送一个登陆表单的POST请求：
 * POST http://127.0.0.1/login.do HTTP/1.0
	Accept: image/gif, image/jpeg, image/pjpeg, *\/*
	Accept-Language: en-us,zh-cn;q=0.5
	Content-Type: application/x-www-form-urlencoded
	User-Agent: Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)
	Content-Length: 28
	username=admin&password=1234
 * 要在MIDP应用程序中模拟浏览器发送这个POST请求，首先设置HttpConnection的请求方式为POST：
 * hc.setRequestMethod(HttpConnection.POST);
 * 然后构造出HTTP正文：
 * byte[] data = "username=admin&password=1234".getBytes();
 * 并计算正文长度，填入Content-Type和Content-Length：
 * hc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
 * hc.setRequestProperty("Content-Length", String.valueOf(data.length));
 * 然后打开OutputStream将正文写入：
 * OutputStream output = hc.openOutputStream();
 * output.write(data);
 * 需要注意的是，数据仍需要以URL编码格式编码，由于MIDP库中没有J2SE中与之对应的URLEncoder类，因此，需要自己动手编写这个encode()方法，可以参考java.net.URLEncoder.java的源码。剩下的便是读取服务器响应，代码与GET一致，这里就不再详述。
 * https://www.liaoxuefeng.com/article/895889887461120
 */
public class HttpClinet {
//	static HttpClient httpClient = HttpClient.newBuilder().build();
//
//	public static void main(String[] args) throws Exception {
//		httpGet("https://www.sina.com.cn/");
//		httpPost("https://accounts.douban.com/j/mobile/login/basic",
//				"name=bob%40example.com&password=12345678&remember=false&ck=&ticket=");
//		httpGetImage("https://img.t.sinajs.cn/t6/style/images/global_nav/WB_logo.png");
//	}
//
//	static void httpGet(String url) throws Exception {
//		HttpRequest request = HttpRequest.newBuilder(new URI(url))
//				// 设置Header:
//				.header("User-Agent", "Java HttpClient").header("Accept", "*/*")
//				// 设置超时:
//				.timeout(Duration.ofSeconds(5))
//				// 设置版本:
//				.version(Version.HTTP_2).build();
//		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(response.body().substring(0, 1024) + "...");
//	}
//
//	static void httpGetImage(String url) throws Exception {
//		HttpRequest request = HttpRequest.newBuilder(new URI(url))
//				// 设置Header:
//				.header("User-Agent", "Java HttpClient").header("Accept", "*/*")
//				// 设置超时:
//				.timeout(Duration.ofSeconds(5))
//				// 设置版本:
//				.version(Version.HTTP_2).build();
//		HttpResponse<InputStream> response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
//		// 显示Http返回的图片:
//		BufferedImage img = ImageIO.read(response.body());
//		ImageIcon icon = new ImageIcon(img);
//		JFrame frame = new JFrame();
//		frame.setLayout(new FlowLayout());
//		frame.setSize(200, 100);
//		JLabel lbl = new JLabel();
//		lbl.setIcon(icon);
//		frame.add(lbl);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	}
//
//	static void httpPost(String url, String body) throws Exception {
//		HttpRequest request = HttpRequest.newBuilder(new URI(url))
//				// 设置Header:
//				.header("User-Agent", "Mozilla/5.0 (compatible, MSIE 11, Windows NT 6.3; Trident/7.0) like Gecko")
//				.header("Accept", "*/*").header("Content-Type", "application/x-www-form-urlencoded")
//				// 设置超时:
//				.timeout(Duration.ofSeconds(5))
//				// 设置版本:
//				.version(Version.HTTP_2)
//				// 使用POST并设置Body:
//				.POST(BodyPublishers.ofString(body, StandardCharsets.UTF_8)).build();
//		HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//		System.out.println(response.body());
//	}
	
//这个是电子投保单签署H5的里面的代码,可以看一下taobao-sdk-java-auto_1479188381469-20210512.jar 钉钉提供的这个jar里面的代码,我自己写的有公众号里面有说钉钉是怎么用jdk的HttpURLConnection这个东西的。
//可以看下钉钉是怎么设置超时时间的。
//	URL url = new URL(urlString);
//    conn = (HttpURLConnection) url.openConnection();
//    conn.setDoOutput(true);
//    conn.setDoInput(true);
//    conn.setRequestMethod(method);
//    conn.setRequestProperty("Accept-Charset", "utf-8");
//    conn.setRequestProperty("Content-Type", "application/json");
//    conn.setConnectTimeout(connectTimeout);
//    conn.setReadTimeout(readTimeout);这里还可以设置ReadTimeout的时间
}
