package com.yale.test.net.server.webservice;

import javax.jws.WebService;

/**
 * https://bbs.csdn.net/topics/392248191 webservice客户端设置连接超时时间失效
 * （java语言）使用webservice开发客户端，并设置了超时时间为5秒，代码如下：
 * Map<String, Object> ctxt = ((BindingProvider) apps).getRequestContext();
 * ctxt.put("com.sun.xml.internal.ws.connect.timeout", 5*1000);
 * ctxt.put("com.sun.xml.internal.ws.request.timeout", 5*1000);
 * 首先将服务端关闭，然后运行客户端向服务端发起请求，按照我的理解，客户端应该会在第5s时报连接超时错误，然而客户端却是在20s的时候才报连接超时错误，感觉上面的连接超时时间设置失效了，求大神答疑解惑啊，是我对连接超时时间理解错了吗?20秒时报的异常如下：
 * javax.xml.ws.WebServiceException: Failed to access the WSDL at: http://192.168.49.110:8080/TestGeneAxisServer/services/appsystemstatusHttpPort?wsdl. It failed with:
 * Got Connection timed out: connect while opening stream from http://192.168.49.110:8080/TestGeneAxisServer/services/appsystemstatusHttpPort?wsdl.
 * at com.sun.xml.internal.ws.wsdl.parser.RuntimeWSDLParser.tryWithMex(Unknown Source)
 * 另外：webservice客户端中间代码室友jdk自带的命令生成的
 * 没有用框架，就是用jdk自带的wsimport.exe程序生成中间代码（wsdl文件是别人给的），然后将中间代码拷贝到客户端里，供客户端调用
 * 代码如下：（Appsystemstatus、AppsystemstatusPortType类就是中间代码里的类）
 * Appsystemstatus stus = new Appsystemstatus(new URL("http://192.168.49.110:8080/TestGeneAxisServer/services/appsystemstatusHttpPort?wsdl"));
 * AppsystemstatusPortType apps = stus.getAppsystemstatusHttpPort();
 * Map<String, Object> ctxt = ((BindingProvider) apps).getRequestContext();
 * ctxt.put("com.sun.xml.internal.ws.connect.timeout", connectionTimeOut);
 * ctxt.put("com.sun.xml.internal.ws.request.timeout", waitTimeOut);
 * String result = apps.updateStatus("0000000000",sysStaus);
 * return result;
 * 你设置超时的代码没错，现在获取wsdl时候超时 你生成的客户端，其中应该有Service和ServiceImpl ，直接按demo给的方法而不是你现在的每次调用都根据wsdl动态生成
 * //创建服务访问点集合对象
 * HelloServiceImplService helloServiceImplService=new HelloServiceImplService();
 * //获得服务点绑定的类
 * HelloServiceImpl helloService=helloServiceImplService.getHelloServiceImplPort();
 * //调用服务端方法
 * String returnstr=helloService.say("小明")
 * getHelloServiceImplPort还有一个带参数的getHelloServiceImplPort(URL); 用这个就可以修改
 * @author issuser
 */
@WebService(portName="helloService", serviceName="helloService", targetNamespace="http://test.elim.com/ws")
public interface HelloService {
	public String sayHello(String name);
}
