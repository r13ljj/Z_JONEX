package forkjoin.response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * getPoolSize(): 此方法返回 int 值，它是ForkJoinPool内部线程池的worker线程们的数量。
 * getParallelism(): 此方法返回池的并行的级别。
 * getActiveThreadCount(): 此方法返回当前执行任务的线程的数量。
 * getRunningThreadCount():此方法返回没有被任何同步机制阻塞的正在工作的线程。
 * getQueuedSubmissionCount(): 此方法返回已经提交给池还没有开始他们的执行的任务数。
 * getQueuedTaskCount(): 此方法返回已经提交给池已经开始他们的执行的任务数。
 * hasQueuedSubmissions(): 此方法返回 Boolean 值，表明这个池是否有queued任务还没有开始他们的执行。
 * getStealCount(): 此方法返回 long 值，worker 线程已经从另一个线程偷取到的时间数。
 * isTerminated(): 此方法返回 Boolean 值，表明 fork/join 池是否已经完成执行。
 */
public class Main {

    public static void main(String[] args) {
        Document mock=new Document();
        String[][] document=mock.generateDocument(100, 1000, "the");

        DocumentTask task=new DocumentTask(document, 0, 100, "the");
        long start = System.currentTimeMillis();
        ForkJoinPool pool=new ForkJoinPool();
        pool.execute(task);

        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n",pool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n",pool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n",pool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n",pool.getStealCount());
            System.out.printf("******************************************\n");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());
        System.out.println("Main: The word appears ct:"+(System.currentTimeMillis()-start)+"ms");
        pool.shutdown();

        try {
            System.out.printf("Main: The word appears %d in the document",task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        try {
            System.out.printf("Main: The word appears %d in the document",task.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

}
