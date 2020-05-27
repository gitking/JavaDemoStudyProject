package com.yale.test.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadClassFile {

	public static void main(String[] args) throws IOException {
		//D:\GitWorkSpace\JavaDemoStudyProject\bin\sun\reflect
		File file = new File("d:" + File.separator + "GitWorkSpace" + File.separator + "JavaDemoStudyProject" + File.separator + "bin" + File.separator + "sun" + File.separator + "reflect" + File.separator + "GeneratedMethodAccessor1.class");
		InputStream in = new FileInputStream(file);
		
		File fileOut = new File("d:" + File.separator + "GitWorkSpace" + File.separator + "JavaDemoStudyProject" + File.separator + "bin" + File.separator + "sun" + File.separator + "reflect" + File.separator + "GeneratedMethodAccessorTest.class");
		OutputStream out = new FileOutputStream(fileOut);

		int temp;
		while ((temp = in.read()) != -1) {//一次读一个字节
			System.out.print(temp);
			out.write(temp);
		}
		in.close();
		out.close();
	}
}
