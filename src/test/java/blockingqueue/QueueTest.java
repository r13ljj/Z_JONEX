package blockingqueue;

import scala.concurrent.java8.FuturesConvertersImpl;

/**
 * <pre>
 *
 *  File: QueueTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/5				lijunjun				Initial.
 *
 * </pre>
 */
public class QueueTest {

    public static void main(String[] args)throws Exception {
        NativeSynchronousQueue<Integer> queue = new NativeSynchronousQueue();
        final int count = 1000000;
        Thread t1 = new Thread(){
            @Override
            public void run() {
                try {
                    for (int i=0; i<count; i++){
                        queue.put(new Integer(1));
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };
        Thread t2 = new Thread(){
            @Override
            public void run() {
                try {
                    Object msg = null;
                    while((msg = queue.take()) != null){
                        System.out.println("msg:"+msg);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        };


    }

}
