package com.yale.test.io.imooc;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileReaderDemo {
	public static void main(String[] args) throws IOException {
		FileReader fr = new FileReader("D:" + File.separator + "JavaDemo" + File.separator + "info.txt");
		FileWriter fw = new FileWriter("D:" + File.separator + "JavaDemo" + File.separator + "info2.txt", true);
		char[] buffer = new char[2048];
		int c;
		while ((c = fr.read(buffer, 0, buffer.length)) != -1) {
			fw.write(buffer, 0, c);
			fw.flush();
		}
		fr.close();
		fw.close();
	}
}
