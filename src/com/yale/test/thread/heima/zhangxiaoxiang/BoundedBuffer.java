package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * （ArrayBlockingQueue 类提供了这项功能，因此没有理由去实现这个示例类。） 
 * JAVA 官方API的例子阻塞队列例子,这个例子在Condition这个API里面
 * @author dell
 */
public class BoundedBuffer {
	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();
	
	final Object[] items = new Object[100];
	int putptr, takeptr, count;
	
	public void put (Object x) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length) {//items数据已经满了
				notFull.await();//线程等待
			}
			items[putptr] = x;
			
			if (++putptr == items.length) {//putptr等于items.length,说明数组放一遍了,注意不是放满了
				putptr = 0;
			}
			++count;//放了几个数据了,也可以理解为还有多少个数据可以取
			notEmpty.signal();
		} finally {
			lock.unlock();//释放锁
		}
	}
	
	public Object take () throws InterruptedException {
		lock.lock();
		try {
			/**
			 * count=0说明数组里面还没有放数据,如果一开始take这个线程先运行,
			 * 那么count肯定是等于0的,take这个线程就会进入阻塞状态,让出CPU,那么put的线程就会先运行
			 */
			while (count == 0) {
				notEmpty.await();//线程等待
			}
			Object x = items[takeptr];
			if (++takeptr == items.length) {//按顺序取出数据,先放进去的,先取出来
				takeptr = 0;
			}
			--count;
			notFull.signal();
			return x;
		} finally {
			lock.unlock();//释放锁
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		BoundedBuffer bb = new BoundedBuffer();
		bb.put("11");
		
		int putptr = 0;
		if (++putptr == 0) {//putptr等于items.length,说明数组放一遍了,注意不是放满了
			System.out.println("先判断,再++");
		} else {
			System.out.println("先++,再判断");
		}
		
		int putptr1 = 0;
		if (putptr1++ == 0) {//putptr等于items.length,说明数组放一遍了,注意不是放满了
			System.out.println("putptr1,先判断,再++");
		} else {
			System.out.println("putptr1,先++,再判断");
		}
		
		int numI,numQ= 0;//初始值都是0
        numI = numQ++;//这里是先赋值,先把numQ(0)的值赋给numI,再运算numQ++。
        System.out.println(numI);//numI输入0,因为他先把numQ(0)的值赋给numI了。
        System.out.println(numQ);//numQ输入1,numI = numQ++;这行代码numQ++已经运算过了,所以numQ的值为1。
        
        int numJ,numK= 0;//初始值都是0
        numJ= ++numK;//这里是先运算,先把numK的值加1,再赋值，再把numK的值赋给numJ。
        System.out.println(numJ);//numJ输入1
        System.out.println(numK);//numK输入1,这里不用说了，numK的值肯定是1,因为numJ= ++numK;这里是先运算,先把numK的值加1,再赋值
        
        //你们要注意i++,++i是否参与运算表达式，懂吗？
        //下面这种其实是一样的。
        int arrI = 0;
        int [] arr = new int[]{1,2,3,4};
        arr[arrI++]++;//这里的arr[arrI++]等于arr[0],先把arrI的值赋给数组的下标索引,等于arr[0]取出的是1,然后1++编程2了，数组的第一个值就变成2了。
        System.out.println("数组"+ arr[0]+"数组"+arr[1]+"数组"+arr[2]);//数组2数组2数组3
        System.out.println("arrI的值为:"+ arrI);
        
        //下面这个跟上面的效果一样
        int arrK = 0;
        int [] arrSec = new int[]{1,2,3,4};
        arrSec[arrK++] = arrSec[arrK++]++;
        System.out.println("数组"+ arrSec[0]+"数组"+arrSec[1]+"数组"+arrSec[2]);//数组2数组2数组3
        System.out.println("arrK的值为:"+ arrK);
        
        int arrF = 0;
        int [] arrTh = new int[]{1,2,3,4};
        arrSec[++arrF] = arrSec[++arrF]++;
        System.out.println("数组"+ arrTh[0]+"数组"+arrTh[1]+"数组"+arrTh[2]);//数组2数组2数组3
        System.out.println("arrF的值为:"+ arrF);
	}
}
