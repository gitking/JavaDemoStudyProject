package com.yale.test.io.imooc;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class DataInputStreamDemo {

	public static void main(String[] args) throws IOException {
		String file = "demo/dos.bat";
		IOUtil.printHex(file);
		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		int i = dis.readInt();
		System.out.println(i);
		i = dis.readInt();
		System.out.println(i);

		long ii = dis.readLong();
		System.out.println(ii);

		double ib = dis.readDouble();
		System.out.println(ib);
		String s = dis.readUTF();
		System.out.println(s);
		dis.close();		
	}

}
