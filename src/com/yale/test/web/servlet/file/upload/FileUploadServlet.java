package com.yale.test.web.servlet.file.upload;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * commons-fileupload组件是Apache的一个开源项目之一，可以从https://commons.apache.org/proper/commons-fileupload/下载。该组件简单易用，可实现一次上传一个或多个文件，并可限制文件大小。
 * 下载后解压zip包，将commons-fileupload-1.x.jar复制到tomcat的webapps/你的webapp/WEB-INF/lib/下，如果目录不存在请自建目录。
 * commons-fileupload-1.2.1.jar
 * https://www.liaoxuefeng.com/article/895884706212896
 * 
 * 使用multipart/form-data发送文件
 * 如果要在MIDP客户端向服务器上传文件，我们就必须模拟一个POST multipart/form-data类型的请求，Content-Type必须是multipart/form-data。
 * 以multipart/form-data编码的POST请求格式与application/x-www-form-urlencoded完全不同，multipart/form-data需要首先在HTTP请求头设置一个分隔符，例如ABCD：
 * hc.setRequestProperty("Content-Type", "multipart/form-data; boundary=ABCD");
 * 然后，将每个字段用“--分隔符”分隔，最后一个“--分隔符--”表示结束。例如，要上传一个title字段"Today"和一个文件C:\1.txt，HTTP正文如下：
 * --ABCD
	Content-Disposition: form-data; name="title"
	Today
	--ABCD
	Content-Disposition: form-data; name="1.txt"; filename="C:\1.txt"
	Content-Type: text/plain
	<这里是1.txt文件的内容>
	--ABCD--
 * 请注意，每一行都必须以\r\n结束，包括最后一行。如果用Sniffer程序检测IE发送的POST请求，可以发现IE的分隔符类似于---------------------------7d4a6d158c9，
 * 这是IE产生的一个随机数，目的是防止上传文件中出现分隔符导致服务器无法正确识别文件起始位置。我们可以写一个固定的分隔符，只要足够复杂即可。
 * 发送文件的POST代码如下：
 *  String[] props = ... // 字段名
	String[] values = ... // 字段值
	byte[] file = ... // 文件内容
	String BOUNDARY = "---------------------------7d4a6d158c9"; // 分隔符
	StringBuffer sb = new StringBuffer();
	// 发送每个字段:
	for(int i=0; i<props.length; i++) {
	    sb = sb.append("--");
	    sb = sb.append(BOUNDARY);
	    sb = sb.append("\r\n");
	    sb = sb.append("Content-Disposition: form-data; name=\""+ props[i] + "\"\r\n\r\n");
	    sb = sb.append(URLEncoder.encode(values[i]));
	    sb = sb.append("\r\n");
	}
	// 发送文件:
	sb = sb.append("--");
	sb = sb.append(BOUNDARY);
	sb = sb.append("\r\n");
	sb = sb.append("Content-Disposition: form-data; name=\"1\"; filename=\"1.txt\"\r\n");
	sb = sb.append("Content-Type: application/octet-stream\r\n\r\n");
	byte[] data = sb.toString().getBytes();
	byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
	// 设置HTTP头:
	hc.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);
	hc.setRequestProperty("Content-Length", String.valueOf(data.length + file.length + end_data.length));
	// 输出:
	output = hc.openOutputStream();
	output.write(data);
	output.write(file);
	output.write(end_data);
	// 读取服务器响应：
	// TODO...
 * 
 * https://www.liaoxuefeng.com/article/895889887461120
 * @author issuser
 */
public class FileUploadServlet extends HttpServlet {
	private String uploadDir = "C:\\temp";
	
	/**
	 * 如果要在web.xml配置文件中读取指定的上传文件夹，可以在init()方法中初始化：
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.uploadDir = config.getInitParameter("dir");
	}
	
	/**
	 * 当servlet收到浏览器发出的Post请求后，在doPost()方法中实现文件上传，我们需要遍历FileItemIterator，获得每一个FileItemStream：
	 * 最后在web.xml中配置Servlet：
	 * <?xml version="1.0" encoding="UTF-8"?>
		<!DOCTYPE web-app
		    PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
		    "http://java.sun.com/dtd/web-app_2_3.dtd">
		<web-app>
		    <servlet>
		        <servlet-name>UploadServlet</servlet-name>
		        <servlet-class>com.liaoxuefeng.web.FileUploadServlet</servlet-class>
		    </servlet>
		    <servlet-mapping>
		        <servlet-name>UploadServlet</servlet-name>
		        <url-pattern>/upload</url-pattern>
		    </servlet-mapping>
		</web-app>
	 * 配置好Servlet后，启动Tomcat或Resin，写一个简单的index.htm测试：
	 * <html>
		<body>
		<p>FileUploadServlet Demo</p>
		<form name="form1" action="upload" method="post" enctype="multipart/form-data">
		    <input type="file" name="file" />
		    <input type="submit" name="button" value="Submit" />
		</form>
		</body>
		</html>
	 * @throws IOException 
	 * @throws ServletException 
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		try {
			ServletFileUpload upload = new ServletFileUpload();
			//set max file size to 1MB
			//当上传成功后，显示success.jsp，否则，抛出异常。如果上传的文件大小超过了我们设定的1MB，就会得到一个FileSizeLimitExceededException。
			upload.setFileSizeMax(1024 * 1024);
			FileItemIterator it = upload.getItemIterator(req);
			while (it.hasNext()) {//循环处理每一个file
				FileItemStream item = it.next();
				if (!item.isFormField()) {
					handleFileItem(item);
				}
			}
			//转发到成功的页面
			req.getRequestDispatcher("success.jsp").forward(req, resp);
		} catch (FileUploadException e) {
			throw new ServletException("Cannot upload file.", e);
		}
	}
	
	void handleFileItem(FileItemStream item) {
		System.out.println("upload file:" + item.getName());
		File newUploadFile = new File(uploadDir + "/" + UUID.randomUUID().toString());
		byte[] buffer = new byte[4096];
		InputStream input = null;
		OutputStream output = null;
		try {
			input = item.openStream();
			output = new BufferedOutputStream(new FileOutputStream(newUploadFile));
			for (;;) {
				int n = input.read(buffer);
				if (n==-1) {
					break;
				}
				output.write(buffer, 0, n);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
