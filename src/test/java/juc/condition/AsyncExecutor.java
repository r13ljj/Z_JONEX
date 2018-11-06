package juc.condition;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xubai on 2018/11/05 3:01 PM.
 */
public class AsyncExecutor<T> {

    private static final AtomicInteger executedCount = new AtomicInteger(0);
    private static ExecutorService threadpool = Executors.newFixedThreadPool(32, new ThreadFactory() {
        private AtomicInteger index = new AtomicInteger(0);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "async_thread_"+index.getAndIncrement());
        }
    });

    private List<Future<T>> futures = new ArrayList<>();
    private List<T> results = new ArrayList<>();

    private List<Condition> conditions = new ArrayList<>();

    protected ReentrantLock lock = new ReentrantLock(true);

    private volatile AtomicBoolean executed = new AtomicBoolean(false);


    //public AsyncExecutor(ReentrantLock lock) {
    //    this.lock = lock;
    //}

    public void join(Callable<T> task){
        //noop
        Future<T> future = threadpool.submit(task);
        futures.add(future);
    }

    public List<T> actionGet()
    {
        try
        {
            futures.forEach(tFuture -> {
                try {
                    T t = tFuture.get();
                    results.add(t);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            executed.compareAndSet(false, true);
        }
        //notifyAll
        if (conditions != null && conditions.size() > 0){
            lock.lock();
            conditions.forEach(condition -> {
                condition.signal();
            });
            lock.unlock();
        }
        executedCount.incrementAndGet();
        return results;
    }

    public List<T> get(){
        if (executed.get() == false){
            this.actionGet();
        }
        return results;
    }

    public static long getExecutedCount(){
        return executedCount.get();
    }


    public void addCondition(Condition condition){
        conditions.add(condition);
    }



}
