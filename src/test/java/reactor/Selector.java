package reactor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

/**
 * Created by xubai on 2018/10/10 下午3:57.
 */
public class Selector {

    private LinkedTransferQueue<Event> queue = new LinkedTransferQueue<>();
    private Object lock = new Object();


    public List<Event> select(int timeout){
        if (timeout > 0){
            if (queue.isEmpty()){
                synchronized (lock){
                    if (queue.isEmpty()){
                        try {
                            lock.wait(timeout);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        List<Event> readyEvents = new ArrayList<>();
        queue.drainTo(readyEvents);
        return readyEvents;
    }

    public boolean addEvent(Event event){
        System.out.println("add event");
        return queue.offer(event);
    }


}
