package paralexecutor.future;

import paralexecutor.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * LinkedTransferQueue
 *
 */
public class FutureExecutors {

    private static final ExecutorService thread_pool = new ThreadPoolExecutor(10, 100,
            300, TimeUnit.SECONDS,
            new LinkedTransferQueue<>());
    private CountDownLatch latch;

    public Object execute(List<Task> tasks){
        latch = new CountDownLatch(tasks.size());
        List<Future<Object>> futures = new ArrayList<>();
        List<Object> results = null;
        try {
            for(Task task : tasks){
                futures.add(thread_pool.submit(new TaskCallable(task, latch)));
            }
            latch.await();
            System.out.println("latch is unlock");
            results = new ArrayList<>();
            for(Future f : futures){
                results.add(f.get());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return results;
    }
}
