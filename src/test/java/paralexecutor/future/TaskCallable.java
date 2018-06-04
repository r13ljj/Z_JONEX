package paralexecutor.future;

import paralexecutor.Task;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 *
 *  File: TaskCallable.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/1				lijunjun				Initial.
 *
 * </pre>
 */
public class TaskCallable implements Callable<Object> {

    private Task task;
    private CountDownLatch latch;

    public TaskCallable(Task task, CountDownLatch latch) {
        this.task = task;
        this.latch = latch;
    }

    @Override
    public Object call() throws Exception {
        Object result = task.execute();
        latch.countDown();
        return result;
    }
}
