package com.yale.test.thread.mldn;
class Pen {
	public synchronized void get(Note note) {
		System.out.println("我为了得到本");
		for(int i=0;i<Integer.MAX_VALUE;i++) {//下面这个for循环啥都不会输出,这是为什么？因为编译优化了肯定
			int a = i;
			if (a > (Integer.MAX_VALUE -1)) {
				System.out.println(a);
				break;
			}
		}
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
		System.out.println(note);
		System.out.println(pen);
		System.out.println("这是一个死锁程序,多运行几下就会发生死锁现象");
		System.out.println("这个为啥会发生死锁,不好理解");
		/**
		 * 发生死锁的原因如下:
		 * 代码在pen.get(note)这行代码的时候将pen对象锁住了,然后可以调用note.result();这个方法,但是注意要调用
		 * note.result();这个方法,首先要获取note这个对象的锁,不幸的是note对象在run方法的note.get(pen);这
		 * 行代码的时候,note对象就已经被锁住了,然后note.get(pen);这个方法里面还要调用pen.result();方法,但是注意要调用
		 * pen.result();就要首先获取pen这个对象的锁,不幸的是pen这个对象在pen.get(note);这里就已经被锁住了,
		 * 此时就发生死锁了,这就是发生死锁的原因.阿里云 魔乐科技 李兴华老师这节课讲的不太好,没说清楚为啥发生死锁,
		 * 这是我自己用jstack -l pid 自己理解出来的。
		 */
		new DeadLock();
	}
	public DeadLock(){
		new Thread(this, "DeadLock01").start();
		pen.get(note);
	}
	@Override
	public void run() {
		note.get(pen);
	}
}
