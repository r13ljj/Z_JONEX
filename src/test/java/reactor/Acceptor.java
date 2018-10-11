package reactor;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by xubai on 2018/10/10 下午4:31.
 */
public class Acceptor implements Runnable{

    private Selector selector;

    private LinkedBlockingQueue<Input> queue = new LinkedBlockingQueue();

    public Acceptor(Selector selector) {
        this.selector = selector;
    }

    public boolean accept(Input input){
        System.out.println("accept input:"+input);
        return queue.offer(input);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            Input input = null;
            try {
                input = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (input != null){
                Event event = new Event(input);
                selector.addEvent(event);
            }
        }
    }
}
