package com.yale.test.net.server.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.DefaultMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.remoting.httpinvoker.HttpComponentsHttpInvokerRequestExecutor;
import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor;

/**
 * https://mp.weixin.qq.com/s/4wty3-igm9wHec5h0jxVeQ
 * http://hc.apache.org/status.html
 * The 3.1 branch of Commons HttpClient is at the end of life. No more public releases are expected.
 * Commons HttpClient的3.1分支即将结束。 预计不会有更多的公开发布。
 * commons-httpclient-3.1.jar跟httpclient-4.5.9.jar是什么关系？
 * 取而代之的是httpclient-4.5.9.jar
 * 
 * commons-httpclient-3.1.jar的官网教程连接为:http://hc.apache.org/httpclient-legacy/tutorial.html#Method_recovery
 * 
 * https://club.perfma.com/article/2428647
 * ava.io.BufferedReader.readLine(BufferedReader.java:371)
java.io.BufferedReader.readLine(BufferReader.java:389)
java_io_BufferedReader$readLine.call(Unknown Source)
com.domain.detect.http.HttpClient.getResponse(HttpClient.groovy:122)
com.domain.detect.http.HttpClient.this$2$getResponse(HttpClient.groovy)
 * 这个线程栈的报错我见得多了，我们设置的 HTTP DNS 超时是 1s， connect 超时是 2s， read 超时是 3s，这种报错都是探测服务正常发送了 HTTP 请求，服务器也在收到请求正常处理后正常响应了，但数据包在网络层层转发中丢失了，所以请求线程的执行栈会停留在获取接口响应的地方。
 * 这种情况的典型特征就是能在服务器上查找到对应的日志记录。而且日志会显示服务器响应完全正常。与它相对的还有线程栈停留在 Socket connect 处的，这是在建连时就失败了，服务端完全无感知。
 * 其实还是要反省一下自己的，一开始报警邮件里还有这样的线程栈：
 * groovy.json.internal.JsonParserCharArray.decodeValueInternal(JsonParserCharArray.java:166)
 * groovy.json.internal.JsonParserCharArray.decodeJsonObject(JsonParserCharArray.java:132)
 * groovy.json.internal.JsonParserCharArray.decodeValueInternal(JsonParserCharArray.java:186)
 * groovy.json.internal.JsonParserCharArray.decodeJsonObject(JsonParserCharArray.java:132)
 * groovy.json.internal.JsonParserCharArray.decodeValueInternal(JsonParserCharArray.java:186)
 * 看到这种报错线程栈却没有细想，要知道 TCP 是能保证消息完整性的，况且消息没有接收完也不会把值赋给变量，这种很明显的是内部错误，如果留意后细查是能提前查出问题所在的，查问题真是差了哪一环都不行啊。
 * 来源 | https://zhenbianshu.github.io/
 * @author dell
 */
public class HttpClientUtil {
	private static HttpClient httpClient = null;  
    private static final String CHARSET = "UTF-8";  
    private HttpClientUtil(){  
    } 
    
