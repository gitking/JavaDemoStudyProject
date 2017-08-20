package com.yale.test.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ScoketReadContext {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("127.0.0.1",8080);
			OutputStream output = socket.getOutputStream();
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
			bw.write("你好");
			bw.flush();
			bw.write("我在用客户端给你写信");
			bw.flush();
			bw.write("我走了");
			bw.flush();
			output.close();
			bw.close();
			socket.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
