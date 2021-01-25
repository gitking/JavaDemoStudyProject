package com.yale.test.java.classpath.rmi.demo;

import java.io.Serializable;
import javax.swing.JPanel;

/*
 * GUI服务要实现的部分
 * 定义任何通用服务都得要实现的getGuiPanel()这个方法,因为继承Serializable,所以能够自动地序列化,
 * 而这也是又通过网络传送服务所必须的机制。
 * 这个类是最关键的部分,这个简单的接口只有一个getGuiPanel()方法,每个要传送给客户端的服务都得实现这个接口,这样才会让浏览器"通用"。
 * 实现这个接口之后,服务才能克服客户端完全不知道服务实际类的问题。至少来的服务会有getGuiPanel()方法。
 * 客户端调用getService(selectedSvc)后取得序列化对象,然后再不知道是什么类的情况下调用一定会有的getGuiPanel()来取得JPanel,然后放到浏览器上开始与用户交互。
 */
public interface Service extends Serializable {
	public JPanel getGuiPanel();
}
