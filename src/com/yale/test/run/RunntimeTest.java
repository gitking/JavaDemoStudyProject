package com.yale.test.run;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Runtime描述的是运行时的状态,也就是说在整个的JVM之中,Runtime类是唯一一个与JVM运行状态有关的类,
 * 并且都会默认提供有一个该类的实例化对象。
 * 由于在每一个JVM进程里面只允许提供一个Runtime类的对象,所以这个类的构造方法被默认私有化了,那么就证明该类
 * 使用的是单例设计模式
 * 阿里云 魔乐科技的JAVA教程视频
 * https://edu.aliyun.com/lesson_36_446?spm=5176.8764728.0.0.3ead521ct2pu1e#_446
 * https://edu.aliyun.com/course/explore/gentech?spm=5176.10731491.category.12.78904d8aB9zl2v&subCategory=java&filter%5Btype%5D=all&filter%5Bprice%5D=all&filter%5BcurrentLevelId%5D=all&orderBy=studentNum&title=
 * @author dell
 */
public class RunntimeTest {
	private static final int CPUTIME = 5000;  
    
    private static final int PERCENT = 100;  
  
    private static final int FAULTLENGTH = 10; 
    
	public static void main(String[] args) throws InterruptedException {
		RunntimeTest sdf = new RunntimeTest();
		sdf = null;//等待垃圾回收
		Runtime run = Runtime.getRuntime();
		System.out.println("获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		String str = "垃圾空间";
		for (int i=0;i <30000; i++) {
			str = str + i;
		}
		System.out.println("[2]获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("[2]获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("[2]获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		Thread.sleep(2000);
		run.gc();//手工调用垃圾回收
		System.gc();//System.gc内部调用的就是run.gc()
		
		System.out.println("[3]获取最大可用内存空间:" +  run.maxMemory() + "字节,默认的配置为本机系统内存的4分之1");
		System.out.println("[3]获取可用内存空间:" +  run.totalMemory() + "字节,默认的配置为本机系统内存的64分之1");
		System.out.println("[3]获取空余内存空间:" +  run.freeMemory() + "字节");
		System.out.println("availableProcessors方法可以获取本机的CPU内核数:" + run.availableProcessors());
		
//		RunntimeTest sdf = new RunntimeTest();
//		try {
//			sdf.finalize();
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
		try {
			run.exec("calc");//这里可以执行CMD命令,calc打开windows的计算机
			run.exec("notepad");//这里可以执行CMD命令,notepad打开记事本
			run.exec("CMD");//这里可以执行CMD命令,cmd打开cmd命令窗口
			run.exec("mspaint");//这里可以执行CMD命令,mspaint打开windows的画图程序
			//testGetSysInfo();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		/**
		 * 但是从JDK1.9开始,这一操作已经不建议使用了,而对于对象回收释放.从JDK 
		 * 或者使用java.lang.ref.Cleaner类进行回收(Cleaner也支持有AutoCloseable处理).
		 * 1、GC之前被调用 。2、只会被调用一次。3、不可靠，不能保证被执行，不建议使用。
		 */
		System.out.println("finalize方法会在JAVA进行垃圾回收前,被自动调用,这个方法抛出的异常类型为Throwable,但是这个类即使抛出异常了,也不影响后面代码的执行" );
		throw new Exception("我不影响后面代码的继续运行,因为我这个类马上就死了");
	}
	
	/**
	 * java获取硬件信息
	 * Java程序如果需要获取系统的硬件信息，如CPU、硬盘序列号，网卡MAC地址等，一般需要使用JNI程序调用，不过还有另外一种方法也可以实现这个功能，
	 * 并且不需要JNI调用，原理是根据操作系统调用系统命令command，再根据命令行返回的信息分析获取硬件信息，这个方法需要程序判断操作系统类型来进行相应的调用，
	 * 可以调用任何操作系统提供的命令，下面是Windows下面获取网卡MAC地址的例子：
	 * http://blog.sina.com.cn/s/blog_5ca9fdd80100bd59.html
	 */
	public static void testGetSysInfo() {
		String address = "";
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith("Windows")) {
			try {
				String command = "cmd.exe /c ipconfig /all";
				Process p = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line;
				while ((line = br.readLine()) != null) {
					System.out.println("*****" + line);
					if (line.indexOf("Physical Address") > 0 || line.indexOf("物理地址") > 0) {
						int index = line.indexOf(":");
						index += 2;
						address = line.substring(index);
						break;
					}
				}
				br.close();
				System.out.println("電腦的MAC地址為:mac address:" + address.trim());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
   /**
    * 获得CPU使用率.
    * @return 返回cpu使用率
    * @author amg     * Creation date: 2008-4-25 - 下午06:05:11
	* wmic很强大，网上有很多wmic的命令，
	* eg:wmic 获取物理内存
	* wmic memlogical get TotalPhysicalMemory
	* wmic 获取进程信息，很详细
	* wmic process
	* https://www.cnblogs.com/Rozdy/p/5522873.html
    */  
   private double getCpuRatioForWindows() {
       try { 
    	   /**
    	    * wmic很强大，网上有很多wmic的命令，
    	    * eg:wmic 获取物理内存
			* wmic memlogical get TotalPhysicalMemory
			* https://superuser.com/questions/334641/whats-the-equivalent-command-of-wmic-memlogical-in-windows-7
			* wmic 获取进程信息，很详细
			* wmic process
			* wmic 删除指定进程(根据进程PID):
			* wmic process where pid="123" delete
			* https://www.cnblogs.com/scotth/p/9434412.html
    	    */
           String procCmd = System.getenv("windir") + "//system32//wbem//wmic.exe process get Caption,CommandLine,"  
                   + "KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";  
           // 取进程信息  
           long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));  
           Thread.sleep(CPUTIME);  
           long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));  
           if (c0 != null && c1 != null) {  
               long idletime = c1[0] - c0[0];  
               long busytime = c1[1] - c0[1];  
               return Double.valueOf(  
                       PERCENT * (busytime) / (busytime + idletime))  
                       .doubleValue();  
           } else {  
               return 0.0;  
           }  
       } catch (Exception ex) {  
           ex.printStackTrace();  
           return 0.0;  
       }  
   }
   
   /**
    * 读取CPU信息.
    * @param proc
    * @return
    * @author amg     * Creation date: 2008-4-25 - 下午06:10:14
    */  
   private long[] readCpu(final Process proc) {  
       long[] retn = new long[2];  
       try {  
           proc.getOutputStream().close();  
           InputStreamReader ir = new InputStreamReader(proc.getInputStream());  
           LineNumberReader input = new LineNumberReader(ir);  
           String line = input.readLine();  
           if (line == null || line.length() < FAULTLENGTH) {  
               return null;  
           }  
           int capidx = line.indexOf("Caption");  
           int cmdidx = line.indexOf("CommandLine");  
           int rocidx = line.indexOf("ReadOperationCount");  
           int umtidx = line.indexOf("UserModeTime");  
           int kmtidx = line.indexOf("KernelModeTime");  
           int wocidx = line.indexOf("WriteOperationCount");  
           long idletime = 0;  
           long kneltime = 0;  
           long usertime = 0;  
           while ((line = input.readLine()) != null) {  
               if (line.length() < wocidx) {  
                   continue;  
               }  
               // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,  
               // ThreadCount,UserModeTime,WriteOperation  
               String caption = Bytes.substring(line, capidx, cmdidx - 1)  
                       .trim();  
               String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();  
               if (cmd.indexOf("wmic.exe") >= 0) {  
                   continue;  
               }  
               // log.info("line="+line);  
               if (caption.equals("System Idle Process")  
                       || caption.equals("System")) {  
                   idletime += Long.valueOf(  
                           Bytes.substring(line, kmtidx, rocidx - 1).trim())  
                           .longValue();  
                   idletime += Long.valueOf(  
                           Bytes.substring(line, umtidx, wocidx - 1).trim())  
                           .longValue();  
                   continue;  
               }  
 
               kneltime += Long.valueOf(  
                       Bytes.substring(line, kmtidx, rocidx - 1).trim())  
                       .longValue();  
               usertime += Long.valueOf(  
                       Bytes.substring(line, umtidx, wocidx - 1).trim())  
                       .longValue();  
           }  
           retn[0] = idletime;  
           retn[1] = kneltime + usertime;  
           return retn;  
       } catch (Exception ex) {  
           ex.printStackTrace();  
       } finally {  
           try {  
               proc.getInputStream().close();  
           } catch (Exception e) {  
               e.printStackTrace();  
           }  
       }  
       return null;  
   }  
}

class Bytes {   
    /** *//**  
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在  
     * 包含汉字的字符串时存在隐患，现调整如下：  
     * @param src 要截取的字符串  
     * @param start_idx 开始坐标（包括该坐标)  
     * @param end_idx   截止坐标（包括该坐标）  
     * @return  
     */  
    public static String substring(String src, int start_idx, int end_idx){   
        byte[] b = src.getBytes();   
        String tgt = "";   
        for(int i=start_idx; i<=end_idx; i++){   
            tgt +=(char)b[i];   
        }   
        return tgt;   
    }   
}  
