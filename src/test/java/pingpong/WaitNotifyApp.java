package pingpong;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * <pre>
 *
 *  File: WaitNotifyApp.java
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
public class WaitNotifyApp {

    private static Object mutex = new Object();

    private static int hit = 1000000;

    private static long startTime = 0;

    public static void main(String[] args) {

        Thread pingThread = new Thread(new Ping());
        Thread pongThread = new Thread(new Pong());
        pingThread.start();
        pongThread.start();
    }


    static class Ping implements Runnable{

        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public void run() {
            for (;;){
                if (startTime == 0){
                    startTime = System.currentTimeMillis();
                }
                if (hit <= 0){
                    System.out.println("wait/nofifyAll ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                synchronized (mutex){
                    if (hit % 2 != 1){
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("ping"+counter.incrementAndGet()+":"+(hit--));
                    mutex.notifyAll();
                }
            }
        }
    }

    static class Pong implements Runnable{

        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        public void run() {
            for (;;){
                if (startTime == 0){
                    startTime = System.currentTimeMillis();
                }
                if (hit <= 0){
                    System.out.println("wait/nofifyAll ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                synchronized (mutex) {
                    if (hit % 2 != 0){
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    System.out.println("pong"+counter.incrementAndGet()+":"+(hit--));
                    mutex.notifyAll();
                }
            }
        }
    }


}
