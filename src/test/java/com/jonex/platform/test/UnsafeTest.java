package com.jonex.platform.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import sun.misc.Unsafe;

public class UnsafeTest {
	
	private SomeThing object;
	
	private Object value;
	private final static Unsafe unsafe = getUnsafe1();
	private final static long valueOffset;
	
	static{
		try {
			valueOffset = unsafe.objectFieldOffset(UnsafeTest.class.getDeclaredField("value"));
		} catch (Exception e) {
			throw new Error(e);
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
	    final int THREADS_COUNT = 20;
	    final int LOOP_COUNT = 100000;

	    long sum = 0;
	    long min = Integer.MAX_VALUE;
	    long max = 0;
	    for(int n = 0;n <= 100;n++) {
	        final UnsafeTest basket = new UnsafeTest();
	        List<Thread> putThreads = new ArrayList<Thread>();
	        List<Thread> takeThreads = new ArrayList<Thread>();
	        for (int i = 0; i < THREADS_COUNT; i++) {
	            putThreads.add(new Thread() {
	                @Override
	                public void run() {
	                    for (int j = 0; j < LOOP_COUNT; j++) {
	                        basket.create();
	                    }
	                }
	            });
	            takeThreads.add(new Thread() {
	                @Override
	                public void run() {
	                    for (int j = 0; j < LOOP_COUNT; j++) {
	                        basket.get().getStatus();
	                    }
	                }
	            });
	        }
	        long start = System.nanoTime();
	        for (int i = 0; i < THREADS_COUNT; i++) {
	            takeThreads.get(i).start();
	            putThreads.get(i).start();
	        }
	        for (int i = 0; i < THREADS_COUNT; i++) {
	            takeThreads.get(i).join();
	            putThreads.get(i).join();
	        }
	        long end = System.nanoTime();
	        long period = end - start;
	        if(n == 0) {
	            continue;    //由于JIT的编译，第一次执行需要更多时间，将此时间不计入统计
	        }
	        sum += (period);
	        System.out.println(period);
	        if(period < min) {
	            min = period;
	        }
	        if(period > max) {
	            max = period;
	        }
	    }
	    System.out.println("Average : " + sum / 100);
	    System.out.println("Max : " + max);
	    System.out.println("Min : " + min);
	}
	
	public void create() {
        SomeThing temp = new SomeThing();
        //将value赋null值只是一项无用操作，实际利用的是这条语句的内存屏障
        unsafe.putOrderedObject(this, valueOffset, null);
        object = temp;
    }

    public SomeThing get() {
        while (object == null) {
            Thread.yield();
        }
        return object;
    }
	
	
	private static Unsafe getUnsafe1(){
		try {
			Field f = Unsafe.class.getDeclaredField("theUnsafe");
			f.setAccessible(true);
			return (Unsafe)f.get(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Unsafe getUnsafe2(){
		try {
			Method m = Unsafe.class.getDeclaredMethod("getUnsafe", null);
			m.setAccessible(true);
			return (Unsafe)m.invoke(Unsafe.class, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static class SomeThing {
        private int status;

        public SomeThing() {
            status = 1;
        }

        public int getStatus() {
            return status;
        }
    }

}
