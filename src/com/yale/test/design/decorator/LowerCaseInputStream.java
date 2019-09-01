package com.yale.test.design.decorator;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * JAVA的IO都是装饰者模式
 * 当时JAVA IO也引出装饰者模式的一个缺点:利用装饰者模式,常常造成设计中有大量的小类,数量实在太多,可能造成使用此API程序员的困扰。
 * @author lenovo
 *
 */
public class LowerCaseInputStream extends FilterInputStream {

	protected LowerCaseInputStream(InputStream in) {
		super(in);
	}
	
	public int read() throws IOException{
		int c = super.read();
		return (c == -1 ? c : Character.toLowerCase((char)c));
	}
	
	public int read(byte[] b,int offset,int len) throws IOException{
		int result = super.read(b, offset, len);
		for(int i=offset; i< offset+result; i++){
			b[i] = (byte)Character.toLowerCase((char)b[i]);
		}
		return result;
	}
}
