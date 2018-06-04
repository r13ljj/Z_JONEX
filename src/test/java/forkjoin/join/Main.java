package forkjoin.join;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 *
 * get()和join()有两个主要的区别：
 *
 * join()方法不能被中断。如果你中断调用join()方法的线程，这个方法将抛出InterruptedException异常。
 * 如果任务抛出任何未受检异常，get()方法将返回一个ExecutionException异常，而join()方法将返回一个RuntimeException异常。
 *
 */
public class Main {

    public static void main(String[] args) throws Exception{

        List<Long> data = new ArrayList<>();
        for (int i=0; i<1000; i++){
            data.add(Long.valueOf(i+1));
        }
        long start = System.currentTimeMillis();
        Task task = new Task(0, 1000, data);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.execute(task);
        do {
            System.out.printf("******************************************\n");
            System.out.printf("Main: Parallelism: %d\n",forkJoinPool.getParallelism());
            System.out.printf("Main: Active Threads: %d\n",forkJoinPool.getActiveThreadCount());
            System.out.printf("Main: Task Count: %d\n",forkJoinPool.getQueuedTaskCount());
            System.out.printf("Main: Steal Count: %d\n",forkJoinPool.getStealCount());
            System.out.printf("******************************************\n");
            /*try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        } while (!task.isDone());
        System.out.println("Main: The word appears ct:"+(System.currentTimeMillis()-start)+"ms");
        forkJoinPool.shutdown();
        Long result = task.get();
        System.out.println("==========="+result);
    }

    static class Task extends RecursiveTask<Long> {

        private int start;
        private int end;
        private List<Long> data;

        public Task(int start, int end, List<Long> data) {
            this.start = start;
            this.end = end;
            this.data = data;
        }

        @Override
        protected Long compute() {
            Long result = 0L;
            if (end-start < 100){
                result = computeSelf();
            }else{
                int middle = (start + end) /2;
                Task subTask1 = new Task(start, middle, data);
                Task subTask2 = new Task(middle, end, data);
                //join()方法，这将等待任务执行的完成，并且返回任务的结果
                //subTask1.fork();
                //subTask2.fork();
                //subTask1.join();
                //subTask2.join();
                invokeAll(subTask1, subTask2);
                try {
                    //result += subTask1.get();
                    //result += subTask2.get();
                    Long leftResult = subTask1.join();
                    Long rightResult = subTask2.compute();
                    result += leftResult;
                    result += rightResult;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return result;
        }

        private Long computeSelf(){
            Long r = 0L;
            for(int i=start; i<end; i++){
                r += i;
            }
            return r;
        }

    }

}
