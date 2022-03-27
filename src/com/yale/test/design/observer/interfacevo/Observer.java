package com.yale.test.design.observer.interfacevo;

/**
 * 观察者
 * https://www.liaoxuefeng.com/article/895889887461120
 * 一个完整的HTTP连接为：用户通过某个命令发起连接请求，然后系统给出一个等待屏幕提示正在连接，当连接正常结束后，前进到下一个屏幕并处理下载的数据。如果连接过程出现异常，将给用户提示并返回到前一个屏幕。用户在等待过程中能够随时取消并返回前一个屏幕。
 * 我们设计一个HttpThread线程类负责在后台连接服务器，HttpListener接口实现Observer（观察者）模式，以便HttpThread能提示观察者下载开始、下载结束、更新进度条等。HttpListener接口如下：
 * 实现HttpListener接口的是继承自Form的一个HttpWaitUI屏幕，它显示一个进度条和一些提示信息，并允许用户随时中断连接：
 * HttpThread是负责处理Http连接的线程类，它接受一个URL和HttpListener：
 * 使用GET获取内容
 * 我们先讨论最简单的GET请求。GET请求只需向服务器发送一个URL，然后取得服务器响应即可。在HttpThread的run()方法中实现如下：
 * public void run() {
	    HttpConnection hc = null;
	    InputStream input = null;
	    try {
	        hc = (HttpConnection)Connector.open(url);
	        hc.setRequestMethod(HttpConnection.GET); // 默认即为GET
	        hc.setRequestProperty("User-Agent", USER_AGENT);
	        // get response code:
	        int code = hc.getResponseCode();
	        if(code!=HttpConnection.HTTP_OK) {
	            listener.onError(code, hc.getResponseMessage());
	            return;
	        }
	        // get size:
	        int size = (int)hc.getLength(); // 返回响应大小，或者-1如果大小无法确定
	        listener.onSetSize(size);
	        // 开始读响应：
	        input = hc.openInputStream();
	        int percent = 0; // percentage
	        int tmp_percent = 0;
	        int index = 0; // buffer index
	        int reads; // each byte
	        if(size!=(-1))
	            buffer = new byte[size]; // 响应大小已知，确定缓冲区大小
	        else
	            buffer = new byte[MAX_LENGTH]; // 响应大小未知，设定一个固定大小的缓冲区
	        while(!cancel) {
	            int len = buffer.length - index;
	            len = len&gt;128 ? 128 : len;
	            reads = input.read(buffer, index, len);
	            if(reads<=0)
	                break;
	            index += reads;
	            if(size>0) { // 更新进度
	                tmp_percent = index * 100 / size;
	                if(tmp_percent!=percent) {
	                    percent = tmp_percent;
	                    listener.onProgress(percent);
	                }
	            }
	        }
	        if(!cancel && input.available()>0) // 缓冲区已满，无法继续读取
	            listener.onError(601, "Buffer overflow.");
	        if(!cancel) {
	            if(size!=(-1) && index!=size)
	                listener.onError(102, "Content-Length does not match.");
	            else
	                listener.onFinish(buffer, index);
	        }
	    }
	    catch(IOException ioe) {
	        listener.onError(101, "IOException: " + ioe.getMessage());
	    }
	    finally { // 清理资源
	        if(input!=null)
	            try { input.close(); } catch(IOException ioe) {}
	        if(hc!=null)
	            try { hc.close(); } catch(IOException ioe) {}
	    }
	}
 * 
 * @author lenovo
 */
public interface Observer {
	/**
	 * 所有的具体观察者都需要实现此方法
	 * 当主题发生变化时,主题会调用此方法将变化值通知各个观察者
	 * @param temp
	 * @param humidity
	 * @param pressure
	 */
	public void update(float temp,float humidity,float pressure);
}
