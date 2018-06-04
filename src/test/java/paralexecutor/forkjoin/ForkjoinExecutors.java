package paralexecutor.forkjoin;

import paralexecutor.Task;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

/**
 * <pre>
 *
 *  File: ForkjoinExecutors.java
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
public class ForkjoinExecutors {

    private static final ForkjoinExecutors FORK_JOIN_EXECUTOR = new ForkjoinExecutors();

    private ForkJoinPool forkJoinPool;


    private ForkjoinExecutors() {
        forkJoinPool = new ForkJoinPool();
    }

    public static ForkjoinExecutors getInstance(){
        return FORK_JOIN_EXECUTOR;
    }

    public Object execute(List<Task> tasks){
        TaskAdaptor taskAdaptor = new TaskAdaptor(tasks);
        forkJoinPool.execute(taskAdaptor);
        Object result = null;
        try {
            result = taskAdaptor.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }
}
