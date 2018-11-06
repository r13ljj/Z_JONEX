package metrics;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by xubai on 2018/10/22 下午5:06.
 */
public class MyQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {

    private final BlockingQueue<E> queue;
    private final int capacity;

    private final AtomicInteger size = new AtomicInteger(0);


    public MyQueue(BlockingQueue queue, int capacity) {
        this.queue = queue;
        this.capacity = capacity;
    }


    @Override
    public boolean add(E e) {
        boolean added = this.queue.add(e);
        if (added){
            size.incrementAndGet();
        }
        return added;
    }

    @Override
    public Iterator<E> iterator() {
        Iterator<E> it= queue.iterator();
        return new Iterator<E>(){
            E current;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public E next() {
                current = it.next();
                return current;
            }

            @Override
            public void remove() {
                if (queue.remove(current)){
                    size.decrementAndGet();
                }
            }
        };
    }

    @Override
    public int size() {
        return size.get();
    }

    @Override
    public void put(E e) throws InterruptedException {
        throw new IllegalStateException("put not allowed on threadpool queue");
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        throw new IllegalStateException("offer with timeout not allowed on threadpool queue");
    }

    @Override
    public E take() throws InterruptedException {
        E e = queue.take();
        size.decrementAndGet();
        return e;
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        E e = queue.poll(timeout, unit);
        if (e != null){
            size.decrementAndGet();
        }
        return e;
    }

    @Override
    public int remainingCapacity() {
        return this.capacity - this.size.get();
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        int transfered = queue.drainTo(c);
        size.addAndGet(-transfered);
        return transfered;
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        int transfered = queue.drainTo(c, maxElements);
        size.addAndGet(-transfered);
        return transfered;
    }

    @Override
    public boolean offer(E e) {
        int remain = capacity - size.get();
        if (remain <=0){
            return false;
        }
        boolean offered = queue.offer(e);
        if (offered){
            size.incrementAndGet();
        }
        return offered;
    }

    @Override
    public E poll() {
        E e = queue.poll();
        if (e != null){
            size.decrementAndGet();
        }
        return e;
    }

    @Override
    public E peek() {
        throw new IllegalStateException("peek not allowed on threadpool queue");
    }
}
