package linkedtransferqueue;

/**
 *
 */
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
            while((msg = queue.take()) != null){
                System.out.println(" Consumer " + Thread.currentThread().getName() +":"+ msg);
            }
        } catch (InterruptedException e) {
        }
    }

}
