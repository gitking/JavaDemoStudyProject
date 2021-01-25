package com.yale.test.java.classpath.rmi.demo.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.yale.test.java.classpath.rmi.demo.Service;
import com.yale.test.java.classpath.rmi.demo.ServiceServer;

/*
 * 这是客户端类,它创建出简单的GUI.在RMI regsitry中查询取得ServiceSever的stub,然后调用它的远程服务取得服务清单并显示在GUI上面。
 */
public class ServiceBrowser {
	JPanel mainPanel;
	JComboBox serviceList;
	ServiceServer server;
	
	public void buildGUI() {
		JFrame frame = new JFrame("RMI Browser");
		mainPanel = new JPanel();
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		
		Object[] services = getServicesList();
		//把服务列表添加到JComboBox里面,JComboBox知道如何显示数组中的字符串
		serviceList = new JComboBox(services);
		frame.getContentPane().add(BorderLayout.NORTH, serviceList);
		serviceList.addActionListener(new MyListListener());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}
	
	void loadService(Object serviceSelection) {
		try {
			Service svc = server.getService(serviceSelection);
			mainPanel.removeAll();
			mainPanel.add(svc.getGuiPanel());
			mainPanel.validate();
			mainPanel.repaint();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 执行RMI registry查询,取得stub并调用getServiceList()
	 */
	Object[] getServicesList() {
		Object[] services;
		try {
			Object obj = null;
			services = null;
			
			obj = Naming.lookup("rmi://127.0.0.1/ServiceServer");
			//将stub转换成remote instance的类型,这样才能调用它的getServiceList()
			server = (ServiceServer)obj;
			services = server.getServiceList();
			return services;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	class MyListListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ev) {
			Object selection = serviceList.getSelectedItem();
			//获取用户选中的服务
			loadService(selection);
		}
	}
	
	/*
	 * 运行这个类之前,必须先运行ServiceServerImpl这个类
	 */
	public static void main(String[] args) {
		new ServiceBrowser().buildGUI();
	}
}
