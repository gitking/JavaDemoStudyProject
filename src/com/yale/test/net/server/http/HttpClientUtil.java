package com.yale.test.net.server.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**
 * https://mp.weixin.qq.com/s/4wty3-igm9wHec5h0jxVeQ
 * http://hc.apache.org/status.html
 * The 3.1 branch of Commons HttpClient is at the end of life. No more public releases are expected.
 * Commons HttpClient的3.1分支即将结束。 预计不会有更多的公开发布。
 * commons-httpclient-3.1.jar跟httpclient-4.5.9.jar是什么关系？
 * 取而代之的是httpclient-4.5.9.jar
 * @author dell
 */
public class HttpClientUtil {
	private static HttpClient httpClient = null;  
    private static final String CHARSET = "UTF-8";  
    private HttpClientUtil(){  
    }  
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
    	        	manager.setParams(managerParams);
    	        	httpClient = new HttpClient(manager);
    	        	httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,CHARSET);
    	        } 
    		}
    	}
        return httpClient;  
    }  
}
