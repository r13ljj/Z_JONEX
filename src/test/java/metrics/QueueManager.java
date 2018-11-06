package metrics;

import com.codahale.metrics.*;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by xubai on 2018/10/22 下午5:40.
 */
public class QueueManager {

    Queue<Event> queue = null;

    Counter pendingJobs = null;

    public QueueManager(String name) {
        this.queue = new MyQueue<>(new LinkedBlockingDeque(), 1000);;
        MeterTest.registry.register(MetricRegistry.name(QueueManager.class, name, "size"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        return queue.size();
                    }
                });
        pendingJobs = MeterTest.registry.counter(MetricRegistry.name(QueueManager.class, "pending-jobs"));
    }

    public void monitor(){
        ConsoleReporter reporter = ConsoleReporter.forRegistry(MeterTest.registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.SECONDS);
    }

    public boolean addEvent(Event e){
        pendingJobs.inc();
        return this.queue.add(e);
    }

    public Event takeEvent(){
        pendingJobs.dec();
        return this.queue.poll();
    }

    class Event{
        String name;
        Object body;
    }

}