    //apache的commons-httpclient-3.1.jar的httpclient用法
    public static HttpClient getSaveHttpClient(){  
    	if(httpClient == null){
    		synchronized(HttpClientUtil.class){
    			if(httpClient == null){  
    	        	HttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
    	        	HttpConnectionManagerParams managerParams = new HttpConnectionManagerParams();
    	        	managerParams.setConnectionTimeout(50000);//超时时间为50秒
    	        	managerParams.setSoTimeout(50000);//每次读取超时时间为50秒
    	        	//甚至超出了虚拟化平台对容器的2000线程数限制(平台为了避免机器上的部分容器线程数过高，导致机器整体夯死而设置的熔断保护),因此实例是被虚拟化平台kill了
    	        	managerParams.setMaxTotalConnections(2000);
    	        	//maxHostConnections的含义:每个host路由的默认最大连接,需要通过setDefaultMaxConnectionsPerHost来设置,否则默认值是2。
    	        	//所以并不是我对业务的最大连接数计算失误，而是因为不知道要设置DefaultMaxConnectionsPerHost而导致每个请求的Host并发连接数只有2，
    	        	//限制了线程获取连接的并发度(所以难怪刚才观察tcp并发度的时候发现只有2个连接建立 )
    	        	// netstat -ant|grep "10.15.22.14"|awk '{print $6}'|sort|uniq -c
    	        	// netstat -anp|grep -E "10.15.22.14"|awk '/tcp/ {print $6}'|sort|uniq -c
    	        	managerParams.setDefaultMaxConnectionsPerHost(2000);
    	        	//确定是否使用 Nagle 算法。 Nagle 算法试图通过最小化发送的段数来节省带宽。 当应用程序希望减少网络延迟并提高性能时，它们可以禁用 Nagle 算法（即启用 TCP_NODELAY）。 数据将更早发送，代价是带宽消耗增加。 
    	        	//managerParams.setTcpNoDelay(true);确定是否使用 Nagle 算法。 纳格尔算法
    	        	manager.setParams(managerParams);
    	        	//manager.getParams().setTcpNoDelay(true);
    	        	
    	        	/*
    	        	 * 3.1版本，俩个超时时间已经移到params里面了
						client.getHttpConnectionManager().getParams() 
						            .setConnectionTimeout(3000); 
						    client.getHttpConnectionManager().getParams().setSoTimeout(3000); 
    	        	 */

    	        	httpClient = new HttpClient(manager);
    	        	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,CHARSET);
    	        	
    	        	/**
    	        	 * https://stackoverflow.com/questions/2285799/java-apache-httpclient-how-to-disable-retry
    	        	 * apache的httpclient默认有retry重试机制,默认会重试3次,怎么禁用(关掉)这个重试机制呢,不同的版本有不同的办法
    	        	 * httpclient-4.5.9.jar
    	        	 * DefaultHttpRequestRetryHandler这个类httpclient4.0才有,commons-httpclient-3.1.jar 这个版本没有
    	        	 * setHttpRequestRetryHandler这个方法也是httpclient4.0才有,commons-httpclient-3.1.jar 这个版本没有
    	        	 * httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
    	        	 * 
    	        	 * From httpclient 4.3 use HttpClientBuilder
    	        	 * HttpClientBuilder.create().disableAutomaticRetries().build();
    	        	 * 
    	        	 * DefaultHttpClient httpClient = new DefaultHttpClient();
    	        	 * DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
    	        	 * httpClient.setHttpRequestRetryHandler(retryHandler);
    	        	 * 或者
    	        	 * HttpClient httpClient = new DefaultHttpClient();
    	        	 * DefaultHttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(0, false);
					 * ((AbstractHttpClient)httpClient).setHttpRequestRetryHandler(retryHandler);
					 * 
					 * apache官网的教程(tutorial)http://hc.apache.org/httpclient-legacy/tutorial.html#Method_recovery
					 * client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
					 * The cast to AbstractHttpClient is not necessary. Another way is to use a strategy with AutoRetryHttpClient with DefaultServiceUnavailableRetryStrategy set to 0 for retry parameter. A better way would be to extend the AbstractHttpClient or implement HttpClient to expose the desired method.
    	        	 */
    	        	
    	        	//commons-httpclient-3.1.jar,可以使用这个禁用retry重试机制,官方教程推荐使用这个
    	        	/* 关于超时重试这个retry,apache官方是这样解释的
    	        	 * http://hc.apache.org/httpclient-legacy/tutorial.html#Method_recovery
    	        	 * Default recovery procedure can be replaced with a custom one. The number of automatic retries can be increased. HttpClient can also be instructed to retry the method even though the request may have already been processed by the server and the I/O exception has occurred while receiving the response. Please exercise caution when enabling auto-retrial. Use it only if the method is known to be idempotent, that is, it is known to be safe to retry multiple times without causing data corruption or data inconsistency.
    	        	 * The rule of thumb is GET methods are usually safe unless known otherwise, entity enclosing methods such as POST and PUT are usually unsafe unless known otherwise.
    	        	 * 默认的恢复过程可以用自定义的来代替。 可以增加自动重试的次数。 即使请求可能已经被服务器处理并且在接收响应时发生了 I/O 异常，也可以指示 HttpClient 重试该方法。 启用自动重试时请小心。 仅当已知该方法具有幂等性时才使用它，即已知多次重试是安全的，而不会导致数据损坏或数据不一致。
    	        	 * 经验法则是 GET 方法通常是安全的，除非另有说明，实体封装方法（如 POST 和 PUT）通常是不安全的，除非另有说明。 
    	        	 * DefaultMethodRetryHandler retryhandler = new DefaultMethodRetryHandler(10, true);
    	        	 * client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler);
    	        	 */
    	        	DefaultHttpMethodRetryHandler dhmr = new DefaultHttpMethodRetryHandler(0, false);
    	        	httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, dhmr);
    	        	
    	        	//commons-httpclient-3.1.jar,也可以使用这个禁用retry重试机制
    	        	DefaultMethodRetryHandler dh = new DefaultMethodRetryHandler();
    	        	dh.setRequestSentRetryEnabled(false);//禁用重试机制
    	        	dh.setRetryCount(0);//重试次数设置为0,DefaultMethodRetryHandler这个类默认的重试次数为3
    	        	httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, dh);
    	        } 
    		}
    	}
        return httpClient;  
    }
    
    //apache的commons-httpclient-3.1.jar的httpclient发送post请求的用法
    public static String sendInfoToCarFlat(Map<String,String> httpMap, byte[] sendXML) throws HttpException, IOException {
		long startTime = System.currentTimeMillis();
		PostMethod method = null;
		String returnXml = "";
		String url = "";
		String httpName = "";
		String encoding = "";
		String sendStr = "";
		int stats = 0;
		String charSet = "";
		try {
			httpName = ObjectUtils.toString(httpMap.get("name"));//调用的http接口名字
			url = ObjectUtils.toString(httpMap.get("url"));
			
			//送报文采用线程安全httpclient连接池来处理 性能提升3倍左右 初始化50个连接
			HttpClient client = HttpClientUtil.getSaveHttpClient();
			
			//添加编码格式判断
			encoding = StringUtils.defaultIfEmpty(httpMap.get("encoding"), "GBK");
			
			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, encoding);
			//add by wlsun date 2017-3-1 10:18:29 desc 新增调用时自定义设计请求返回超时时间，因存在部分接口平台执行过长，如手续费打包
			if (httpMap.get("HTTPCLIENT_CONNECTIONTIMEOUT") != null && httpMap.get("HTTPCLIENT_SOTIMEOUT") != null) {
				HttpConnectionManagerParams managerParams = client.getHttpConnectionManager().getParams();
				managerParams.setConnectionTimeout(Integer.parseInt(httpMap.get("HTTPCLIENT_CONNECTIONTIMEOUT")));
				managerParams.setSoTimeout(Integer.parseInt(httpMap.get("HTTPCLIENT_SOTIMEOUT")));
			}
			
			sendStr = new String(sendXML, encoding);
			RequestEntity entity = new ByteArrayRequestEntity(sendXML);
			
			method = new PostMethod(url);
			//http://www.360doc.com/content/11/0419/11/2104556_110714345.shtml
			method.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);//设置PostMethod方法的超时时间
			//DefaultMethodRetryHandler和setMethodRetryHandler这个俩个东西过期了,不建议使用。http://www.360doc.com/content/11/0419/11/2104556_110714345.shtml
