package com.yale.test.thread.mldn;
class Pen {
	public synchronized void get(Note note) {
		System.out.println("我为了得到本");
		//下面这个for循环啥都不会输出,这是为什么？因为编译优化了肯定
//		for(int i=0;i<Integer.MAX_VALUE;i++) {
//			int a = i;
//			if (a > (Integer.MAX_VALUE -1)) {
//				System.out.println(a);
//				break;
//			}
//		}
		note.result();
	}
	public synchronized void result() {
		System.out.println("为了涂鸦");
	}
}
class Note {
	public synchronized void get(Pen pen) {
		System.out.println("我为了得到笔");
		pen.result();
	}
	public synchronized void result() {
		System.out.println("为了上厕所");
	}
}
public class DeadLock implements Runnable{
	private static Note note = new Note();
	private static Pen pen = new Pen();
	public static void main(String[] args) {
		System.out.println("这是一个死锁程序,因为一个对象只能持有一个锁,多运行几下就会发生死锁现象");
		System.out.println("这个为啥会发生死锁,不好理解");
		new DeadLock();
	}
	public DeadLock(){
		new Thread(this).start();
		pen.get(note);
	}
	@Override
	public void run() {
		note.get(pen);
	}
}
