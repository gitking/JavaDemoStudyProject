package com.yale.test.web.filter.lxf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;

/*
 * 修改请求
 * Filter可以对请求进行预处理，因此，我们可以把很多公共预处理逻辑放到Filter中完成。
 * 考察这样一种需求：我们在Web应用中经常需要处理用户上传文件，例如，一个UploadServlet可以简单地编写如下：
 * 但是要保证文件上传的完整性怎么办？在哈希算法一节中，我们知道，如果在上传文件的同时，把文件的哈希也传过来，服务器端做一个验证，就可以确保用户上传的文件一定是完整的。
 * 这个验证逻辑非常适合写在ValidateUploadFilter中，因为它可以复用。
 * 我们先写一个简单的版本，快速实现ValidateUploadFilter的逻辑：
 * 这个ValidateUploadFilter的逻辑似乎没有问题，我们可以用curl命令测试：
 * curl http://localhost:8080/upload/file -v -d 'test-data' \
	  -H 'Signature-Method: SHA-1' \
	  -H 'Signature: 7115e9890f5b5cc6914bdfa3b7c011db1cdafedb' \
	  -H 'Content-Type: application/octet-stream'
	*   Trying ::1...
	* TCP_NODELAY set
	* Connected to localhost (::1) port 8080 (#0)
	> POST /upload/file HTTP/1.1
	> Host: localhost:8080
	> User-Agent: curl/7.64.1
	> Accept: *\/*
	> Signature-Method: SHA-1
	> Signature: 7115e9890f5b5cc6914bdfa3b7c011db1cdafedb
	> Content-Type: application/octet-stream
	> Content-Length: 9
	> 
	* upload completely sent off: 9 out of 9 bytes
	< HTTP/1.1 200 
	< Transfer-Encoding: chunked
	< Date: Thu, 30 Jan 2020 13:56:39 GMT
	< 
	* Connection #0 to host localhost left intact
	<h1>Uploaded:</h1><pre><code></code></pre>
	* Closing connection 0
 * ValidateUploadFilter对签名进行验证的逻辑是没有问题的，但是，细心的童鞋注意到，UploadServlet并未读取到任何数据！
 * 这里的原因是对HttpServletRequest进行读取时，只能读取一次。如果Filter调用getInputStream()读取了一次数据，后续Servlet处理时，再次读取，将无法读到任何数据。怎么办？
 * 这个时候，我们需要一个“伪造”的HttpServletRequest，具体做法是使用代理模式，对getInputStream()和getReader()返回一个新的流：
 * 注意观察ReReadableHttpServletRequest的构造方法，它保存了ValidateUploadFilter读取的byte[]内容，并在调用getInputStream()时通过byte[]构造了一个新的ServletInputStream。
 * 然后，我们在ValidateUploadFilter中，把doFilter()调用时传给下一个处理者的HttpServletRequest替换为我们自己“伪造”的ReReadableHttpServletRequest：
 * 再注意到我们编写ReReadableHttpServletRequest时，是从HttpServletRequestWrapper继承，而不是直接实现HttpServletRequest接口。
 * 这是因为，Servlet的每个新版本都会对接口增加一些新方法，从HttpServletRequestWrapper继承可以确保新方法被正确地覆写了，因为HttpServletRequestWrapper是由Servlet的jar包提供的，目的就是为了让我们方便地实现对HttpServletRequest接口的代理。
 * 我们总结一下对HttpServletRequest接口进行代理的步骤：
 * 1。从HttpServletRequestWrapper继承一个XxxHttpServletRequest，需要传入原始的HttpServletRequest实例；
 * 2.覆写某些方法，使得新的XxxHttpServletRequest实例看上去“改变”了原始的HttpServletRequest实例；
 * 3.在doFilter()中传入新的XxxHttpServletRequest实例。
 * 虽然整个Filter的代码比较复杂，但它的好处在于：这个Filter在整个处理链中实现了灵活的“可插拔”特性，即是否启用对Web应用程序的其他组件（Filter、Servlet）完全没有影响。
 * 小结
 * 借助HttpServletRequestWrapper，我们可以在Filter中实现对原始HttpServletRequest的修改。
 */
@WebFilter("/upload/*")
public class ValidateUploadFilter implements Filter{
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String digest = req.getHeader("Signature-Method");
		String signature = req.getHeader("Signature");
		if (digest == null || digest.isEmpty() || signature == null || signature.isEmpty()) {
			sendErrorPage(resp, "Missing signature.");
			return;
		}
		MessageDigest md = getMessageDigest(digest);
		InputStream input = new DigestInputStream(request.getInputStream(), md);
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for(;;) {
			int len = input.read(buffer);
			if (len == -1) {
				break;
			}
			output.write(buffer, 0, len);
		}
		String actual = toHexString(md.digest());
		if (!signature.equals(actual)){
			sendErrorPage(resp, "Invalid signature.");
			return;
		}
		chain.doFilter(new ReReadableHttpServletRequest(req, output.toByteArray()), response);
	}
	
	private String toHexString(byte[] digest) {
		StringBuilder sb = new StringBuilder();
		for (byte b:digest) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
	
	private MessageDigest getMessageDigest(String name) throws ServletException {
		try {
			return MessageDigest.getInstance(name);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}
	
	private void sendErrorPage(HttpServletResponse resp, String errorMessage) throws IOException {
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		PrintWriter pw = resp.getWriter();
		pw.write("<html><body><h1>");
		pw.write(errorMessage);
		pw.write("</h1></body></html");
		pw.flush();
		
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}

class ReReadableHttpServletRequest extends HttpServletRequestWrapper {
	private byte[] body;
	private boolean open = false;
	
	public ReReadableHttpServletRequest(HttpServletRequest request, byte[] body) {
		super(request);
		this.body = body;
	}
	
	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (open) {
			throw new IllegalStateException("Cannot re-open input stream!");
		}
		open = true;
		return new ServletInputStream() {
			private int offset =0;
			
			@Override
			public boolean isFinished() {
				return offset >= body.length;
			}
			
			@Override
			public boolean isReady() {
				return true;
			}
			
			@Override
			public void setReadListener(ReadListener listener) {
			}
			
			@Override
			public int read() throws IOException {
				if (offset >= body.length) {
					return -1;
				}
				int n = body[offset] & 0xff;
				offset++;
				return n;
			}
		};
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		if (open) {
			throw new IllegalStateException("Cannot re-open reader!");
		}
		open = true;
		return new BufferedReader(new InputStreamReader(getInputStream(), "UTF-8"));
	}
}