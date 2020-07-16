package com.yale.test.thread.mldn;

/**
 * Java命令学习系列（二）——Jstack
 * http://www.hollischuang.com/archives/110
 * 例子来源:欢迎关注『Java之道』微信公众号 
 * @author dell
 */
public class JStackDemo2 {
    public static void main(String[] args) {
        Thread t1 = new Thread(new Thread1());//建立一个线程
        t1.start();//启动一个线程
    }
}
class Thread1 implements Runnable {

	@Override
    public void run() {
       while (true) {
    	   System.out.println(1);
       }
    }
}

