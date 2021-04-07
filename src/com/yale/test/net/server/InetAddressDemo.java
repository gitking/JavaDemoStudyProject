package com.yale.test.net.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Map;

public class InetAddressDemo {
	public static void main(String[] args) throws UnknownHostException {
		String osName = System.getProperty("os.name").toLowerCase();
		System.out.println("操作系统版本名字:" + System.getProperty("os.name").toLowerCase()); 
		InetAddress ip = InetAddress.getLocalHost();
		String localname = ip.getHostName();
		String loaclIp = ip.getHostAddress();
		System.out.println("本机名:" + localname);
		System.out.println("本机ip地址:" + loaclIp);
		Map<String, String> map = System.getenv();
	    String userName = map.get("USERNAME");//获取用户名
	    String computerName = map.get("COMPUTERNAME");// 获取计算机名
	    String userDomain = map.get("USERDOMAIN");// 获取计算机域名
	    
		System.out.println("获取用户名:" + userName);
		System.out.println("获取计算机名:" + computerName);
		System.out.println("获取计算机域名:" + userDomain);

		try {
			byte[] mac = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i=0; i< mac.length; i++) {
				if (i!=0) {
					sb.append("-");
				}
				String s = Integer.toHexString(mac[i] & 0xFF);
				sb.append(s.length()==1?0+s:s);
			}
			System.out.println("本机MAC地址:" + sb.toString().toUpperCase());
		} catch (SocketException e4) {
			e4.printStackTrace();
		}
		
		//java 获取本机的所有网卡的Mac地址
		try {
			Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
			while (enumeration.hasMoreElements()) {
				StringBuilder strBuil = new StringBuilder();
				NetworkInterface networkInterface = enumeration.nextElement();
				if (networkInterface != null) {
					byte[] bytes = networkInterface.getHardwareAddress();
					if (bytes != null) {
						for (int i=0; i<bytes.length; i++) {
							if (i!=0) {
								strBuil.append("-");
							}
							int tmp = bytes[i]&0xff;//字节转换为整数
							String str = Integer.toHexString(tmp);
							if (str.length() == 1) {
								strBuil.append("0" + str);
							} else {
								strBuil.append(str);
							}
						}
						String mac = strBuil.toString().toUpperCase();
						System.out.println("获取本机所有MAC地址为:" + mac);
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		try {
			Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
			StringBuilder macStr = new StringBuilder();
			InetAddress inetAddress = null;
			while (el.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface)el.nextElement();
				byte[] mac = ni.getHardwareAddress();
				if (mac == null || mac.length == 0) {
					continue;
				}
				Enumeration<InetAddress> nii = ni.getInetAddresses();
				while (nii.hasMoreElements()) {
					inetAddress = nii.nextElement();
					if (inetAddress instanceof Inet6Address) {
						continue;
					}
					if (!inetAddress.isReachable(3000)) {
						continue;
					}
					for (byte b : mac) {
						int tmp = b&0xff;//字节转换为整数
						String str = Integer.toHexString(tmp);
						macStr.append(str + "-");
					}
					macStr.append("&");
				}
			}
			System.out.println("另一种获取MAC地址的方式:" + macStr.toString());
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		if(osName.startsWith("windows")){
			//本地是windows
			String mac = getWindowsMACAddress();
			System.out.println("本机MAC Address:" + mac);
		} else {
			//本地是非windows系统 一般就是unix
			String mac = getUnixMACAddress();
			System.out.println("本机Linux MAC Address:" + mac);
		} 
	}
	
	/**
	* 获取unix网卡的mac地址.
	* 非windows的系统默认调用本方法获取.如果有特殊系统请继续扩充新的取mac地址方法.
	* @return mac地址
	*/
	public static String getUnixMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ifconfig eth0");// linux下的命令，一般取eth0作为本地主网卡 显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(process
			.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("hwaddr");// 寻找标示字符串[hwaddr]
				if (index >= 0) {// 找到了
					mac = line.substring(index +"hwaddr".length()+ 1).trim();//  取出mac地址并去除2边空格
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			bufferedReader = null;
			process = null;
		}
	
		return mac;
	} 
	
	/**
	* 获取widnows网卡的mac地址.
	* 如果你获取不到MAC地址,说明你的windows是中文版本的,你可以调式一下这个方法就知道了
	* @return mac地址
	*/
	public static String getWindowsMACAddress() {
		String mac = null;
		BufferedReader bufferedReader = null;
		Process process = null;
		try {
			process = Runtime.getRuntime().exec("ipconfig -all");// windows下的命令，显示信息中包含有mac地址信息
			bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int index = -1;
			while ((line = bufferedReader.readLine()) != null) {
				index = line.toLowerCase().indexOf("physical address");// 寻找标示字符串[physical address]
				if (index >= 0) {// 找到了
					index = line.indexOf(":");// 寻找":"的位置
					if (index>=0) {
						mac = line.substring(index + 1).trim();//  取出mac地址并去除2边空格
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		try {
			if (bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			bufferedReader = null;
			process = null;
		}
		return mac;
	} 
}
