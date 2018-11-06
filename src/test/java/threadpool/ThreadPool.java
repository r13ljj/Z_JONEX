package threadpool;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by xubai on 2018/11/06 2:31 PM.
 */
public class ThreadPool {

    private int nWorkers;
    private Worker[] workers;
    private LinkedBlockingQueue<Task> queue;

    //private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(false);

    public ThreadPool(int nWorkers) {
        this.nWorkers = nWorkers;
        this.queue = new LinkedBlockingQueue<>();
        workers = new Worker[nWorkers];
        for (int i = 0; i < nWorkers; i++) {
            workers[i] = new Worker();
            workers[i].start();
        }
    }

    public void execute(Task task){
        synchronized (queue){
            queue.add(task);
            queue.notify();
        }
        /*try {
            readWriteLock.writeLock().lock();
            queue.add(task);
            queue.notify();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }*/
    }

    class Worker extends Thread{

        @Override
        public void run() {
            Runnable task;
            while (true){
                synchronized (queue){
                    if (queue.isEmpty()){
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    task = queue.poll();
                }
                /*try {
                    readWriteLock.readLock().lock();
                    task = queue.poll();
                } finally {
                    readWriteLock.readLock().unlock();
                }*/
                if (task != null){
                    try {
                        task.run();
                    } catch (Exception e) {
                        System.out.printf("execute task:%s exception", task);
                    }
                }
            }
        }

    }

}
