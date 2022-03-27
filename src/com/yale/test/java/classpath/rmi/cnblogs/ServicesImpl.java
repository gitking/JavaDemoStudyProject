package com.yale.test.java.classpath.rmi.cnblogs;

import java.rmi.RemoteException;

/**
 * 所以这里服务端存在漏洞的即为ServicesImpl类，其存在一个方法其入口参数为Message对象，并且这里Message这个类是继承自Serializable，即可以进行反序列化。
 * 服务端通过bind()函数绑定远程对象到RMI注册表中，此时客户端即可以访问RMI注册表拿到stub，即可调用服务端的方法，比如sendMessage()函数
 * 此时先启动RMIServer.java,然后再启动RMIClient.java，即可达到打rmi服务端的效果，这里jdk版本为1.6
 * @author issuser
 *
 */
public class ServicesImpl implements Services {
	public ServicesImpl() throws RemoteException {
	}
	
	@Override
	public Object sendMessage(Message msg) throws RemoteException {
		return msg.getMessage();
	}
}
