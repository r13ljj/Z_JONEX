package linkedtransferqueue;

/**
 *
 */
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TransferQueue;

public class Consumer implements Runnable {
    private final BlockingQueue<String> queue;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            String msg = "";
            while((msg = queue.take()) != null){
                System.out.println(" Consumer " + Thread.currentThread().getName()
                        + msg);
            }
        } catch (InterruptedException e) {
        }
    }
}
