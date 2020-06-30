package com.yale.test.math;

import java.io.UnsupportedEncodingException;

public class EncodeDemo {

	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "慕课网ABC";
		//转换成字节序列用的是项目默认的编码
		System.out.println("UTF-8中文占三个字节,英文占一个字节");
		byte[] bytes = s.getBytes();
		for (byte b : bytes) {
			//toHexString把字节转成了int以16进制输出,byte占8个bit(位),转换成int,int是占32bit位
			//toHexString接收的参数为int,所以toHexString会把byte转成int,然后会在byte前面添加24个0,变成int
			System.out.print(Integer.toHexString(b) + "    ");
			System.out.print("& 0xff的意思是把前面的24个零去掉：" + Integer.toHexString(b & 0xff) + "");
			System.out.println();
		}	
		
		System.out.println("GBK中文占俩个字节,英文占一个字节");
		byte[] byteGbk = s.getBytes("GBK");
		for (byte b : byteGbk) {
			//toHexString把字节转成了int以16进制输出,byte占8个bit(位),转换成int,int是占32bit位
			//toHexString接收的参数为int,所以toHexString会把byte转成int,然后会在byte前面添加24个0,变成int
			System.out.print(Integer.toHexString(b) + "    ");
			System.out.print("& 0xff的意思是把前面的24个零去掉：" + Integer.toHexString(b & 0xff) + "");
			System.out.println();
		}
		
		//JAVA是双字节编码utf-16be,utf-16be中文占用2个字节,英文也占俩个字节编码
		byte[] byte4 = s.getBytes("utf-16be");
		System.out.println("JAVA是双字节编码utf-16be,utf-16be中文占用2个字节,英文也占俩个字节编码");
		for (byte b : byte4) {
			System.out.print(Integer.toHexString(b & 0xff) + " ");
		}
		System.out.println();
		
		/**
		 * 文本文件就是字节序列,可以是任意编码的字节序列
		 * 联通,联这个俩个汉字,他们正好符合了utf-8的编码,这点慕课网老师讲的好像不对,
		 * 联通为什么在 txt文档里面会乱码是因为你用记事本保存联通时默认用的是ansi编码保存的,但是当记事本打开时,记事本读取文件里面的字节序列时
		 * 是通过字节来判断你这个文件是ANSI编码还是UTF-8编码的,打开时以UTF-8显示就乱码了,因为你保存的时候把联通这俩个字按ansi编码保存的
		 * txt文档模式是ansi编码的
		 * 这个还得上网看windows联通乱码的原因?window到底是怎么判断一个文件的编码的,用ansi保存为啥又用utf-8打开呢?
		 */
	}
}
