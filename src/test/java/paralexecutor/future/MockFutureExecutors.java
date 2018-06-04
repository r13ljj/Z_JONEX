package paralexecutor.future;

import paralexecutor.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *
 *  File: MockFutureExecutors.java
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
public class MockFutureExecutors {

    //private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();
        for (int i=0; i<10; i++){
            tasks.add(new FutureTask());
        }
        FutureExecutors executors = new FutureExecutors();
        Object result = executors.execute(tasks);
        System.out.println("future execute result:"+result);
        Runtime.getRuntime().exit(0);
    }


    static class FutureTask extends Task{

        @Override
        public Object execute() {
            int a = RANDOM.nextInt(10);
            try {
                TimeUnit.SECONDS.sleep(Long.valueOf(a));
                System.out.println("future task execute ct:"+a+"s");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return a;
        }
    }
}