//			DefaultMethodRetryHandler dmrh = new DefaultMethodRetryHandler();
//			dmrh.setRetryCount(0);//禁用重试retry机制,重试次数设置为0
//			dmrh.setRequestSentRetryEnabled(false);
//			method.setMethodRetryHandler(dmrh);//setMethodRetryHandler这个方法过期了,建议使用HttpMethodParams这个类
			//使用HttpMethodParams这个类,设置重试retry机制,https://stackoverflow.com/questions/43732384/apache-httpclient-3-1-socket-timeout
			method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(0, false));
			String headerStr = ObjectUtils.toString(httpMap.get("Content-Type"));//指定的http的header类型
			if (StringUtils.isNotBlank(headerStr)) {//钉钉机器人要求请求header必须为application/json,https://developers.dingtalk.com/document/app/custom-robot-access
				method.setRequestHeader("Content-Type", headerStr);
			}
			
			method.setRequestEntity(entity);
			
			startTime = System.currentTimeMillis();
			stats = client.executeMethod(method);
			long endTime = System.currentTimeMillis() - startTime;
			if (stats == HttpStatus.SC_OK) {
				charSet = method.getResponseCharSet();
				//传给平台的是什么编码格式,返回的也是什么编码格式
				byte[] body = method.getResponseBody();
				returnXml = new String(body, encoding);
				//String response = method.getResponseBodyAsString();
			} else {
				System.out.println("http服务有问题,返回状态为stats:" + stats);
			}
		} finally {
			if (method != null) {
				method.releaseConnection();
			}
		}
		return returnXml;
    }
    
    /**
     * apache的httpclient-4.5.9.jar和httpcore-4.4.11.jar的httpclient用法
     * https://stackoverflow.com/questions/49561248/how-to-set-default-timeout-in-apache-httpclient-4-5-fluent
     * I'm using Apache HttpClient 4.5 (with fluent interface) in a Java console application. I noticed, that its default timeout value seem to be infinite, 
     * but i have to use a non-infinite timeout value for the requests i send. And i would like to use the same timeout value for all requests.
     * 我在 Java 控制台应用程序中使用 Apache HttpClient 4.5（具有流畅的界面）。 我注意到，它的默认超时值似乎是无限的，但我必须为我发送的请求使用非无限超时值。 我想对所有请求使用相同的超时值。 
     * How do i globally set the default connection-timeout and socket-timeout values, so that i do not have to set them every place in the code where i send a request? (Remember i use the fluent interface)
     * 我如何全局设置默认的连接超时和套接字超时值，以便我不必在代码中发送请求的每个地方都设置它们？ （记得我用的是流畅的界面） 
     * Right now, each place in my code where i send a request i do something like: (simple example)
     * HttpResponse response = Request.Get(url)
   .connectionTimeout(CONNECTION_TIMEOUT) // <- want to get rid of this
   .sessionTimeout(SESSION_TIMEOUT)       // <- and this
   .execute()
   .returnResponse();
     * What i would like to do, is to set the default value once and for all, at the beginning of my program. Something like:
     * 我想做的是在我的程序开始时一劳永逸地设置默认值。 就像是：
     * SomeImaginaryConfigClass.setDefaultConnectionTimeout(CONNECTION_TIMEOUT);
     * SomeImaginaryConfigClass.setDefaultSessionTimeout(SESSION_TIMEOUT);
     * so that i can just send a request like this
     * 这样我就可以发送这样的请求 
     * HttpResponse response = Request.Get(url).execute().returnResponse();
     * without setting the timeout parameters on every single call.
     * 无需在每次调用时设置超时参数。 
     * I have seen some answers on the net, but they're either for old versions of Apache HttpClient (i.e. doesn't work), or they talk about using a builder or passing a config class along or other approaches way too complicated for my needs. 
     * I just want to set the default timeout values, nothing fancier than that. Where do i do this?
     * 我在网上看到了一些答案，但它们要么是针对旧版本的 Apache HttpClient（即不起作用），要么是谈论使用构建器或传递配置类或其他方法对于我的需求来说太复杂了 . 我只想设置默认超时值，没有什么比这更好的了。 我在哪里做这个？ 
     * @return
     */
    public static HttpClient getSaveHttpClient459(){  
    	//One could use a custom Executor to do so可以使用自定义 Executor 来执行此操作 
    	RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000)
    	        .setSocketTimeout(5000).build();
    	SocketConfig socketConfig = SocketConfig.custom()
    	        .setSoTimeout(5000)
    	        .build();
    	CloseableHttpClient client = HttpClients.custom()
    	        .setDefaultRequestConfig(requestConfig)
    	        .setDefaultSocketConfig(socketConfig)
    	        .build();
    	//Executor executor = Executor.newInstance(client);
    	//executor.execute(Request.Get("http://thishost/")).returnResponse();
    	//executor.execute(Request.Get("http://thathost/")).returnResponse();
    	
    	//httpcore-4.4.11.jar
