package com.yale.test.io.imooc;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataOutputStreamDemo {

	public static void main(String[] args) throws IOException {
		String file = "demo/dos.bat";
		DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
		dos.writeInt(10);
		dos.writeInt(-10);
		dos.writeLong(10L);
		dos.writeDouble(10.5);
		dos.writeUTF("中国");//采用UTF-8编码写出
		dos.writeChars("中国");//采用UTF-16be编码写出
		dos.close();
		IOUtil.printHex(file);
	}
}
