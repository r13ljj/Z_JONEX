package blockingqueue;

import java.util.concurrent.Semaphore;

/**
 * <pre>
 *
 *  File: SemaphoreSynchronousQueue.java
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
public class SemaphoreSynchronousQueue<E> {
    E item = null;
    Semaphore sync = new Semaphore(0);
    Semaphore send = new Semaphore(1);
    Semaphore recv = new Semaphore(0);

    public E take() throws InterruptedException {
        recv.acquire();
        E x = item;
        sync.release();
        send.release();
        return x;
    }

    public void put (E x) throws InterruptedException{
        send.acquire();
        item = x;
        recv.release();
        sync.acquire();
    }
}
