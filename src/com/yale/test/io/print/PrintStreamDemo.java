package com.yale.test.io.print;

import java.io.IOException;
import java.io.OutputStream;

public class PrintStreamDemo {

	public static void main(String[] args) throws IOException {
		try {
			Integer.parseInt("abd");
		} catch (Exception e) {
			System.err.println("err会变红:  " + e);
			System.out.println(e);
		}
		
		OutputStream out = System.out; 
		out.write("世界和平".getBytes());
	}

}
