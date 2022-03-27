package com.yale.test.java.classpath.rmi.cnblogs;

import java.rmi.RemoteException;

public interface Services extends java.rmi.Remote{
	Object sendMessage(Message msg) throws RemoteException;
}