//    	HttpResponse response = Request.Get(url)
//    			   .connectionTimeout(CONNECTION_TIMEOUT) // <- want to get rid of this
//    			   .sessionTimeout(SESSION_TIMEOUT)       // <- and this
//    			   .execute()
//    			   .returnResponse();
        return httpClient;  
    }
    
    
    /**
     * 超时时间不起作用
     * https://stackoverflow.com/questions/21576414/setting-time-out-in-apache-http-client
     * I'm using Apache http client 4.3.2 to send get requests. What I have done is:
     * 我正在使用 Apache http 客户端 4.3.2 发送获取请求。 我所做的是： 
     * And when sending request:当发送请求时： 
     * @return
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public static HttpClient getSaveHttpClient432() throws ClientProtocolException, IOException{  
    	RequestConfig requestConfig = RequestConfig.custom()
    	        .setConnectTimeout(1000)
    	        .setConnectionRequestTimeout(1000)
    	        .setSocketTimeout(1000)
    	        .build();
    	//httpclient-4.5.9.jar
    	org.apache.http.client.HttpClient client = HttpClientBuilder.create()
    	        .disableAuthCaching()
    	        .disableAutomaticRetries()
    	        .disableConnectionState()
    	        .disableContentCompression()
    	        .disableCookieManagement()
    	        .disableRedirectHandling()
    	        .setDefaultRequestConfig(requestConfig)
    	        .build(); 
    	/*
    	 * And when sending request:当发送请求时： 
    	 * But some of my requests still takes long time to timeout. This is stack trace of exception:
    	 * 但是我的一些请求仍然需要很长时间才能超时。 这是异常的堆栈跟踪： 
    	 * Is there any other time out value I should set? What am I doing wrong?
    	 * 我应该设置任何其他超时值吗？ 我究竟做错了什么？ 
    	 * I have the same problem, request.viaProxy(proxy).connectTimeout(1000).socketTimeout(1000).execute().returnContent().asString(StandardCharsets.UTF_8); will take even 1 munites!
    	 * 我有同样的问题， request.viaProxy(proxy).connectTimeout(1000) .socketTimeout(1000).execute() .returnContent().asString(StandardCharsets.UTF_8); 甚至会占用 1分钟！ 
    	 */
    	HttpGet request = null;
    	try {
    	    request = new HttpGet("url");
    	    if (client.execute(request).getStatusLine().getStatusCode() == 200) {
    	        /* do some work here */
    	    }
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    	    if (request != null) {
    	        request.releaseConnection();
    	    }
    	}
    	
    	/**
    	 * When I had this problem I changed my request to configure the timeout on each request.
    	 * 当我遇到这个问题时，我改变了我的请求来配置每个请求的超时时间。 
    	 * It worked fine.它工作得很好。 
    	 */
    	HttpRequestBase request1 = new HttpGet("url"); //or HttpPost
    	RequestConfig.Builder requestConfig1 = RequestConfig.custom();
    	requestConfig1.setConnectTimeout(30 * 1000);
    	requestConfig1.setConnectionRequestTimeout(30 * 1000);
    	requestConfig1.setSocketTimeout(30 * 1000);
    	request1.setConfig(requestConfig1.build());
    	CloseableHttpResponse response = (CloseableHttpResponse)client.execute(request1);
    	
    	//Here is a code snippet example to achieve the goal :这是以类型安全和可读的方式配置所有三个超时的推荐方法。 
    	//That is the recommended way of configuring all three timeouts in a type-safe and readable manner.
    	//这是以类型安全和可读的方式配置所有三个超时的推荐方法。 
    	//Here is the full detail(https://www.baeldung.com/httpclient-timeout)
    	int timeout = 5;
    	RequestConfig config = RequestConfig.custom()
    	  .setConnectTimeout(timeout * 1000)
    	  .setConnectionRequestTimeout(timeout * 1000)
    	  .setSocketTimeout(timeout * 1000).build();
    	CloseableHttpClient client1 =  HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        return httpClient;  
    }
    
    /**
     * https://stackoverflow.com/questions/6764035/apache-httpclient-timeout
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public static HttpClient getSaveHttpClient43() throws ClientProtocolException, IOException{  
    	/*
    	 * Is there a way to specify a timeout for the whole execution of HttpClient?有没有办法为 HttpClient 的整个执行指定超时？
    	 * I have tried the following:我尝试了以下方法：
    	 * httpClient.getParams().setParameter("http.socket.timeout", timeout * 1000);
		 * httpClient.getParams().setParameter("http.connection.timeout", timeout * 1000);
		 * httpClient.getParams().setParameter("http.connection-manager.timeout", new Long(timeout * 1000));
		 * httpClient.getParams().setParameter("http.protocol.head-body-timeout", timeout * 1000);
		 * It actually works fine, except if a remote host sends back data - even at one byte/second - it will continue to read forever! 
		 * But I want to interrupt the connection in 10 seconds max, whether or not the host responds.
		 * 它实际上工作正常，除非远程主机发回数据 - 即使是一字节/秒 - 它会永远继续读取！ 但是我想在最多 10 秒内中断连接，无论主机是否响应。 
		 * Femi 回答：
		 * There is currently no way to set a maximum request duration of that sort: basically you want to say I don't care whether or not any specific request stage times out, but the entire request must not last longer than 15 seconds (for example).
		 * Your best bet would be to run a separate timer, and when it expires fetch the connection manager used by the HttpClient instance and shutdown the connection, which should terminate the link. Let me know if that works for you.
    	 * 目前没有办法设置这种类型的最大请求持续时间：基本上你想说我不在乎任何特定的请求阶段是否超时，但整个请求不能持续超过 15 秒（例如） .
    	 * 最好的办法是运行一个单独的计时器，当它到期时获取 HttpClient 实例使用的连接管理器并关闭连接，这应该终止链接。 让我知道这是否适合您。
    	 * 另一个回答：
    	 * For a newer version of httpclient (e.g. http components 4.3对于较新版本的 httpclient（例如 http components 4.3 
    	 * int CONNECTION_TIMEOUT_MS = timeoutSeconds * 1000; // Timeout in millis.
		 *	RequestConfig requestConfig = RequestConfig.custom()
			    .setConnectionRequestTimeout(CONNECTION_TIMEOUT_MS)
			    .setConnectTimeout(CONNECTION_TIMEOUT_MS)
			    .setSocketTimeout(CONNECTION_TIMEOUT_MS)
			    .build();
		 *	HttpPost httpPost = new HttpPost(URL);
		 *	httpPost.setConfig(requestConfig);
		 *
		 * 另一个回答:Works fine, as proposed by Femi. Thanks! 工作正常，正如 Femi 所提议的那样。 谢谢！ 
		 * Timer timer = new Timer();
			timer.schedule(new TimerTask() {
			    public void run() {
			        if(getMethod != null) {
			            getMethod.abort();
			        }
			    }
			}, timeout * 1000);
		 * needs a lock, to avoid NPE. getMethod can become null between the check and the call to abort需要锁，以避免 NPE。 getMethod 可以在检查和调用 abort 之间变为 null！ 
		 * can you show an example of how to add this lock?你能举例说明如何添加这个锁吗？ 
		 * synchronized(foo){ ... getMethod=null; } synchronized(foo){ if (getMethod!=null){ getMethod.abort(); } }
		 * thanks for that. I'm new to synchronization. The section of getMethod=null; is just an example of the method where the HttpClient is being used correct? That is, getMethod wouldn't deliberately get set to null, but might become null when that method exits, therefore it should be synchronized (on the same object) as the Timer thread.
		 * 感谢你的举例。 我是同步的新手。 getMethod=null 部分； 只是正确使用 HttpClient 的方法的一个示例？ 也就是说，getMethod 不会故意设置为 null，但在该方法退出时可能会变为 null，因此它应该与 Timer 线程同步（在同一对象上）。 
		 * This solution is not right for prod. Keep in in mind each timer will spin a thread. 1000/req per minute will spin 1000 thread a min. This will continue to pile up until you service die due to out of memory。
		 * 此解决方案不适合生产。 请记住，每个计时器都会旋转一个线程。 每分钟 1000/req 将每分钟旋转 1000 个线程。 这将继续堆积，直到您因内存不足而死 
		 * 另一个回答：
		 * Timer is evil! Using timer or executor or any other mechanism which creates a thread/runnable object per request is a very bad idea.
		 * Please think wisely and don't do it. Otherwise you will quickly run into all kind of memory issues with more or less real environment. 
		 * Imagine 1000 req/min means 1000 threads or workers / min. Poor GC. 
		 * The solution I propose require only 1 watchdog thread and will save you resources time and nerves. Basically you do 3 steps.
		 * 定时器是邪恶的！ 使用计时器或执行程序或任何其他机制为每个请求创建一个线程/可运行对象是一个非常糟糕的主意。 请慎重考虑，不要这样做。 否则，您将很快在或多或少的真实环境中遇到各种内存问题。 
		 * 想象一下 1000 req/min 意味着 1000 个线程或工人/分钟。 可怜的GC。 
		 * 我提出的解决方案只需要 1 个看门狗线程，将节省您的资源时间和精力。 基本上你做3个步骤。 
		 * 1.put request in cache.  将请求放入缓存中。
		 * 2.remove request from cache when complete.完成后从缓存中删除请求。
		 * 3.abort requests which are not complete within your limit.中止在您的限制内未完成的请求。 
		 * your cache along with watchdog thread may look like this.您的缓存和看门狗线程可能如下所示。 
			import org.apache.http.client.methods.*;
			import java.util.*;
			import java.util.concurrent.*;
			import java.util.stream.*;
			
			public class RequestCache {
			
			private static final long expireInMillis = 300000;
			private static final Map<HttpUriRequest, Long> cache = new ConcurrentHashMap<>();
			private static final ScheduledExecutorService exe = Executors.newScheduledThreadPool(1);
			
			static {
			    // run clean up every N minutes
			    exe.schedule(RequestCache::cleanup, 1, TimeUnit.MINUTES);
			}
			
			public static void put(HttpUriRequest request) {
			    cache.put(request, System.currentTimeMillis()+expireInMillis);
			}
			
			public static void remove(HttpUriRequest request) {
			    cache.remove(request);
			}
			
			private static void cleanup() {
			    long now = System.currentTimeMillis();
			    // find expired requests
			    List<HttpUriRequest> expired = cache.entrySet().stream()
			            .filter(e -> e.getValue() > now)
			            .map(Map.Entry::getKey)
			            .collect(Collectors.toList());
			
			    // abort requests
			    expired.forEach(r -> {
			        if (!r.isAborted()) {
			            r.abort();
			        }
			        cache.remove(r);
			      });
			    }
			  }
		 * and the following sudo code how to use cache 以及以下 sudo 代码如何使用缓存 
		 * import org.apache.http.client.methods.*;
			public class RequestSample {
			public void processRequest() {
			    HttpUriRequest req = null;
			    try {
			        req = createRequest();
			
			        RequestCache.put(req);
			
			        execute(req);
			
			    } finally {
			        RequestCache.remove(req);
			    }
			  }
			}
		 * Building off the the other answers, my solution was to use a HttpRequestInterceptor to add the abort runnable to every request. Also i swapped out the Timer for a ScheduledExecutorService.
		 * 基于其他答案，我的解决方案是使用 HttpRequestInterceptor 将 abort runnable 添加到每个请求。 我还把 Timer 换成了 ScheduledExecutorService。 
		 * public class TimeoutInterceptor implements HttpRequestInterceptor {
			private int requestTimeout = 1 * DateTimeConstants.MILLIS_PER_MINUTE;
			
			private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			
			public TimeoutInterceptor() {  }
			
			public TimeoutInterceptor(final int requestTimeout) {
			    this.requestTimeout = requestTimeout;
			}
			
			@Override
			public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
			    if (request instanceof AbstractExecutionAwareRequest) {
			        final AbstractExecutionAwareRequest abortableRequest = (AbstractExecutionAwareRequest) request;
			        setAbort(abortableRequest);
			    } else if (request instanceof HttpRequestWrapper) {
			        HttpRequestWrapper wrapper = (HttpRequestWrapper) request;
			        this.process(wrapper.getOriginal(), context);
			    }
			
			}

    	 * @param abortableRequest
		private void setAbort(final AbstractExecutionAwareRequest abortableRequest) {
		    final SoftReference<AbstractExecutionAwareRequest> requestRef = new SoftReference<AbstractExecutionAwareRequest>(abortableRequest);
		
		    executorService.schedule(new Runnable() {
		
		        @Override
		        public void run() {
		            AbstractExecutionAwareRequest actual = requestRef.get();
		            if (actual != null && !actual.isAborted()) {
		                actual.abort();
		            }
		        }
		    }, requestTimeout, TimeUnit.MILLISECONDS);
		
		}
		
		public void setRequestTimeout(final int requestTimeout) {
		    this.requestTimeout = requestTimeout;
		}
		}
		 * This is working solution but you need create a runnable per request. That quickly turns into a problem on real high volume env. 1000 req per min will result in 1000 runnables per min. Runnables will be queued up. Indeed you will run into various problems causing service failure. 
		 * 这是有效的解决方案，但您需要为每个请求创建一个可运行的。 这很快就变成了真正高容量环境的问题。 每分钟 1000 次请求将导致每分钟 1000 次可运行。 Runnable 将排队。 事实上，您会遇到各种导致服务失败的问题。 
		 * 另一个回答：
		 * In HttpClient 4.3 version you can use below example.. let say for 5 seconds 在 HttpClient 4.3 版本中，您可以使用以下示例.. 说 5 秒 
		 * int timeout = 5;
			RequestConfig config = RequestConfig.custom()
			  .setConnectTimeout(timeout * 1000)
			  .setConnectionRequestTimeout(timeout * 1000)
			  .setSocketTimeout(timeout * 1000).build();
			CloseableHttpClient client = 
			  HttpClientBuilder.create().setDefaultRequestConfig(config).build();
			HttpGet request = new HttpGet("http://localhost:8080/service"); // GET Request
			response = client.execute(request);
    	 */
        return httpClient;  
    }
    
    static {
    	/**
    	 * https://www.baeldung.com/httpclient-timeout
    	 * While setting timeouts on establishing the HTTP connection and not receiving data is very useful, sometimes we need to set a hard timeout for the entire request.
    	 * 虽然在建立 HTTP 连接和不接收数据时设置超时非常有用，但有时我们需要为整个请求设置硬超时。 
    	 * For example, the download of a potentially large file fits into this category. In this case, the connection may be successfully established, data may be consistently coming through, but we still need to ensure that the operation doesn't go over some specific time threshold.
    	 * HttpClient doesn't have any configuration that allows us to set an overall timeout for a request; it does, however, provide abort functionality for requests, so we can leverage that mechanism to implement a simple timeout mechanism:
    	 * 例如，下载一个潜在的大文件就属于这一类。 在这种情况下，连接可能会成功建立，数据可能会一直通过，但我们仍然需要确保操作不会超过某个特定的时间阈值。
    	 * HttpClient 没有任何配置允许我们为请求设置总体超时； 然而，它确实为请求提供了中止功能，因此我们可以利用该机制来实现一个简单的超时机制： 
    	 * HttpGet getMethod = new HttpGet("http://localhost:8080/httpclient-simple/api/bars/1");
			int hardTimeout = 5; // seconds
			TimerTask task = new TimerTask() {
			    @Override
			    public void run() {
			        if (getMethod != null) {
			            getMethod.abort();
			        }
			    }
			};
			new Timer(true).schedule(task, hardTimeout * 1000);
			
			HttpResponse response = httpClient.execute(getMethod);
			System.out.println(
			  "HTTP Status of response: " + response.getStatusLine().getStatusCode());
		 * We're making use of the java.util.Timer and java.util.TimerTask to set up a simple delayed task which aborts the HTTP GET request after a 5 seconds hard timeout.
		 * 我们正在使用 java.util.Timer 和 java.util.TimerTask 来设置一个简单的延迟任务，该任务在 5 秒硬超时后中止 HTTP GET 请求。 
		 * Timeout and DNS Round Robin – Something to Be Aware Of 超时和 DNS 循环 - 需要注意的事项 
		 * It's quite common that some larger domains will be using a DNS round robin configuration – essentially having the same domain mapped to multiple IP addresses. This introduces a new challenge for a timeout against such a domain, simply because of the way HttpClient will try to connect to that domain that times out:
		 * 一些较大的域将使用 DNS 循环配置是很常见的——本质上是将相同的域映射到多个 IP 地址。 这为针对此类域的超时引入了新挑战，这仅仅是因为 HttpClient 将尝试连接到超时的域的方式： 
		 * HttpClient gets the list of IP routes to that domain
			it tries the first one – that times out (with the timeouts we configure)
			it tries the second one – that also times out
			and so on …
		 * HttpClient 获取到该域的 IP 路由列表
			它尝试第一个 - 超时（使用我们配置的超时）
			它尝试第二个——也超时了
			等等 … 
		 * So, as you can see – the overall operation will not time out when we expect it to. Instead – it will time out when all the possible routes have timed out. What's more – this will happen completely transparently for the client (unless you have your log configured at the DEBUG level).
		 * 因此，如您所见 - 整个操作不会在我们预期的时候超时。 相反 - 当所有可能的路由都超时时，它就会超时。 更重要的是——这对客户端来说是完全透明的（除非你在 DEBUG 级别配置了日志）。 
		 * Here's a simple example you can run and replicate this issue:这是一个简单的示例，您可以运行并复制此问题： 
		 * int timeout = 3;
			RequestConfig config = RequestConfig.custom().
			  setConnectTimeout(timeout * 1000).
			  setConnectionRequestTimeout(timeout * 1000).
			  setSocketTimeout(timeout * 1000).build();
			CloseableHttpClient client = HttpClientBuilder.create()
			  .setDefaultRequestConfig(config).build();
			
			HttpGet request = new HttpGet("http://www.google.com:81");
			response = client.execute(request);
		 * You will notice the retrying logic with a DEBUG log level:您会注意到具有 DEBUG 日志级别的重试逻辑： 
		 * DEBUG o.a.h.i.c.HttpClientConnectionOperator - Connecting to www.google.com/173.194.34.212:81
			DEBUG o.a.h.i.c.HttpClientConnectionOperator - 
			 Connect to www.google.com/173.194.34.212:81 timed out. Connection will be retried using another IP address
			
			DEBUG o.a.h.i.c.HttpClientConnectionOperator - Connecting to www.google.com/173.194.34.208:81
			DEBUG o.a.h.i.c.HttpClientConnectionOperator - 
			 Connect to www.google.com/173.194.34.208:81 timed out. Connection will be retried using another IP address
			
			DEBUG o.a.h.i.c.HttpClientConnectionOperator - Connecting to www.google.com/173.194.34.209:81
			DEBUG o.a.h.i.c.HttpClientConnectionOperator - 
			 Connect to www.google.com/173.194.34.209:81 timed out. Connection will be retried using another IP address
		 * Conclusion 结论 
		 * This tutorial discussed how to configure the various types of timeouts available for an HttpClient. It also illustrated a simple mechanism for hard timeout of an ongoing HTTP connection.
		 * 本教程讨论了如何配置 HttpClient 可用的各种类型的超时。 它还说明了一个简单的机制，用于对正在进行的 HTTP 连接进行硬超时。
		 * The implementation of these examples can be found in the GitHub project.
		 * 这些示例的实现可以在 GitHub 项目中找到。 https://github.com/eugenp/tutorials/tree/master/httpclient-simple
    	 */
    }
    
    
    
    /**
     * https://blog.csdn.net/sinat_24928447/article/details/53390052 HttpInvoker提升效率 解决超时问题
     * 最近接手服务器总被人质疑效率问题，说到底是质疑Spring HttpInvoke的效率问题。好在经过同事们的努力，找到了问题的根源，最终解决了这个问题。 我也顺道整理一下Spring HttpInvoke——那曾经最为熟悉的东西。 
     * Spring HttpInvoke，一种较为常用的、基于Spring架构的服务器之间的远程调用实现，可以说是轻量级的RMI，RMI是Remote Method Invocation的缩写,关于RMI可以参考(com.yale.test.net.server.rmi.Client.java)。最初，我们使用Spring HttpInvoke同步配置数据，刷新多个服务器上的缓存，当然如果用分布式缓存是不是更好   ！ 
     * 使用Spring HttpInvoke，你可以调用远程接口，进行数据交互、业务逻辑操作等等。 废话不说了，上代码！ 用户操作接口： IHttpInvokeTest.class
     * 用户类，注意实现 Serializable 接口，这是执行远程调用传递数据对象的第一要求——数据对象必须实现 Serializable 接口，因为，要执行序列化/反序列化操作！  覆盖toString()方法，输出用户信息！ 
     * 再看UserServiceImpl实现： 只把用户信息打出来即可说明调用效果！ 
     * 看applicationContext.xml 
     * <bean id="userService" class="org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter">  
		    <property name="service">我们要把userService暴露出去，这样外部就可以通过http接口调用这个接口的实现了。 说说HttpInvokerServiceExporter，这个类用来在服务器端包装需要暴露的接口。 熟悉service，定义具体的实现类！ 
		        <bean class="org.zlex.spring.httpinvoke.service.impl.UserServiceImpl" />  
		    </property>  熟悉serviceInterface指向需要暴露的接口， 注意使用value标注接口名称！  
		    <property name="serviceInterface" value="org.zlex.spring.httpinvoke.service.UserService" />  
		</bean> 
	 * 最后再看servlet.xml配置:
	 * <bean class="org.springframework.web.servlet.handler.SimpleUrlHandlerMapping">  
		    <property name="urlMap">  
		        <map>  
		            <entry key="*" value-ref="userService" />  
		        </map>  
		    </property>  
		</bean>
	 * 直接将请求指向刚才配置的userService 现在我们之间访问一下http://localhost:8080/spring/service/ 
	 * 05.jpg
	 * 这就说明，服务器端配置已经成功了！如果在日志中频繁得到这种异常，那很可能服务器被恶意访问了！ 
	 * 再看客户端实现：
	 * 我们做了什么？Nothing！就跟调用一般Spring容器中的实现一样！ 再看applicationContext.xml： 
	 * <bean id="userService" class="org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean">  
		    <property  
		        name="serviceUrl"  
		        value="http://localhost:8080/spring/service" />  
		    <property  
		        name="serviceInterface"  
		        value="org.zlex.spring.httpinvoke.service.UserService" />  
		</bean>
	 * 这里我们可以通过Spring容器调用userService，而实际上，他是一个 HttpInvokerProxyFactoryBean ，在这个配置里，定义了访问地址serviceUrl，和访问接口serviceInterface。
	 * 执行测试！
	 * 22432c90-4045-3223-bd4b-10c6f55602f2.jpg 注:我自己要看下这些日志是怎么打印出来的,Spring自己打印的？
	 * 如果我们这样写，其实默认调用了 SimpleHttpInvokerRequestExecutor 做实现，这个实现恐怕只能作为演示来用！ 这也是效率问题所在！！！ 为提高效率，应该通过Commons-HttpClient！ 我们需要做什么？导入这个jar，改改xml就行！
		<bean  
		    id="userService"  
		    class="org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean">  
		    <property  
		        name="serviceUrl"  
		        value="http://localhost:8080/spring/service" />  
		    <property  
		        name="serviceInterface"  
		        value="org.zlex.spring.httpinvoke.service.UserService" />  
		    <property  
		        name="httpInvokerRequestExecutor">  
		        <ref  
		            bean="httpInvokerRequestExecutor" />  
		    </property>  
		</bean>  
		<bean  
		    id="httpInvokerRequestExecutor" class="org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor">  
		    <property  
		        name="httpClient">  
		        <bean  
		            class="org.apache.commons.httpclient.HttpClient">  
		            <property  
		                name="connectionTimeout"  
		                value="2000" />  
		            <property  
		                name="timeout"  
		                value="5000" />  
		        </bean>  
		    </property>  
		</bean>  
	 * 通过HttpClient，我们可以配置超时时间timeout和连接超时connectionTimeout两个属性，这样，服务器执行操作时，如果超时就可以强行释放连接，这样可怜的tomcat不会因为HttpInvoke连接不释放而被累死！   回头看了一眼我N多年前的代码，万岁，我当时确实是这么实现的！好在没有犯低级错误！！！ 
	 * 这时，转为org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor实现了！ 不过同事认为，这个效率还是不够高！！！ 再改，改什么？还是xml！ 执行操作！ 
	 * 01.jpg注:我自己要看下这些日志是怎么打印出来的,Spring自己打印的？
	 * <bean  
        id="httpInvokerRequestExecutor"  
        class="org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor">  
        <property  
            name="httpClient">  
            <bean  
                class="org.apache.commons.httpclient.HttpClient">  
                <property  
                    name="connectionTimeout"  
                    value="2000" />  
                <property  
                    name="timeout"  
                    value="5000" />  
                <property  
                    name="httpConnectionManager">  
                    <ref  
                        bean="multiThreadedHttpConnectionManager" />  
                </property>  
            </bean>  
        </property>  
    </bean>  
    <bean  
        id="multiThreadedHttpConnectionManager"  
        class="org.apache.commons.httpclient.MultiThreadedHttpConnectionManager">  
        <property  
            name="params">  
            <bean  
                class="org.apache.commons.httpclient.params.HttpConnectionManagerParams">  
                <property  
                    name="maxTotalConnections"  
                    value="600" />  
                <property  
                    name="defaultMaxConnectionsPerHost"  
                    value="512" />  
            </bean>  
        </property>  
    </bean>  
     * 改用 MultiThreadedHttpConnectionManager ，多线程！！！ 测试就不说了，实践证明： 默认实现，服务器平均10s左右才能响应一个请求。 多线程实现，服务器平均20ms左右响应一个请求。 这简直不是一个数量级！！！ 
     * 注意：在HttpClient的3.1版本中，已不支持如下配置，相应的方法已经废弃！  
     * 如果仔细看看文档， 引用HttpClient that uses a default MultiThreadedHttpConnectionManager.
     * commons 系列的实现怎么会不考虑多线程呢？人家默认实现就是多线程的！同事多虑了！ 当然，同事还补充了一句，需要控制连接数！ 
     * 难怪，这里要设置，默认啥情况？
     * maxConnectionsPerHost 每个主机的最大并行链接数，默认为2 
     * public static final int DEFAULT_MAX_HOST_CONNECTIONS = 2; 
     * maxTotalConnections 客户端总并行链接最大数，默认为20  
     * public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20; 
     * 3.1版本，俩个超时时间已经移到params里面了
client.getHttpConnectionManager().getParams() 
            .setConnectionTimeout(3000); 
    client.getHttpConnectionManager().getParams().setSoTimeout(3000); 
     * https://www.iteye.com/blog/snowolf-703732
     * @param url
     * @param requestXml
     * @return
     * @throws Exception
     */
    public String httpInvokerProxyHelp(String url, String requestXml) throws Exception {
    	HttpInvokerProxyFactoryBean httpInvokerProxy = new HttpInvokerProxyFactoryBean();
		
    	//CommonsHttpInvokerRequestExecutor是spring_2.5.6.jar这个jar包里面的org.springframework.remoting.httpinvoker.CommonsHttpInvokerRequestExecutor
    	//CommonsHttpInvokerRequestExecutor hkre = new CommonsHttpInvokerRequestExecutor();
    	//hkre.setHttpClient(HttpClientUtil.getSaveHttpClient());//设置超时时间
    	HttpComponentsHttpInvokerRequestExecutor hkre = new HttpComponentsHttpInvokerRequestExecutor();
		hkre.setHttpClient(null);//设置超时时间,传进来一个HttpClient,然后HttpClient设置好超时时间就行了
		httpInvokerProxy.setHttpInvokerRequestExecutor(hkre);
		httpInvokerProxy.setServiceUrl(url);
		httpInvokerProxy.setServiceInterface(IHttpInvokeTest.class);
		httpInvokerProxy.afterPropertiesSet();
		IHttpInvokeTest httpInvokeTest = (IHttpInvokeTest) httpInvokerProxy.getObject();	
		String retXml = httpInvokeTest.invoke(requestXml);
		return retXml;
    }
    
    
    /**
     * 速保科技的地址用其他的httpPost连接不了,需要证书什么的，这个方法跳过证书去连接 <br>
     * javax.net.ssl.SSLException: Server key
     * at com.sun.net.ssl.internal.ssl.Handshaker.throwSSLException(Handshaker.java:1139)
     * java.security.spec.InvalidKeySpecException: key spec not recognised
     * cfca.sadk.org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi.engineGeneratePublic(BaseKeyFactorySpi.java:36)
     * javax.net.ssl.SSLException: Server key 这个报错需要检查服务器上面JDK安装目录jre/lib/ext/这个目录下有没有bcprov-jdkxxx-xxx.jar这种格式的jar包
     * /jre/lib/security/java.security这个文件里面有没有security.provider.10=org.bouncycastle.jce.provider.BouncyCastleProvider 这样的配置
     * java.lang.RuntimeException: Could not generate DH keypair 还有这个报错也是因为JDK的配置问题.https://my.oschina.net/syso4yy/blog/2050271
     * Caused by: java.security.InvalidAlgorithmParameterException: parameter object not a ECParameterSpec 还有这个报错也是因为JDK的配置问题.https://my.oschina.net/syso4yy/blog/2050271
     * https://www.v2ex.com/t/748798#reply5
     * https://club.perfma.com/question/2195295
     * 你本地根据你的描述就没有用BC作为加密provider，所以没问题，用的是sun的JCE，其实我个人觉得JDK8以后都推荐使用sun JCE，不需要额外使用BC了，基本主流加解密都支持的了
     * https://ask.csdn.net/questions/7397562
     * https://www.oschina.net/question/2719528_2321032
     * https://stackoverflow.com/questions/24810805/invalidkeyexception-key-spec-not-recognised
     * 创建时间：2020年5月8日16:50:50 </pre>
     * @param 参数类型 参数名 说明
     * @return String 说明
     * @throws BusinessServiceException 
     * @throws 异常类型 说明
     */
    public String sendHttpsAndSkipCertificate(Map<String,String> httpMap, String jsonStr) throws Exception {
    	long startTime = 0L;
    	long endTime = 0L;
        String result = null;// 返回的结果
        CloseableHttpResponse response = null;
        CloseableHttpClient client = null;
        HttpPost httpPost = new HttpPost(httpMap.get("url")); //创建HttpPost对象  
        // 参数不为空
        if(StringUtils.isNotBlank(jsonStr)) {
            try {
    			//包装成一个Entity对象
    	        StringEntity entity = new StringEntity(jsonStr, "UTF-8");
    	        //设置请求的内容
    	        httpPost.setEntity(entity);
    	        
    	        
    	        //设置请求的报文头部的编码
    	        httpPost.setHeader(new BasicHeader("Content-Type", "application/json;charset=utf-8"));
    	        //设置期望服务端返回的编码
    	        httpPost.setHeader(new BasicHeader("Accept", "application/json, text/plain, */*"));
    	        
    	        int timeout = Integer.parseInt(StringUtils.defaultIfBlank("15000", "15000"));
    	        //设置连接超时时间30秒
    	        httpPost.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, timeout);
    	        
    	        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setConnectTimeout(timeout).build();
    	        httpPost.setConfig(requestConfig);
    	        //调用方法，创建 CloseableHttpClient 对象
                client = buildSSLCloseableHttpClient();
                startTime = System.currentTimeMillis();
                response = client.execute(httpPost);
                int statusCode = response.getStatusLine().getStatusCode();
                if(HttpURLConnection.HTTP_OK == statusCode) {
                    HttpEntity httpEntity = response.getEntity();  
                    //取出应答字符串 
                    result = EntityUtils.toString(httpEntity); 
                } else {
                	throw new Exception("没有正确连接[" + httpMap.get("name") + "],HTTP服务返回有误,服务返回编码：" + statusCode);
                }
            }  catch (Exception e) {
                endTime = System.currentTimeMillis() - startTime;
                result = e.getMessage();  
                throw e;
            } finally {
    			if (response != null) {
    				try {//关闭response和client
    					response.close();
    				} catch(Exception e) {
    	                endTime = System.currentTimeMillis() - startTime;
    				}
    			}
    			if (client != null) {
    				try {//关闭response和client
    					client.close();
    				} catch(Exception e) {
    	                endTime = System.currentTimeMillis() - startTime;
    				}
    			}
    		}
        }
        return result;
    }
    
    /**
     * buildSSLCloseableHttpClient:(设置允许所有主机名称都可以，忽略主机名称验证)
     * javax.net.ssl.SSLException: Server key
     * at com.sun.net.ssl.internal.ssl.Handshaker.throwSSLException(Handshaker.java:1139)
     * Caused by: java.security.spec.InvalidKeySpecException: key spec not recognised
     * at cfca.sadk.org.bouncycastle.jcajce.provider.asymmetric.util.BaseKeyFactorySpi.engineGeneratePublic(BaseKeyFactorySpi.java:36)
     */
    private static CloseableHttpClient buildSSLCloseableHttpClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
            // 信任所有
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();
        // ALLOW_ALL_HOSTNAME_VERIFIER:这个主机名验证器基本上是关闭主机名验证的,实现的是一个空操作，并且不会抛出javax.net.ssl.SSLException异常。
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }
    
    /**
     * https://blog.csdn.net/zh5220909/article/details/111604877
     * java后端发起https请求时Received fatal alert: protocol_version错误
     * 查了下日志发现大批量的javax.net.ssl.SSLException: Received fatal alert: protocol_version
     * 在Java 1.8上，默认TLS协议是v1.2。在Java 1.6和1.7上，默认是已废弃的TLS1.0
     * 这个原因是我能找到的一个可靠的说法，但这个接口以前就是https的，我认为这个三方的服务接口应该是被做了限制。具体怎样的不得而知
     * 解决办法：平常我们的httpclient请求是这样的
     * public static CloseableHttpResponse postHttp(String url) throws Exception{
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost httpPost = new HttpPost(url);
			CloseableHttpResponse response = null;
			response = httpclient.execute(httpPost);
			return response;
		}
	 *  改为，使用TLSv1.2创建httpclient的方式即可，亲测有效
	 *  public static CloseableHttpResponse getHttp(String url) throws Exception{
			SSLContext ctx = SSLContexts.custom().useProtocol("TLSv1.2").build();
			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(ctx)).build();
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = null;
			response = httpClient.execute(httpGet);
			return response;
		}
     */
}
