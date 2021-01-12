package com.yale.test.java.fanshe.perfma;

/**
 * volatile解决多线程内存不可见问题。对于一写多读，是可以解决变量同步问题，但是如果多写，同样无法解决线程安全问题。
	说明：如果是count++操作，使用如下类实现：AtomicInteger count = new AtomicInteger(); count.addAndGet(1); 
	如果是JDK8，推荐使用LongAdder对象，比AtomicLong性能更好（减少乐观锁的重试次数）。《阿里巴巴Java开发手册（泰山版）.
 * 
 * 为什么要对线程间共享的变量用关键字volatile声明？这涉及到Java的内存模型。在Java虚拟机中，变量的值保存在主内存中，但是，当线程访问变量时，它会先获取一个副本，并保存在自己的工作内存中。
 * 如果线程修改了变量的值，虚拟机会在某个时刻把修改后的值回写到主内存，但是，这个时间是不确定的！
 *  ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
	           Main Memory
	│                               │
	   ┌───────┐┌───────┐┌───────┐
	│  │ var A ││ var B ││ var C │  │
	   └───────┘└───────┘└───────┘
	│     │ ▲               │ ▲     │
	 ─ ─ ─│─│─ ─ ─ ─ ─ ─ ─ ─│─│─ ─ ─
	      │ │               │ │
	┌ ─ ─ ┼ ┼ ─ ─ ┐   ┌ ─ ─ ┼ ┼ ─ ─ ┐
	      ▼ │               ▼ │
	│  ┌───────┐  │   │  ┌───────┐  │
	   │ var A │         │ var C │
	│  └───────┘  │   │  └───────┘  │
	   Thread 1          Thread 2
	└ ─ ─ ─ ─ ─ ─ ┘   └ ─ ─ ─ ─ ─ ─ ┘
 * 这会导致如果一个线程更新了某个变量，另一个线程读取的值可能还是更新前的。例如，主内存的变量a = true，线程1执行a = false时，它在此刻仅仅是把变量a的副本变成了false，
 * 主内存的变量a还是true，在JVM把修改后的a回写到主内存之前，其他线程读取到的a的值仍然是true，这就造成了多线程之间共享的变量不一致。
 * 因此，volatile关键字的目的是告诉虚拟机：
 * 	每次访问变量时，总是获取主内存的最新值；
 * 	每次修改变量后，立刻回写到主内存。
 * volatile关键字解决的是可见性问题：当一个线程修改了某个共享变量的值，其他线程能够立刻看到修改后的值。
 * 如果我们去掉volatile关键字，运行上述程序，发现效果和带volatile差不多，这是因为在x86的架构下，JVM回写主内存的速度非常快，但是，换成ARM的架构，就会有显著的延迟。
 * x86架构加不加volatile其实区别不大，其他架构要注意，很可能一个线程改了值几秒内另一个线程读的还是旧的
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306580767211554
 * volatile只保证：
 * 读主内存到本地副本；
 * 操作本地副本；
 * 回写主内存。
 * 这3步多个线程可以同时进行。
 * https://mp.weixin.qq.com/s/x78EZQ0E0fgKSwGdK5vtwg
 * @author dell
 */
