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
        Object objectBase = UNSAFE.staticFieldBase(hitField);
        Thread pingThread = new Thread(new Ping(objectBase, address));
        Thread pongThread = new Thread(new Pong(objectBase, address));
        pingThread.start();
        pongThread.start();
    }

    static class Ping implements Runnable{

        private Object objectBase;
        private long hitAddress;

        public Ping(Object object, long hitAddress) {
            this.objectBase = object;
            this.hitAddress = hitAddress;
        }

        @Override
        public void run() {
            System.out.println("hitAddress:"+hitAddress);
            int val = UNSAFE.getInt(objectBase, hitAddress);
            System.out.println("val:"+val);
            long local = 0L;
            while (true){
                local = UNSAFE.getInt(objectBase, hitAddress);
                if (startTime == 0){
                    startTime = System.currentTimeMillis();
                }
                if (local <= 0){
                    System.out.println("shareVariables ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                if (local % 2 == 1) {
                    if (UNSAFE.compareAndSwapLong(objectBase, hitAddress, local, --local)){
                        System.out.println("ping:"+local);
                    }
                }/*else{
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }*/
            }
        }
    }

    static class Pong implements Runnable{

        private Object objectBase;
        private long hitAddress;

        public Pong(Object object, long hitAddress) {
            this.objectBase = object;
            this.hitAddress = hitAddress;
        }

        @Override
        public void run() {
            System.out.println("hitAddress:"+hitAddress);
            int val = UNSAFE.getInt(objectBase, hitAddress);
            System.out.println("val:"+val);
            long local = 0L;
            while (true){
                local = UNSAFE.getInt(objectBase, hitAddress);
                if (startTime == 0){
                    startTime = System.currentTimeMillis();
                }
                if (local <= 0){
                    System.out.println("shareVariables ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                if (local % 2 == 0) {
                    if (UNSAFE.compareAndSwapLong(objectBase, hitAddress, local, --local)){
                        System.out.println("pong:"+local);
                    }
                }/*else{
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }*/
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
