package com.yale.test.run;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

public class OperatingSystemMXBeanTest {
	/**
	 * https://blog.csdn.net/jackyrongvip/article/details/83975506?depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-5&utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromBaidu-5
	 * 在JAVA 7中，在监控方面，可以监视了系统和CPU负载，代码段如下：
import com.sun.management.OperatingSystemMXBean;
...
OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
OperatingSystemMXBean.class);
// What % CPU load this current JVM is taking, from 0.0-1.0
System.out.println(osBean.getProcessCpuLoad());

// What % load the overall system is at, from 0.0-1.0
System.out.println(osBean.getSystemCpuLoad());

这里用数字0到1表示CPU的负载及系统的负载。
可惜的是，不知道为啥ORACLE没放到标准的java.lang中，而采用的是
com.sun.management.OperatingSystemMXBean。
要注意的中是，getSystemLoadAverage(),在JAVA 6也有，但
可惜的是在WINDOWS上运行的并不好。
	 */
	public static void main(String[] args) {
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		System.out.println("操作系统最近一分钟的平均负载" + osBean.getSystemLoadAverage());
		System.out.println("操作系統架构:" + osBean.getArch());
		System.out.println("操作系統名字:" + osBean.getName());
		System.out.println("操作系統名字:" + System.getProperty("os.name"));
		System.out.println("操作系統版本:" + osBean.getVersion());
		
		System.out.println("操作系統版本:" + System.getProperty("sun.os.patch.level"));//得到操作系统版本
		
		System.out.println("PlatformManagedObject名字:" + osBean.getObjectName());
	}
	
	/**
	 * https://cloud.tencent.com/developer/ask/43687
	 * @return
	 * @throws Exception
	 */
	public static double getProcessCpuLoad() throws Exception {

	    MBeanServer mbs    = ManagementFactory.getPlatformMBeanServer();
	    ObjectName name    = ObjectName.getInstance("java.lang:type=OperatingSystem");
	    AttributeList list = mbs.getAttributes(name, new String[]{ "ProcessCpuLoad" });

	    if (list.isEmpty()) {
	    	return Double.NaN;
	    }

	    Attribute att = (Attribute)list.get(0);
	    Double value  = (Double)att.getValue();

	    // usually takes a couple of seconds before we get real values
	    if (value == -1.0)      return Double.NaN;
	    // returns a percentage value with 1 decimal point precision
	    return ((int)(value * 1000) / 10.0);
	}
}
