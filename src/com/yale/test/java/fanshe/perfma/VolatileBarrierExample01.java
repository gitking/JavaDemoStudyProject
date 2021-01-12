package com.yale.test.java.fanshe.perfma;

/*
 * https://www.zhihu.com/answer/270308453
 * 无论在哪种情况，即使是在第二种场景里，stop的可见性其实都没有得到保证。
因为即使在第二种场景里，虽然子线程中有对volatile类型变量i的读写+非volatile类型变量stop的读，但是主线程中只有对非volatile类型变量stop的写入，因此无法建立 （主线程对stop的写) happens-before于 (子线程对stop的读) 的关系。
因此你不能指望主线程对stop的写一定能被子线程看到。
虽然场景二在实际运行时程序依然正确终止了，但是这个只能算是运气好，如果换一种JVM实现或者换一种CPU架构，可能场景二也会陷入死循环。
可以设想这样的一个场景，主/子线程分别在core1/core2上运行，core1的cache中有stop的副本，core2的cache中有stop与i的副本，而且stop和i不在同一条cacheline里。
core1修改了stop变量，但是由于stop不是volatile的，这个改动可以只发生在core1的cache里，而被修改的cacheline理论上可以永远不刷回内存，这样core2上的子线程就永远也看不到stop的变化了。
我们可以试着分析一下程序实际执行时发生了什么。
 * 由于run方法里的while循环会被执行很多次，所以必然会触发jit编译
 * 下面来分析两种情况下jit编译后的结果（触发了多次jit编译，只贴出最后一次C2等级jit编译后的结果）
 * 一：i为run方法内的局部变量的情况：
 * 在第一个红框处检测stop变量，如果为true，那么跳转到L0001处继续执行（L0001处再往下走函数就退出了），但此时stop为false，所以不会走这个分支
 * L0000，inc %ebp。也就是i++
 * test %eax, -0x239864a(%rip)，轮询SAFEPOINT的操作，可以无视
 * jmp L0000，无条件跳转回L0000处继续执行i++
 * 把jit编译后的代码改写回来，大概是这个样子
 * if(!stop){ while(true){ i++; } }
 * 也就是说JVM觉得每次循环都去访问非volatile类型的stop变量太浪费了，就只在函数执行之初访问一次stop，后续无论stop变量怎么变，都不管了。
 * 第一种情况死循环就是这么来的。
 * 二：i为全局的volatile变量的情况：VolatileBarrierExample2
 * 从第一个红框开始看：
 * jmp L0001，无条件跳转到label L0001处
 * movzbl 0x6c(%r10),%r8d; 访问static变量stop，并将其复制到寄存器r8d里
 * test %r8d, %r8d; je L0000; 如果r8d里的值为0，跳转到L0000处，否则继续往下走（函数结束）
 * L000: mov 0x68(%r10), %r8d; 访问static变量i，并将其复制到寄存器r8d里
 * inc %r8d; 自增r8d里的值
 *  mov %r8d, 0x68(%r10); 将自增后r8d里的新值复制回static变量i中（上面三行是i++的流程）
 * lock addl $0x0, (%rsp); 给rsp寄存器里的值加0，没有任何效果，关键在于前面的lock前缀，会导致cache line的刷新，从而实现变量i的volatile语义
 * test %eax, -0x242a056(%rip); 轮询SAFEPOINT的操作，可以无视
 * L0001，回到step 2
 * 也就是说，每次循环都会去访问一次stop变量，迟早会访问到stop被修改后的新值（不能确保），导致循环结束。
 * 这两种场景的区别主要在于第二种情况的循环中有对static volatile类型变量i的访问，导致jit编译时JVM无法做出激进的优化，是附加的效果。
 * 
 * 我自己对这个问题的思考：
 * https://www.zhihu.com/answer/270308453
 * 第一种情况即使没有jit的激进编译，也肯定会死循环吧。因为JMM规定共享变量存在于主内存中，每个线程操作共享变量的时候要将共享变量复制一份到线程自己的工作内存中。第一种情况由于Main线程先休眠了1秒，所以子线程肯定会先运行，子线程会把stop的值复制一份到自己的工作内存里面，这时stop的值是false。然后Main线程也把stop的纸复制一份发到自己的线程内存里面，然后将stop修为true，由于stop不是volatile变量，所以Main线程对stop的修改什么时候写会主内存还不确定，不过即使Main线程会立即把stop的值刷新到主内存中，子线程也感知不到啊，因为stop不是volatile类型的变量。所以子线程工作内存中的stop一直都是false，肯定会死循环。所以我认为即使没有jit的激进编译第一种情况也肯定会死循环。大湿，不知道小弟理解的对不对?
 */
public class VolatileBarrierExample01 {
	private static boolean stop = false;
	private static volatile int i = 0;
	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run(){
				long i=0;
				while(!stop){
					i++;//注意这个i是局部变量
					if (i>999999999999l && i <(999999999999l+10000)) {
						//Thread.sleep(1000);//加上行代码也会解决死循环问题,sleep会释CPU,但不会释放锁
						System.out.println(i + "非常奇怪,加了这个System.out.println进行打印就不会出现死循环了,不加System.out.println就会死循环");
					}
				}
			}
		});
		
		thread.start();//启动线程
		Thread.sleep(1000);//Main线程休眠1秒
		stop= true;
		System.out.println("Main线程已经把stop修改成true了,子线程的循环会结束吗?");
		thread.join();
	}
}
