package pingpong;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 * <pre>
 *
 *  File: TransferQueueApp.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/8				lijunjun				Initial.
 *
 * </pre>
 */
public class TransferQueueApp {
    static TransferQueue<Integer> pingQueue = new LinkedTransferQueue<>();
    static TransferQueue<Integer> pongQueue = new LinkedTransferQueue<>();

    static long startTime = 0L;

    public static void main(String[] args) throws Exception {
        Thread pingThread = new Thread(new Ping());
        Thread pongThread = new Thread(new Pong());
        pingThread.start();
        pongThread.start();
        startTime = System.currentTimeMillis();
        pingQueue.transfer(1000000);
    }

    static class Ping implements Runnable{


        @Override
        public void run() {
            while(true){
                try {
                    Integer msg = pingQueue.take();
                    if (msg != null && msg <= 0){
                        System.out.println("transfer queue ct:"+(System.currentTimeMillis()-startTime)+"ms");
                        return;
                    }
                    if (msg != null && msg % 2 == 0){
                        System.out.println("ping:"+msg);
                        pongQueue.transfer(--msg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    static class Pong implements Runnable{


        @Override
        public void run() {
            while(true){
                try {
                    Integer msg = pongQueue.take();
                    if (msg != null && msg <= 0){
                        System.out.println("transfer queue ct:"+(System.currentTimeMillis()-startTime));
                        return;
                    }
                    if (msg != null && msg % 2 == 1){
                        System.out.println("pong:"+msg);
                        pingQueue.transfer(--msg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

}
