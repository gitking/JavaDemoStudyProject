package com.yale.test.io.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/**
 * 字节输出流:OutputStream
 * File类不支持文件内容处理,如果要处理文件内容那么必须通过流的操作模式来完成
 * 在java.io包中流分为俩种:字节流和字符流
 * 字节流:InputStream,OutputStream;
 * 字符流:Reader, Writer;
 * 字节流和字符流操作的本质区别只有一个:字节流是原生的操作,而字符流是经过处理后的操作。在你进行网络数据传输,磁盘数据保存,所保存的支持的数据类型只有:字节,
 * 而所有磁盘中的数据必须先读取到内存后才可以操作, 内存里面会帮我们将字节变为字符。字符更加适合处理中文。
 * @author dell
 */
public class FileOutputStreamDemo {
	public static void main(String[] args) throws IOException {
		OutputStream os = null;
		try {
			File file = new File("d:" + File.separator + "JavaDemo" + File.separator + "hello.txt");
			if(!file.getParentFile().exists()) {
				//如果父目录不存在,则自动创建父目录
				file.getParentFile().mkdirs();
			}
			//OutputStream是一个抽象类,FileOutputStream默认会覆盖文件,如果追加文件使用另外一个构造方法
			//如果文件不存在,则会自动创建,不再需要编写createNewFile()方法手工创建
			os = new FileOutputStream(file);
			//new FileOutputStream(file, true),往文件里面追加内容
			//OutputStream os = new FileOutputStream(file, true);

			String msg = "www.mldn.cn";
			os.write(msg.getBytes());//将msg转换为字节,写入文件
			//flush会把内容,立马写到硬盘里面去,如果你开发servlet,往浏览器里面输出东西,你不能调用flush方法,因为你flush的时候,代码还不知道把内容写往哪里,浏览器不是你本地文件
			os.flush();//即使下面的代码报错,也能把内容写到硬盘里面
			//int sdf = 1/0;
			msg = "www.mldn.cn\r\n";//换行
			os.write(msg.getBytes());
			msg = "www.mldn.cn\r\n";//换行
			os.write(msg.getBytes());
			
			os.write(msg.getBytes(),0, 3);//只输出3个字节,部分输出,这个方法是最常用的
			
		} finally {
			/**
			 * 必须关闭文件流,如果你在代码中没有关闭输出流,你就再也关不了这个资源了,
			 * 除非你重启电脑,你可以写个for循环,循环1万次,打开输入流,不关闭
			 * 试试会不会把电脑搞死
			 */
			//字符流如果不关闭,内容是写不到的文件里面的,因为字符流用到了缓存,但是字节流不关闭,内容是可以写到文件里面的
			//字符流需要在缓存中将字节转换为字符,字节则不需要
			os.close();//关闭输出
		}
		
	}
}

