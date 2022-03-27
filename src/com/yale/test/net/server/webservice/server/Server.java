package com.yale.test.net.server.webservice.server;

import javax.xml.ws.Endpoint;

/**
 * https://blog.csdn.net/elim168/article/details/78090768
 * 在上述的服务端代码中随机睡眠了5-25秒，而客户端指定的超时时间是15秒，所以在测试的时候你会看到有时候服务调用会超时，有时会正常响应。
 * https://cloud.tencent.com/developer/ask/94978 如何设置JAX-WS Web服务客户端的超时？
 * 我已经使用JAXWS-RI 2.1为基于WSDL的Web服务创建了一个接口。我可以与Web服务交互没有问题，但一直未能指定向Web服务发送请求的超时。如果由于某种原因，它不响应客户端，似乎永远旋转它的轮子。
 * 狩猎已经显示，我应该尝试做这样的事情：
 * ((BindingProvider)myInterface).getRequestContext().put("com.sun.xml.ws.request.timeout", 10000);
 * ((BindingProvider)myInterface).getRequestContext().put("com.sun.xml.ws.connect.timeout", 10000);
 * 我还发现，根据使用的是哪个版本的JAXWS-RI，您可能需要设置这些属性：
 * ((BindingProvider)myInterface).getRequestContext().put("com.sun.xml.internal.ws.request.timeout", 10000);
 * ((BindingProvider)myInterface).getRequestContext().put("com.sun.xml.internal.ws.connect.timeout", 10000);
 * 我的问题是，无论上述哪一项是正确的，我都不知道我可以在哪里做到这一点。我所得到的只是一个Service实现了自动生成的web服务接口的子类，并且在这种情况下，如果WSDL没有响应，那么设置属性已经太迟了：
 * MyWebServiceSoap soap;
 * MyWebService service = new MyWebService("http://www.google.com");
 * soap = service.getMyWebServiceSoap();
 * soap.sendRequestToMyWebService();
 * 任何人都可以指向正确的方向吗？！
 * 我知道这是旧的，并在其他地方回答，但希望这可以关闭。我不确定为什么要动态下载WSDL，但系统属性如下：
 * sun.net.client.defaultConnectTimeout (default: -1 (forever))
 * sun.net.client.defaultReadTimeout (default: -1 (forever))
 * 应该适用于所有读取并使用JAX-WS使用的HttpURLConnection进行连接。如果您从远程位置获取WSDL，这应该可以解决问题 - 但本地磁盘上的文件可能更好！
 * 接下来，如果你想为特定服务设置超时，一旦你创建了你的代理，你需要将它转换为一个BindingProvider（你已经知道），获取请求上下文并设置你的属性。在线的JAX-WS文档是错误的，这些是正确的属性名称（当然，它们适用于我）。
 * MyInterface myInterface = new MyInterfaceService().getMyInterfaceSOAP();
Map<String, Object> requestContext = ((BindingProvider)myInterface).getRequestContext();
requestContext.put(BindingProviderProperties.REQUEST_TIMEOUT, 3000); // Timeout in millis
requestContext.put(BindingProviderProperties.CONNECT_TIMEOUT, 1000); // Timeout in millis
myInterface.callMyRemoteMethodWith(myParameter);
 * 当然，这是一种可怕的做事方式，我会创建一个漂亮的工厂来制作这些绑定提供程序，并可以使用想要的超时注入。
 * 使用一组不同的属性（可在JBoss JAX-WS用户指南中找到https://docs.jboss.org/author/index.html）使其工作：
 * //Set timeout until a connection is established
 * ((BindingProvider)port).getRequestContext().put("javax.xml.ws.client.connectionTimeout", "6000");
 * //Set timeout until the response is received
 * ((BindingProvider) port).getRequestContext().put("javax.xml.ws.client.receiveTimeout", "1000");
 * @author issuser
 */
public class Server {
	public static void main(String[] args) {
		Endpoint.publish("http://localhost:8888/hello", new HelloServiceImpl());
	}
}
