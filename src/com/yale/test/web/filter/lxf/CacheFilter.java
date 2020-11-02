package com.yale.test.web.filter.lxf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.WriteListener;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/*
 * 修改响应
 * 既然我们能通过Filter修改HttpServletRequest，自然也能修改HttpServletResponse，因为这两者都是接口。
 * 我们来看一下在什么情况下我们需要修改HttpServletResponse。
 * 假设我们编写了一个Servlet，但由于业务逻辑比较复杂，处理该请求需要耗费很长的时间：HelloServlet 
 * 好消息是每次返回的响应内容是固定的，因此，如果我们能使用缓存将结果缓存起来，就可以大大提高Web应用程序的运行效率。
 * 缓存逻辑最好不要在Servlet内部实现，因为我们希望能复用缓存逻辑，所以，编写一个CacheFilter最合适：
 * 实现缓存的关键在于，调用doFilter()时，我们不能传入原始的HttpServletResponse，因为这样就会写入Socket，我们也就无法获取下游组件写入的内容。
 * 如果我们传入的是“伪造”的HttpServletResponse，让下游组件写入到我们预设的ByteArrayOutputStream，我们就“截获”了下游组件写入的内容，于是，就可以把内容缓存起来，
 * 再通过原始的HttpServletResponse实例写入到网络。
 * 这个CachedHttpServletResponse实现如下：
 * 可见，如果我们想要修改响应，就可以通过HttpServletResponseWrapper构造一个“伪造”的HttpServletResponse，这样就能拦截到写入的数据。
 * 修改响应时，最后不要忘记把数据写入原始的HttpServletResponse实例。
 * 这个CacheFilter同样是一个“可插拔”组件，它是否启用不影响Web应用程序的其他组件（Filter、Servlet）。
 * 小结
 * 借助HttpServletResponseWrapper，我们可以在Filter中实现对原始HttpServletRequest的修改。
 */
@WebFilter("/slow/*")
public class CacheFilter implements Filter {
	//从Path到byte[]的缓存
	private Map<String, byte[]> cache = new ConcurrentHashMap<>();
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String url = req.getRequestURI();//获取Path:
		byte[] data = this.cache.get(url);//获取缓存内容:
		resp.setHeader("X-Cache-Hit", data==null ? "No" : "Yes");
		if (data == null) {
			//缓存未找到,构造一个伪造的Response
			CachedHttpServletResponse wrapper = new CachedHttpServletResponse(resp);
			data = wrapper.getContent();//让下游组件写入数据到伪造的Response
			//从伪造的Response中读取写入的内容并放入缓存
			cache.put(url, data);
		}
		//写入到原始的Response
		ServletOutputStream output = resp.getOutputStream();
		output.write(data);
		output.flush();
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}
	@Override
	public void destroy() {
		
	}
}

class CachedHttpServletResponse extends HttpServletResponseWrapper {
	private boolean open = false;
	private ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	public CachedHttpServletResponse(HttpServletResponse response) {
		super(response);
	}
	
	@Override
	public PrintWriter getWriter() throws IOException {//获取Writer
		if (open) {
			throw new IllegalStateException("Cannot re-open writer!");
		}
		open = true;
		//return new PrintWriter(output, false, StandardCharsets.UTF-8);jdk8没有这个方法
		return new PrintWriter(output, false);
	}
	
	@Override
	public ServletOutputStream getOutputStream() throws IOException {//获取OutputStream
		if (open) {
			throw new IllegalStateException("Cannot re-open output stream!");
		}
		open = true;
		return new ServletOutputStream() {
			@Override
			public boolean isReady() {
				return true;
			}
			
			@Override
			public void setWriteListener(WriteListener listener) {
			}
			
			@Override
			public void write(int b) throws IOException {//实际写入到ByteArrayOutputStream
				output.write(b);
			}
		};
	}
	
	public byte[] getContent() {//返回写入的byte[]
		return output.toByteArray();
	}
}