public class VolatileDemo {
	static int num = 0;
	public static void main(String[] args) throws InterruptedException {
		/*
		 * https://club.perfma.com/question/267086
		 * 按道理，子线程会通过num++操作破坏while循环的条件，从而终止循环，执行最后的输出操作。
		 * 但在我的多次运行中，偶尔会出现while循环一直不结束的场合。像我截图一样，程序一直不终止。
		 * JDK7、JDK8均已试验，均能偶然触发。
		 * 答案由亮哥提供：https://www.zhihu.com/question/263528143/answer/270308453
		 * 简单说来，是因为JIT激进编译导致的问题。
		 * volatile只是恰巧阻止了JIT的激进编译，所以这里主要的问题不是可见性。因为哪怕变量不是volatile修饰，只要加上-Xint、-XX:-UseOnStackReplacement参数，问题一样不会出现。
		 * 和jit也是有一定关系的，-Xint设定解释执行，也可以只关闭OSR看看，-XX:-UseOnStackReplacement
		 * 大空翼 加-Xint、-XX:-UseOnStackReplacement都能解决问题。
		 * 
		 * 因为jit优化导致的 while 会变成true ，解释执行情况下或关闭jit优化则不会进行优化 而且解释执行情况下getstatic指令是从内存读取值的。所以是始终可见，但因为cpu重排关系有可能不是实时可见 ，不好测
		 * 有个更好的例子 在别的变量加volatile https://www.zhihu.com/question/263528143/answer/270308453
		 * 和jit也是有一定关系的，-Xint设定解释执行，也可以只关闭OSR看看，-XX:-UseOnStackReplacement
		 * 
		 * 
		 * https://club.perfma.com/question/267086
		 * 按照我的理解,你这个程序如果Main线程在num=0时候先执行了while()循环,然后子线程再对num进行加1操作,肯定会出现死循环的情况。因为JMM规定普通的共享变量存在于主内存当中，然后每个线程都有自己的工作内存，每个线程用到变量的时候会先从主存中复制一份到自己的工作内存。就你这程序来说,如果Main线程在num=0时候先执行了while()循环,这个时候Main线程会把num的值复制一份到自己的工作内存,然后Main线程的while循环只会读线程工作内存的副本。接下来子线程开始运行,先把num的值复制一份到在子线程的工作内存里面,然后对num进行加1操作，然后子线程运行结束把num刷新到主存里面。注意，即使此时子线程把num的最新值刷新到主存中，Main线程的while循环也不会结束,因为Main线程只会读自己的工作内存里面num的值，Main线程的工作内存里面的num现在还是0啊。你的num没有用volatile修饰,所以Main线程里面的num不会失效。所以，我觉得你这个程序只要Main线程在num=0时候先执行了while()循环,然后子线程再对num进行加1操作,即使没有jit的激进编译,也肯定会出现死循环的情况。
		 */
		new Thread(()->{
			System.out.println("Child:" + num);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			num ++;
			System.out.println("Child End:" + num);
		}).start();
		//这个程序会出现死循环问题,但是加上-Xint、-XX:-UseOnStackReplacement就能解决问题。
		System.out.println("Main11111-》:" + num);
		long i= 0;
		while (num == 0) {
			i++;
			if (i>999999999999l && i <(999999999999l+10000)) {
				Thread.sleep(1000);//加上行代码也会解决死循环问题,sleep会释CPU,但不会释放锁
				//System.out.println(i + "非常奇怪,加了这个System.out.println进行打印就不会出现死循环了,不加System.out.println就会死循环");
			}
			//https://blog.csdn.net/C_AJing/article/details/103307797 System.out.println()对共享变量和多线程的影响，为什么会造成while循环终止
			//System.out.println("非常奇怪,加了这个System.out.println进行打印就不会出现死循环了,不加System.out.println就会死循环");
			
			//https://blog.csdn.net/weililansehudiefei/article/details/70904111?utm_medium=distribute.pc_relevant.none-task-blog-OPENSEARCH-5.control&depth_1-utm_source=distribute.pc_relevant.none-task-blog-OPENSEARCH-5.control
			//Thread.sleep(1000);加上行代码也会解决死循环问题,sleep会释CPU,但不会释放锁
			
			/**
			 * https://club.perfma.com/question/267086
			 * https://club.perfma.com/question/2079981
			 * 按照我的理解,你这个程序如果Main线程在num=0时候先执行了while()循环,然后子线程再对num进行加1操作,肯定会出现死循环的情况。
			 * 因为JMM规定普通的共享变量存在于主内存当中，然后每个线程都有自己的工作内存，每个线程用到变量的时候会先从主存中复制一份到自己的工作内存。
			 * 就你这程序来说,如果Main线程在num=0时候先执行了while()循环,这个时候Main线程会把num的值复制一份到自己的工作内存,
			 * 然后Main线程的while循环只会读线程工作内存的副本。接下来子线程开始运行,子线程先把num的值复制一份到子线程的工作内存里面,然后对num进行加1操作，
			 * 然后子线程运行结束把num刷新到主存里面。注意，即使此时子线程把num的最新值刷新到主存中，Main线程的while循环也不会结束,
			 * 因为Main线程只会读自己的工作内存里面num的值，Main线程的工作内存里面的num现在还是0啊。你的num没有用volatile修饰,
			 * 所以Main线程里面的num不会失效。所以，我觉得你这个程序只要Main线程在num=0时候先执行了while()循环,然后子线程再对num进行加1操作,
			 * 即使没有jit的激进编译,也肯定会出现死循环的情况。
			 * 我上面的说法也不对。我发现加了-Xint参数之后,就肯定不会出现死循环了。这是为什么呢?难道加了-Xint参数之后,Main线程每次循环的时候都会去主存中读取num的值吗？这跟JMM规定的不太一样啊？JMM规定普通的共享变量存在于主内存当中，然后每个线程都有自己的工作内存。
			 * https://www.zhihu.com/question/263528143/answer/270308453
			 * 其实可以用hsdis + jitwatch,看一下jit编译后的代码
			 * 你这个问题绝对跟JIT的激进编译没有关系，就是num变量的可见性问题,JMM的规范:每个线程都有自己的工作内存。
			 * 第一：你说的JDK7、JDK8均已试验，均能偶然触发。为啥偶然触发呢？是因为一旦Main线程在num=0时候先执行了while()循环,然后子线程再对num进行加1操作,就会出现死循环的情况。你说的偶然就看Main线程跟子线程谁先运行了。就像你评论里面回复别人的 ‘在num++前面加行Thread.sleep(1000)。’，你发现没?在num++前面加了Thread.sleep(1000)就百分之百会出现死循环,这是为啥呢？就是因为Main线程在num=0时候先执行了while()循环,然后子线程再对num进行加1操作,就会出现死循环的情况。
			 * 第二：为啥加了-Xint、-XX:-UseOnStackReplacement参数，问题就不会出现了？这个评论里面的人已经说出来了，加了-Xint解释执行情况下getstatic指令是从内存读取值的，所以Main线程每次循环的时候都是从主内存读值的,子线程修改了num的值,Main线程可以读到，所以加了-Xint解释执行肯定不会出现死循环。
			 */
		}
		System.out.println("Main:" + num);
	}
	
