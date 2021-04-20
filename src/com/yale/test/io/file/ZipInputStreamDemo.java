package com.yale.test.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
 * ZipInputStream是一种FilterInputStream，它可以直接读取zip包的内容：
 * ┌───────────────────┐
│    InputStream    │
└───────────────────┘
          ▲
          │
┌───────────────────┐
│ FilterInputStream │
└───────────────────┘
          ▲
          │
┌───────────────────┐
│InflaterInputStream│
└───────────────────┘
          ▲
          │
┌───────────────────┐
│  ZipInputStream   │
└───────────────────┘
          ▲
          │
┌───────────────────┐
│  JarInputStream   │
└───────────────────┘

另一个JarInputStream是从ZipInputStream派生，它增加的主要功能是直接读取jar文件里面的MANIFEST.MF文件。
因为本质上jar包就是zip包，只是额外附加了一些固定的描述文件。
 * 
 */
public class ZipInputStreamDemo {

	public static void main(String[] args) {
		/*
		 * 读取zip包
		 * 我们要创建一个ZipInputStream，通常是传入一个FileInputStream作为数据源，然后，循环调用getNextEntry()，直到返回null，表示zip流结束。
		 * 一个ZipEntry表示一个压缩文件或目录，如果是压缩文件，我们就用read()方法不断读取，直到返回-1：
		 */
		try(ZipInputStream zip = new ZipInputStream(new FileInputStream(""))) {
			ZipEntry entry = null;
			while ((entry = zip.getNextEntry()) != null) {
				String name = entry.getName();
				if (!entry.isDirectory()) {
					int n;
					while ((n = zip.read()) != -1) {
						
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * 读取zip包
		 * 我们来看看ZipInputStream的基本用法。
		 * 我们要创建一个ZipInputStream，通常是传入一个FileInputStream作为数据源，然后，循环调用getNextEntry()，直到返回null，表示zip流结束。
		 * 一个ZipEntry表示一个压缩文件或目录，如果是压缩文件，我们就用read()方法不断读取，直到返回-1：
		 * 下面的代码没有考虑文件的目录结构。如果要实现目录层次结构，new ZipEntry(name)传入的name要用相对路径。
		 * java.util.zip.ZipFile$ZipFileInflaterInputStream，赶紧Google发现还是有许多小伙伴碰到相同的问题，例如：Java压缩流GZIPStream导致的内存泄露 。
		 */
		try(ZipOutputStream zip = new ZipOutputStream(new FileOutputStream(""))){
			File[] files = new File("").listFiles();
			for (File file: files) {
				zip.putNextEntry(new ZipEntry(file.getName()));
				//zip.write(getFileDataAsBytes(file));
				zip.closeEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
