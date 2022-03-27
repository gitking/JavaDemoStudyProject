package com.yale.test.net.server.webservice.server;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.jws.WebService;

import com.yale.test.net.server.webservice.HelloService;

/**
 * 服务端代码
 * @author issuser
 */
@WebService(portName="helloService", serviceName="helloService", targetNamespace="http://test.elim.com/ws")
public class HelloServiceImpl implements HelloService {
	private Random random = new Random();

	@Override
	public String sayHello(String name) {
		try {
			TimeUnit.SECONDS.sleep(5 + random.nextInt(21));//随机睡眠5-25秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "Hello" + name;
	}
	
	/**
	 * https://stackoverflow.com/questions/13967069/how-to-set-timeout-for-jax-ws-webservice-call
	 * How to Set Timeout for JAX-WS WebService Call
	 * I'm working on a WebService Client and I want to set a Timeout for my WebService Call. I have tried different approaches but still I'm not able to achieve this.
	 * I'm using JAX-WS for code generation from WSDL. I'm using JBoss-eap-5.1 as App Server and JDK1.6.0_27. I found these diff approaches for setting timeout but none of them is working for me.
	 * URL mbr_service_url = new URL(null,GlobalVars.MemberService_WSDL, new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(URL url) throws IOException {
                URL clone_url = new URL(url.toString());
                HttpURLConnection clone_urlconnection = (HttpURLConnection) clone_url.openConnection();
                // TimeOut settings
                clone_urlconnection.setConnectTimeout(10000);
                clone_urlconnection.setReadTimeout(10000);
                return (clone_urlconnection);
            }
        });
        MemberService service = new MemberService(mbr_service_url);
        MemberPortType soap = service.getMemberPort();
        ObjectFactory factory = new ObjectFactory();
        MemberEligibilityWithEnrollmentSourceRequest request = factory.createMemberEligibilityWithEnrollmentSourceRequest();

        request.setMemberId(GlobalVars.MemberId);
        request.setEligibilityDate(value);

        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.ws.client.BindingProviderProperties.REQUEST_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.ws.client.BindingProviderProperties.CONNECT_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.internal.ws.client.BindingProviderProperties.REQUEST_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.internal.ws.client.BindingProviderProperties.CONNECT_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.ws.developer.JAXWSProperties.REQUEST_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.ws.developer.JAXWSProperties.CONNECT_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.internal.ws.developer.JAXWSProperties.REQUEST_TIMEOUT, 10000);
        ((BindingProvider) soap).getRequestContext().put(com.sun.xml.internal.ws.developer.JAXWSProperties.CONNECT_TIMEOUT, 10000);
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        MemberEligibilityWithEnrollmentSourceResponse response = soap.getMemberEligibilityWithEnrollmentSource(request);
        logger.log("Call to member service finished.");
     * For now what I have done is, I have called my webservice method from inside an executor. I know its not a good approach, but its working for me. Guys please help me to do it in a proper way.
     * Answers:
     * You could try these settings (they are paired to be used in pairs) 你可以试试这些设置（它们是成对使用的） 
     * BindingProviderProperties.REQUEST_TIMEOUT
     * BindingProviderProperties.CONNECT_TIMEOUT
     * BindingProviderProperties should be from com.sun.xml.internal.WS.client
     * Or the strings for JBoss:
     * javax.xml.ws.client.connectionTimeout
     * javax.xml.ws.client.receiveTimeout
     * All properties to be put on getRequestContext() in milliseconds.
     * (BindingProvider)wsPort).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, yourTimeoutInMillisec);
     * For JBoss specifically, you might want to use the property StubExt.PROPERTY_CLIENT_TIMEOUT from org.jboss.ws.core.StubExt. See this thread(https://developer.jboss.org/thread/101684) for details.
     * Answers:
     * Like kolossus said you should use:com.sun.xml.internal.ws.client.BindingProviderProperties     
     * And String values are:
     * com.sun.xml.internal.ws.connect.timeout
     * com.sun.xml.internal.ws.request.timeout
     * Although internal packages shouldn't be used, this is the only way if you work with default JDK6. So, in this case setting receive and connect timeout should be done with:
     * bindingProvider.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT,requestTimeoutMs);
     * bindingProvider.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT,connectTimeoutMs);
     * But beware, constant values are different if you are using other JAXWS reference implementation, i.e. JAXWS-RT 2.1.4 BindingProviderProperties:
     * com.sun.xml.ws.client.BindingProviderProperties
     * you will have different String values for REQUEST_TIMEOUT and CONNECT_TIMEOUT:
     * com.sun.xml.ws.request.timeout
     * com.sun.xml.ws.connect.timeout
     * Answers:
     * For me setting javax.xml.ws.client.connectionTimeout and javax.xml.ws.client.receiveTimeout solved the problem
     * ((BindingProvider)port).getRequestContext().put("javax.xml.ws.client.connectionTimeout", timeout);
     * ((BindingProvider)port).getRequestContext().put("javax.xml.ws.client.receiveTimeout", timeout);
     * refer(https://access.redhat.com/documentation/en-us/jboss_enterprise_application_platform/6/html/development_guide/develop_a_jax-ws_client_application)
     * Answers:
     * JBossWS - Supported Target Containers
     * https://developer.jboss.org/docs/DOC-13569
     * https://jbossws.github.io/
     * Answers:
     * Setting the following options works for me. I am using the Metro JAXWS implementation.
     * ((BindingProvider)portType).getRequestContext().put(JAXWSProperties.CONNECT_TIMEOUT, 10000);
     * ((BindingProvider) portType).getRequestContext().put(JAXWSProperties.REQUEST_TIMEOUT, 50000);
     * portType is the Web Service endpoint interface.Values of the above fields from the com.sun.xml.internal.ws.developer.JAXWSProperties
     * public static final java.lang.String CONNECT_TIMEOUT = "com.sun.xml.internal.ws.connect.timeout";
     * public static final java.lang.String REQUEST_TIMEOUT = "com.sun.xml.internal.ws.request.timeout";
	 */
}
