package com.yale.test.thread.heima.zhangxiaoxiang;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/*
 * 使用ForkJoin
 * Java 7开始引入了一种新的Fork/Join线程池，它可以执行一种特殊的任务：把一个大任务拆成多个小任务并行执行。
 * 我们举个例子：如果要计算一个超大数组的和，最简单的做法是用一个循环在一个线程内完成：
 *  ┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
 * 还有一种方法，可以把数组拆成两部分，分别计算，最后加起来就是最终结果，这样可以用两个线程并行执行：
	┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
	┌─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┴─┘
	如果拆成两部分还是很大，我们还可以继续拆，用4个线程并行执行：
	┌─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┘
	┌─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┘
	┌─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┘
	┌─┬─┬─┬─┬─┬─┐
	└─┴─┴─┴─┴─┴─┘
 * 这就是Fork/Join任务的原理：判断一个任务是否足够小，如果是，直接计算，否则，就分拆成几个小任务分别计算。这个过程可以反复“裂变”成一系列小任务。
 * 我们来看如何使用Fork/Join对大数据进行并行求和：
 * 小结
 * Fork/Join是一种基于“分治”的算法：通过分解任务，并行执行，最后合并结果得到最终结果。
 * ForkJoinPool线程池可以把一个大任务分拆成小任务并行执行，任务类必须继承自RecursiveTask或RecursiveAction。
 * 使用Fork/Join模式可以进行并行计算以提高效率。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1306581226487842
 * https://zhuanlan.zhihu.com/p/26685513 
 * Java8直接用parallelStream不行吗
 */
public class ForkJoinDemo {
	static Random random = new Random(0);
	static long random() {
		return random.nextInt(10000);
	}
	public static void main(String[] args) {
		//创建2000个随机数,组成的数组:
		long[] array = new long[2000];
		long expectedSum = 0;
		for (int i=0; i< array.length; i++) {
			array[i] = random();
			expectedSum += array[i];
		}
		System.out.println("Excepted sum:" + expectedSum);
		
		/*
		 * fork/join:
		 * 观察下述代码的执行过程，一个大的计算任务0~2000首先分裂为两个小任务0~1000和1000~2000，这两个小任务仍然太大，继续分裂为更小的0~500，500~1000，1000~1500，1500~2000，最后，计算结果被依次合并，得到最终结果。
		 * 因此，核心代码SumTask继承自RecursiveTask，在compute()方法中，关键是如何“分裂”出子任务并且提交子任务：
		 * Fork/Join线程池在Java标准库中就有应用。Java标准库提供的java.util.Arrays.parallelSort(array)可以进行并行排序，
		 * 它的原理就是内部通过Fork/Join对大数组分拆进行并行排序，在多核CPU上就可以大大提高排序的速度。
		 */
		ForkJoinTask<Long> task = new SumTask(array, 0, array.length);
		long startTime = System.currentTimeMillis();
		// ForkJoinPool fjp = new ForkJoinPool(4); // 最大并发数4
	    //fjp.invoke(task);关键代码是fjp.invoke(task)来提交一个Fork/Join任务并发执行，然后获得异步执行的结果。
		//JDK用来执行Fork/Join任务的工作线程池大小等于CPU核心数。在一个4核CPU上，最多可以同时执行4个子任务。
		Long result = ForkJoinPool.commonPool().invoke(task);
		long endTime = System.currentTimeMillis();
		System.out.println("Fork/join sum: " + result + " in " + (endTime - startTime) + " ms.");
	}
}

/*
 * RecursiveTask实际上是Future实现类
 */
class SumTask extends RecursiveTask<Long> {
	static final int THRESHOLD = 500 ;
	long [] array;
	int start;
	int end;
	SumTask(long[] array, int start, int end){
		this.array = array;
		this.start = start;
		this.end = end;
	}
	
	@Override
	protected Long compute() {
		if (end - start <= THRESHOLD) {//如果任务足够小,直接计算
			long sum = 0;
			for (int i=start; i<end; i++) {
				sum += this.array[i];
				try {
					Thread.sleep(1);//故意放慢计算速度
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return sum;
		}
		//任务太大,则一分为二
		int middle = (end + start) / 2;
		System.out.println(String.format("split %d~%d ==> %d~%d, %d~%d", start, end, start, middle, middle, end));
		// “分裂”子任务:
		SumTask subtask1 = new SumTask(this.array, start, middle);
		SumTask subtask2 = new SumTask(this.array, middle, end);
		
		/*
		 * 分别对子任务调用fork():
	     * subtask1.fork();
	     * subtask2.fork();很遗憾，fork()这种写法是错！误！的！这样写没有正确理解Fork/Join模型的任务执行逻辑。
	     * JDK用来执行Fork/Join任务的工作线程池大小等于CPU核心数。在一个4核CPU上，最多可以同时执行4个子任务。对400个元素的数组求和，执行时间应该为1秒。但是，换成上面的代码，执行时间却是两秒。
	     * 这是因为执行compute()方法的线程本身也是一个Worker线程，当对两个子任务调用fork()时，这个Worker线程就会把任务分配给另外两个Worker，但是它自己却停下来等待不干活了！这样就白白浪费了Fork/Join线程池中的一个Worker线程，导致了4个子任务至少需要7个线程才能并发执行。
		 * 其实，我们查看JDK的invokeAll()方法的源码就可以发现，invokeAll的N个任务中，其中N-1个任务会使用fork()交给其它线程执行，
		 * 但是，它还会留一个任务自己执行，这样，就充分利用了线程池，保证没有空闲的不干活的线程。
		 * https://zhuanlan.zhihu.com/p/26685513
		 */
		invokeAll(subtask1, subtask2); // invokeAll会并行运行两个子任务:
		System.out.println("这里会阻塞,,," + middle + "然后invokeAll递归调用子任务");
		Long subresult1 = subtask1.join();//等待获取获得子任务的结果:
		Long subresult2 = subtask2.join();
		Long result = subresult1 + subresult2;// 汇总结果:
		System.out.println("result = " + subresult1 + " + " + subresult1 + " ==> " + result);
		return result;
	}
}
