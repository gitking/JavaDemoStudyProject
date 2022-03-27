package com.yale.test.run.demo;

//Executors.java，模拟juc的Executors
//http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/tip/src/share/classes/java/util/concurrent/Executors.java#l677 《 Executors$DelegatedExecutorService.submit() 》
public class Executors {

	/**
	 * 模拟Executors.createSingleExecutor
	 */
	public static TExecutorService create() {
		return new FinalizableDelegatedTExecutorService(new TThreadPoolExecutor());
	}
	
	static class FinalizableDelegatedTExecutorService extends DelegatedTExecutorService {
		FinalizableDelegatedTExecutorService(TExecutorService executor) {
			super(executor);
		}
		
		/**
		 * 析构函数中执行shutdown,修改线程池状态
		 * @throws Throwable
		 */
		@Override
		protected void finalize() throws Throwable{
			super.shutdown();
		}
	}
	
	static class DelegatedTExecutorService extends TExecutorService {
		protected TExecutorService e;
		
		public DelegatedTExecutorService(TExecutorService executor) {
			this.e = executor;
		}
		
		@Override
		public void execute() {
			e.execute();
		}
		
		@Override
		public void shutdown() {
			e.shutdown();
		}
	}
}