	/*
	 * 在早期的 CPU 中，是通过在总线上加 LOCK# 锁的形式来解决缓存不一致的问题。因为 CPU 和其他部件进行通信都是通过总线来进行的，如果对总线加 LOCK# 锁的话，
	 * 也就是说阻塞了其他 CPU 对其他部件访问（如内存），从而使得只能有一个 CPU 能使用这个变量的内存。由于在锁住总线期间，其他 CPU 无法访问内存，导致效率低下。
	 * 所以就出现了缓存一致性协议。最出名的就是 Intel 的 MESI 协议，MESI 协议保证了每个缓存中使用的共享变量的副本是一致的。它核心的思想是：当 CPU 写数据时，如果发现操作的变量是共享变量，即在其他 CPU 中也存在该变量的副本，
	 * 会发出信号通知其他 CPU 将该变量的缓存行置为无效状态，因此当其他 CPU 需要读取这个变量时，发现自己缓存中缓存该变量的缓存行是无效的，那么它就会从内存重新读取。
	 * 
	 * 看了那么多文章,其实我发现volatile只有俩个大的作用,第一个是禁止指令重排序。第二个就是可见性。什么是指令重排序？看下面的代码
	 * int i = 0;              
	 * boolean flag = false;
	 * i = 1;                //语句1  
	 * flag = true;          //语句2
	 * 其实真正运行的时候,语句1和语句2的顺序实际是会发生变化的,取决于指令怎么重排序了。
	 * 下面解释一下什么是指令重排序，一般来说，处理器为了提高程序运行效率，可能会对输入代码进行优化，它不保证程序中各个语句的执行先后顺序同代码中的顺序一致，但是它会保证程序最终执行结果和代码顺序执行的结果是一致的。
	 * 比如上面的代码中，语句 1 和语句 2 谁先执行对最终的程序结果并没有影响，那么就有可能在执行过程中，语句 2 先执行而语句 1 后执行。
	 * 但是要注意，虽然处理器会对指令进行重排序，但是它会保证程序最终结果会和代码顺序执行结果相同，那么它靠什么保证的呢？再看下面一个例子：
	 * int a = 10;    //语句1
	 * int r = 2;    //语句2
	 * a = a + 3;    //语句3
	 * r = a*a;     //语句4
	 * 这段代码有4个语句，那么可能的一个执行顺序是：
	 * 语句2  语句1  语句3  语句4
	 * 那么可不可能是这个执行顺序呢：语句2  语句1  语句4  语句3
	 * 不可能，因为处理器在进行重排序时是会考虑指令之间的数据依赖性，如果一个指令 Instruction 2  必须用到Instruction 1的结果，那么处理器会保证 Instruction 1会在 Instruction 2 之前执行。
	 * 虽然重排序不会影响单个线程内程序执行的结果，但是多线程呢？下面看一个例子：
	 * //线程1:
	 * context = loadContext();   //语句1
	 * inited = true;             //语句2
	 *	//线程2:
	 *	while(!inited ){
	 *	  sleep()
	 *	}
	 *	doSomethingwithconfig(context);
	 * 上面代码中，由于语句 1 和语句 2 没有数据依赖性，因此可能会被重排序。假如发生了重排序，在线程 1 执行过程中先执行语句 2，而此时线程 2 会以为初始化工作已经完成，那么就会跳出 while 循环，去执行doSomethingwithconfig(context)方法，而此时 context 并没有被初始化，就会导致程序出错。
	 * 从上面可以看出，指令重排序不会影响单个线程的执行，但是会影响到线程并发执行的正确性。
	 * 也就是说，要想并发程序正确地执行，必须要保证原子性、可见性以及有序性。只要有一个没有被保证，就有可能会导致程序运行不正确。
	 * 请分析以下哪些操作是原子性操作：
	 * x = 10;         //语句1
	 * y = x;         //语句2
	 * x++;           //语句3
	 * x = x + 1;     //语句4
	 * 咋一看，有些朋友可能会说上面的 4 个语句中的操作都是原子性操作。其实只有语句 1 是原子性操作，其他三个语句都不是原子性操作。
	 * java中 只有简单的读取、赋值（而且必须是将数字赋值给某个变量，变量之间的相互赋值不是原子操作）才是原子操作。
	 */
}
