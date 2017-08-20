package com.yale.test.thread.chapter.three;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class TestThrid extends Thread{
	private TestDoSec testDo;
	private String key;
	private String val;
	
	public TestThrid(String key,String key2,String value){
		this.testDo = TestDoSec.getInstance();
		this.key = key + key2;
		this.val = value;
	}
	
	public static void main(String[] args) {
		TestThrid t1 = new TestThrid("1","","1");
		TestThrid t2 = new TestThrid("2","","2");
		TestThrid t3 = new TestThrid("3","","3");
		TestThrid t4 = new TestThrid("1","","1");
		System.out.println("开始:" + System.currentTimeMillis());
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
	
	public void run(){
		testDo.doSome(key, val);
	}
}

class TestDoSec{
	private TestDoSec(){
	}
	private static TestDoSec testDoSec = new TestDoSec();
	public static TestDoSec getInstance() {
		return testDoSec;
	}
	private CopyOnWriteArrayList cowa = new CopyOnWriteArrayList();
	public void doSome(Object key,String value){
		Object obj = key;
		if (!cowa.contains(obj)) {
			cowa.add(obj);//用CopyOnWriteArrayList是为了防止,在迭代过程中加入数据发生异常
		} else {
			for(Iterator it = cowa.iterator();it.hasNext();){
				if(obj.equals(it.next())){
					obj = it.next();
				}
			}
		}
		synchronized (obj){
			try {
				Thread.sleep(1000);
				System.out.println(key+":"+value+":"+(System.currentTimeMillis()/1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}