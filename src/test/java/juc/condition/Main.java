package juc.condition;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xubai on 2018/11/05 2:36 PM.
 */
public class Main {

    private final static Random RANDOM = new Random();

    public static ExecutorService workers = Executors.newFixedThreadPool(30, new ThreadFactory() {
        private AtomicInteger index = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "worker"+index.getAndIncrement());
        }
    });


    public static void main(String[] args)throws Exception {
        for (int i = 0; i < 1000; i++) {
            workers.submit(new Worker("search_params_"+RANDOM.nextInt(100)));
        }
        //Thread.sleep(15000L);
        //System.out.println("=====executedCount:"+AsyncExecutor.getExecutedCount());
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("=====executedCount:"+AsyncExecutor.getExecutedCount());
            }
        }, 10, 60, TimeUnit.SECONDS);
    }


}
