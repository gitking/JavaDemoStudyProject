package com.yale.test.net.server.socket;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/*
 * 这个包装过的SocketWrapper,代码要稍微好写一点点,至少不用每段代码都去写"流",这个流在代码中即是输入流,又是输出流,提供一个这样的对象,
 * 通过对象read,write就可以了。接下来要写一个Server端程序SocketServer。
 * 《java特种兵》第69页
 */
public class SocketWrapper {
	private Socket socket;
	private InputStream inputStream;
	private BufferedReader inputReader;
	private BufferedWriter outputWriter;
	
	public SocketWrapper(Socket socket) throws IOException {
		this.socket = socket;
		this.inputStream = socket.getInputStream();
		this.inputReader = new BufferedReader(new InputStreamReader(inputStream, "GBK"));
		this.outputWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "GBK"));
	}
	
	public String readLine() throws IOException {
		return inputReader.readLine();
	}
	
	public void writeLine(String line) throws IOException {
		outputWriter.write(line + '\n');
		outputWriter.flush();//由于是Buffer,所以需要flush
	}
	
	public void close() {
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
