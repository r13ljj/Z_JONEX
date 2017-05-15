package com.jonex.platform.test;

public class FalseSharing2 implements Runnable{
	
	public static final long ITERATIONS = 500L * 1000L * 100L;
	private int arrayIndex = 0;
	
	private static ValueNoPadding[] longs;
	
	public FalseSharing2(final int index){
		this.arrayIndex = index;
	}
	
	private static void runTest(int threadNum)throws InterruptedException{
		Thread[] threads = new Thread[threadNum];
		longs = new ValueNoPadding[threadNum];
		for(int i=0; i<longs.length; i++){
			longs[i] = new ValueNoPadding();
		}
		for(int i=0; i<threads.length; i++){
			threads[i] = new Thread(new FalseSharing2(i));
		}
		for(Thread t : threads){
			t.start();
		}
		for(Thread t : threads){
			t.join();
		}
	}
	
	@Override
	public void run() {
		long i = ITERATIONS + 1;
		while(0 != --i){
			longs[arrayIndex].value = 0L;
		}
	}
	
	public static void main(String[] args)throws Exception{
		for(int i=1; i<=15; i++){
			System.gc();
			final long start = System.currentTimeMillis();
			runTest(i);
			System.out.println("Thread num "+i+" duration = "+(System.currentTimeMillis()-start));
		}
	}
	
	public final static class ValuePadding{
		protected long p1, p2, p3, p4, p5, p6, p7;
		protected volatile long value = 0L;
		protected long p9, p10, p11, p12, p13, p14;
		protected long p15;
	}
	
	public final static class ValueNoPadding{
		//protected long p1, p2, p3, p4, p5, p6, p7;
        protected volatile long value = 0L;
        //long p9, p10, p11, p12, p13, p14, p15;
	}

}
