package com.yale.test.web.filter.request;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 装饰Request类,增强Request中的部分方法
 * 需要跟filter类中的方法参数request实现相同的接口
 * @author dell
 */
public class EncodingRequestWrapper extends HttpServletRequestWrapper{
	/**
	 * 在这里接收原来的request对象,装饰者模式是什么意思呢？就是一切拜托你(原来的对象来干活),我只增强原来对象的部分方法
	 * 所以构造方法必须接收原来的对象
	 * 这里有一点比较恶心就是,如果原来的对象有100个方法,那你要在这个类里面把这个100个方法都是实现一下
	 * 那有没有不需要我实现的办法呢?这300个方法写着太累了？
	 * 答案:当然有了,用继承就行了,继承HttpServletRequestWrapper 包装类
	 */
	private HttpServletRequest request;
	
	public EncodingRequestWrapper(HttpServletRequest request) {
		super(request);//这行代码很重要,必须将原来的对象传给父类
		this.request = request;
	}
	
	/**
	 * 继承包装类HttpServletRequestWrapper之后,我只需要增强这个方法就行了
	 * 别的方法包装类都已经帮我实现好了HttpServletRequestWrapper
	 */
	@Override
	public String getParameter(String name) {
		String value = request.getParameter(name);
		//这里处理编码问题,增强核心代码
		try {
			value = new String(value.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return value;//我这里返回的值是我增强之后的值了
	}
}
