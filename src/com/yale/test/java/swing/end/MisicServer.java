package com.yale.test.java.swing.end;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class MisicServer {
	ArrayList<ObjectOutputStream> clientOutputStreams;
	
	public class ClientHandler implements Runnable {
		ObjectInputStream in;
		Socket clientSocket;
		public ClientHandler(Socket socket) {
			try {
				clientSocket = socket;
				in = new ObjectInputStream(clientSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		@Override
		public void run() {
			Object o2 = null;
			Object o1 = null;
			try {
				while ((o1 = in.readObject()) != null) {
					o2 = in.readObject();
					System.out.println("read two objects");
					tellEveryone(o1, o2);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void go() {
		clientOutputStreams = new ArrayList<ObjectOutputStream>();
		try {
			ServerSocket serverSocket = new ServerSocket(4242);
			while(true) {
				Socket clientSocket = serverSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
				clientOutputStreams.add(out);
				Thread t = new Thread(new ClientHandler(clientSocket));
				t.start();
				System.out.println("got a connection");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tellEveryone(Object one, Object two) {
		Iterator it = clientOutputStreams.iterator();
		while (it.hasNext()) {
			try {
				ObjectOutputStream out = (ObjectOutputStream)it.next();
				out.writeObject(one);
				out.writeObject(two);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		new MisicServer().go();
	}
}
