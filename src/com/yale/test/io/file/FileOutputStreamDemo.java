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
			/*
			 * 要特别注意：OutputStream还提供了一个flush()方法，它的目的是将缓冲区的内容真正输出到目的地。
			 * 为什么要有flush()？因为向磁盘、网络写入数据的时候，出于效率的考虑，操作系统并不是输出一个字节就立刻写入到文件或者发送到网络，而是把输出的字节先放到内存的一个缓冲区里（本质上就是一个byte[]数组），等到缓冲区写满了，再一次性写入文件或者网络。对于很多IO设备来说，一次写一个字节和一次写1000个字节，花费的时间几乎是完全一样的，所以OutputStream有个flush()方法，能强制把缓冲区内容输出。
			 * 通常情况下，我们不需要调用这个flush()方法，因为缓冲区写满了OutputStream会自动调用它，并且，在调用close()方法关闭OutputStream之前，也会自动调用flush()方法。
			 * 但是，在某些情况下，我们必须手动调用flush()方法。举个栗子：
			 * 小明正在开发一款在线聊天软件，当用户输入一句话后，就通过OutputStream的write()方法写入网络流。小明测试的时候发现，发送方输入后，接收方根本收不到任何信息，怎么肥四？
			 * 原因就在于写入网络流是先写入内存缓冲区，等缓冲区满了才会一次性发送到网络。如果缓冲区大小是4K，则发送方要敲几千个字符后，操作系统才会把缓冲区的内容发送出去，这个时候，接收方会一次性收到大量消息。
			 * 解决办法就是每输入一句话后，立刻调用flush()，不管当前缓冲区是否已满，强迫操作系统把缓冲区的内容立刻发送出去。
			 * 实际上，InputStream也有缓冲区。例如，从FileInputStream读取一个字节时，操作系统往往会一次性读取若干字节到缓冲区，并维护一个指针指向未读的缓冲区。然后，每次我们调用int read()读取下一个字节时，可以直接返回缓冲区的下一个字节，避免每次读一个字节都导致IO操作。当缓冲区全部读完后继续调用read()，则会触发操作系统的下一次读取并再次填满缓冲区。
			 */
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

