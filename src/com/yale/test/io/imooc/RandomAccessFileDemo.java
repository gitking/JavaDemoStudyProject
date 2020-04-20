package com.yale.test.io.imooc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RandomAccessFileDemo {

	public static void main(String[] args) throws IOException {
		/**
		 * RandomAccessFile是java提供的对文件内容的访问,即可以读文件,也可以写文件。
		 * RandomAccessFile支持随机访问文件,可以访问文件的任意位置.
		 * 文件模型 ：文件在硬盘上的文件是byte byte byte存储的，是数据的集合。文本文件 就是一个字节序列，可以是任意编码的字节序列
		 * 打开文件有俩种模式”rw”(读写)”r”(只读)
		 * RandomAccessFile raf=new RandomAccessFile(File,”rw”);
		 * 因为RandomAccessFile支持随机访问文件，所以该类有个文件指针，打开文件时指针在开头，pointer=0;
		 * 写方法 raf.write(int);--->只写一个字节(后8位)，同时指针指向下一个位置准备再次写入
		 * 读方法 Int b=raf.read();-- 读一个字节
		 * 文件读写完成以后一定要关闭(Oracle官方说明)。
		 */
		File demo = new File("Demo");//没有写绝对路径,默认在项目根目录下
		if (!demo.exists()) {
			demo.mkdir();
		}
		File file = new File(demo, "raf.bat");
		if (!file.exists()){
			file.createNewFile();
		}
		
		RandomAccessFile raf = new RandomAccessFile(file, "rw");
		System.out.println("刚开始指针的位置是零:" + raf.getFilePointer());
		raf.write('A');//char占俩个字节,raf一次写一个字节,这次只写后8位,8bit(位)就是一个字节
		System.out.println("写了一个字节,现在指针的位置应该是①:" + raf.getFilePointer());
		raf.write('B');
		
		int i = 0x7fffffff;//这个是int最大的数字
		raf.write(i >>> 24);//高8位
		raf.write(i >>> 16);
		raf.write(i >>> 8);
		raf.write(i);
		System.out.println("现在已经写到第六个位置:" + raf.getFilePointer());
		raf.writeInt(i);//writeInt可以直接写一个int,这里要看源码搞懂
		String s = "中";
		byte[] gbk = s.getBytes("gbk");
		raf.write(gbk);
		System.out.println(raf.length());
		
		//读文件,必须把指针移到头部
		raf.seek(0);
		//一次性读取
		byte[] buf = new byte[(int)raf.length()];
		raf.read(buf);
		System.out.println(Arrays.toString(buf));
		String s1 = new String(buf, "gbk");
		System.out.println(s1);//这里输出会乱码
		for (byte b : buf) {
			System.out.println(Integer.toHexString(b & 0xff) + " ");
		}
		raf.close();
	}
}
