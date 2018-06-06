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
public class ShareVariablesApp {

    private static volatile AtomicInteger hit = new AtomicInteger(1000000);

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
                if (hit.get() <= 0){
                    System.out.println("shareVariables ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                if (hit.get() % 2 == 1){
                    System.out.println("ping"+counter.incrementAndGet()+":"+hit.getAndDecrement());
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
                if (hit.get() <= 0){
                    System.out.println("shareVariables ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    break;
                }
                if (hit.get() % 2 == 0){
                    System.out.println("pong"+counter.incrementAndGet()+":"+hit.getAndDecrement());
                }
            }
        }
    }


}
