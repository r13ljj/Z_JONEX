package pingpong;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *
 *  File: CASApp.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/6				lijunjun				Initial.
 *
 * </pre>
 */
public class CASApp {
    private static int hit = 1000000;

    private static long startTime = 0;

    public static void main(String[] args) throws Exception{
        Field hitField = CASApp.class.getDeclaredField("hit");
        long address = UNSAFE.staticFieldOffset(hitField);
        Thread pingThread = new Thread(new Ping(address));
        Thread pongThread = new Thread(new Pong(address));
        pingThread.start();
        pongThread.start();
    }

    static class Ping implements Runnable{

        private AtomicInteger counter = new AtomicInteger(0);
        private long hitAddress;

        public Ping(long hitAddress) {
            this.hitAddress = hitAddress;
        }

        @Override
        public void run() {
            long local = 0;
            while (UNSAFE.compareAndSwapLong(null, hitAddress, local-1, local-2)){
                if (startTime == 0){
                    startTime = System.currentTimeMillis();
                }
                if (local <= 0){
                    System.out.println("shareVariables ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                local -= 2;
                if (local % 2 == 1){
                    System.out.println("ping"+counter.incrementAndGet()+":"+local);
                }
            }
        }
    }

    static class Pong implements Runnable{

        private AtomicInteger counter = new AtomicInteger(0);
        private long hitAddress;

        public Pong(long hitAddress) {
            this.hitAddress = hitAddress;
        }

        @Override
        public void run() {
            long local = 0;
            while (UNSAFE.compareAndSwapLong(null, hitAddress, local-1, local-2)){
                if (startTime == 0){
                    startTime = System.currentTimeMillis();
                }
                if (local <= 0){
                    System.out.println("shareVariables ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                local -= 2;
                if (local % 2 == 0){
                    System.out.println("ping"+counter.incrementAndGet()+":"+local);
                }
            }
        }
    }


    static final Unsafe UNSAFE;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}
