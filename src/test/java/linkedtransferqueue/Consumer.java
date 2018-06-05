package linkedtransferqueue;

/**
 *
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TransferQueue;

public class Consumer implements Runnable {
    private final TransferQueue<String> queue;

    public Consumer(TransferQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            String msg = "";
            long start = 0;
            while((msg = queue.take()) != null){
                if (start == 0) {
                    start = System.currentTimeMillis();
                }
                System.out.println(" Consumer " + Thread.currentThread().getName() +":"+ msg+" ct:"+(System.currentTimeMillis()-start)+"ms");
            }
        } catch (InterruptedException e) {
        }
    }
}
