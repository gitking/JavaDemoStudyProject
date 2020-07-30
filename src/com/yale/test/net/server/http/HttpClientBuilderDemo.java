package com.yale.test.net.server.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

/**
 * httpclient-4.5.9.jar的api极其繁琐 
 * apache http真够讨厌, 一直换Api， 又改API了？ API基建狂魔再次出手？ 
 * HttpComponents 也就是以前的 HttpClient 项目。。。说白了：研究okhttp去吧 
 * https://www.oschina.net/news/107404/httpcomponents-4-5-9-ga-released
 * https://www.oschina.net/p/httpclient
 * http://hc.apache.org/dev-docs.html
 * https://vimsky.com/examples/detail/java-method-org.apache.http.impl.client.HttpClientBuilder.build.html(这个页面有20中用法示例)
 * commons-httpclient-3.1.jar跟httpclient-4.5.9.jar是什么关系？
 * commons-httpclient-3.1.jar 这个分支Apache已经不再维护了,取而代之的是httpclient-4.5.9.jar
 * @author dell
 */
public class HttpClientBuilderDemo {
	
	/**
     * http的post请求方式
     * @param httpInfoMap
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     * @throws CodeException
     */
    public static String sendHttpByPost(Map<String, String> httpInfoMap) throws ClientProtocolException, IOException {
    	CloseableHttpClient httpClient = null;
    	String responseStr = "";//接口返回结果
    	try {
    		HttpClientBuilder hcb = HttpClients.custom();
        	httpClient = hcb.build();
        	
        	String httpUrl = httpInfoMap.get("httpUrl");//请求地址
        	HttpPost httpPost = new HttpPost(httpUrl);
        	RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        	httpPost.setConfig(requestConfig);
        	httpPost.setHeader("Content-type", "application/json; charset=utf-8");
        	String httpData = httpInfoMap.get("httpData");//请求参数
        	httpPost.setEntity(new StringEntity(httpData, "UTF-8"));
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode == HttpStatus.SC_OK) {
				responseStr = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			} else {
	        	String httpName = httpInfoMap.get("httpName");//请求接口名称
			}
		} finally {
			if (httpClient != null) {
				httpClient.close();
			}
		}
    	return responseStr;
    }
	
	public static Map<String,Object > httpSendHttpsByDigestOrCookieOrSSL(Map<String,String> httpMap,String jsonParam) throws Exception{
        CloseableHttpClient httpClient = null;
        HttpClientBuilder clientBuilder=null;
        Map<String,Object > resultMap=new HashMap<String,Object >();
        String responsejson="";
        boolean isNeedAuthorize=Boolean.valueOf(httpMap.get("isNeedAuthorize"));//是否需要进行摘要认证
        boolean isNeedCookie=Boolean.valueOf(httpMap.get("isNeedCookie"));//是否需要Cookie
        boolean isIgnoreSSL=Boolean.valueOf(httpMap.get("isIgnoreSSL"));//是否需要忽略https证书认证
        String url=httpMap.get("url");
        String Cookie=httpMap.get("Cookie");
        try {
        	clientBuilder = HttpClients.custom();
            //http摘要认证信息
            if (isNeedAuthorize){
	            URI serverURI = new URI(url);
	            CredentialsProvider credsProvider = new BasicCredentialsProvider();
	            credsProvider.setCredentials(new AuthScope(serverURI.getHost(), serverURI.getPort()),
	                    new UsernamePasswordCredentials(httpMap.get("userName"), ""/*DigestUtils.md5Hex(httpMap.get("passWord"))*/  ));
	            clientBuilder.setDefaultCredentialsProvider(credsProvider);
            }
            //忽略https证书认证
            if(isIgnoreSSL){
            	//采用绕过验证的方式处理https请求  
                SSLContext sslcontext = createIgnoreVerifySSL();  
                //设置协议http和https对应的处理socket链接工厂的对象  
                Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()  
                    .register("https", new SSLConnectionSocketFactory(sslcontext))  
                    .build();  
                PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);  
                clientBuilder.setConnectionManager(connManager);
            }
            //增加Cookie处理
            CookieStore cookieStore  = new BasicCookieStore();
            if (isNeedCookie) {
            	clientBuilder.setDefaultCookieStore(cookieStore);
            }
            
            httpClient=clientBuilder.build();
            
            HttpPost post = new HttpPost(url);
              // 构造消息头
            post.setHeader("Content-type", "application/json; charset=utf-8");

            if(!isNeedAuthorize){//不需要摘要认证就在此直接获取双录系统需要的Cookie
            	post.setHeader("Cookie", Cookie);
            }
            /**
			 * VVII-10988【需求编号R1601501】新增“七炅三要素查询风险分接口”
			 * token在 header中的封装格式为:Authorization = "JWT "+token;有效期为7天
			 */
            post.setHeader("Authorization", httpMap.get("Authorization"));
            post.setEntity(new StringEntity(jsonParam, "UTF-8"));//发送xml字符串数据
            HttpResponse response = httpClient.execute(post);
            
            //处理 Cookie
           if(isNeedCookie){//直接把cookies到Map中
	            List<Cookie> cookies = cookieStore.getCookies();
	            resultMap.put("cookies", cookies);
            }
	        //获取响应码
	        int statusCode = response.getStatusLine().getStatusCode();

	        resultMap.put("statusCode", statusCode);
	        if (statusCode == HttpURLConnection.HTTP_OK) {
	            //获取数据
	        	responsejson = EntityUtils.toString(response.getEntity(), "utf-8");
	        	resultMap.put("responsejson", responsejson);
	        } else {
//	        	throw ExceptionUtil.createBusException("没有正确连接[" + name + "],HTTP服务返回有误,服务返回编码：" + statusCode);
	        }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultMap;
    }
	
	
	/** 
	* 绕过验证 
	*   
	* @return 
	* @throws NoSuchAlgorithmException  
	* @throws KeyManagementException  
	*/  
	public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
	        SSLContext sc = SSLContext.getInstance("SSLv3");  

	        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
	        X509TrustManager trustManager = new X509TrustManager() {  
	            @Override  
	            public void checkClientTrusted(  
	                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                    String paramString) throws CertificateException {  
	            }  

	            @Override  
	            public void checkServerTrusted(  
	                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
	                    String paramString) throws CertificateException {  
	            }  

	            @Override  
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
	                return null;  
	            }  
	        };  

	        sc.init(null, new TrustManager[] { trustManager }, null);  
	        return sc;  
	    }
}
