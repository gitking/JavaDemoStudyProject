package com.yale.test.web.filter.response;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class StaticResponse extends HttpServletResponseWrapper {
	private HttpServletResponse response;
	private PrintWriter pw;
	public StaticResponse(HttpServletResponse hsp, String htmlPath) throws FileNotFoundException, UnsupportedEncodingException {
		super(hsp);
		this.response = hsp;
		pw = new PrintWriter(htmlPath, "UTF-8");//PrintWriter这个类如果htmlPath不存在会直接创建这个文件
	}
	
	/**
	 * 装饰者模式
	 * 增强getWriter这个方法
	 */
	public PrintWriter getWriter() {
		//返回一个与html文件绑定在一起的PrintWriter对象
		//jsp会使用它进行输出,这样数据就都输出到我们指定的html文件中了
		return pw;
	}
}
