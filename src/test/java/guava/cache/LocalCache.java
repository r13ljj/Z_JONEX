package guava.cache;

import com.google.common.cache.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

/**
 * CacheBuilder.weakKeys()：使用弱引用存储键。当键没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用键的缓存用==而不是equals比较键。
 * CacheBuilder.weakValues()：使用弱引用存储值。当值没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用值的缓存用==而不是equals比较值。
 * CacheBuilder.softValues()：使用软引用存储值。软引用只有在响应内存需要时，才按照全局最近最少使用的顺序回收。考虑到使用软引用的性能影响，我们通常建议使用更有性能预测性
 *
 * Cache.invalidate(key)：个别清除
 * Cache.invalidateAll(keys)：批量清除
 * Cache.invalidateAll()：清除所有缓存项
 *
 * CacheBuilder.recordStats()：用来开启Guava Cache的统计功能。统计打开后，Cache.stats()方法会返回CacheStats对象以提供如下统计信息：
 * hitRate()：缓存命中率；
 * averageLoadPenalty()：加载新值的平均时间，单位为纳秒；
 * evictionCount()：缓存项被回收的总数，不包括显式清除。
 *
 * http://ifeve.com/google-guava/
 */
public class LocalCache {

    private static final LongAdder CACHE_HIT = new LongAdder();
    private static final LongAdder CACHE_NO_HIT = new LongAdder();
    private static final LongAdder CACHE_EVICT_HIT = new LongAdder();
    private final static int THREAD_COUNT = Runtime.getRuntime().availableProcessors() * 2 + 1;
    private final static Random RANDOM = new Random();

    private final static CountDownLatch LATCH = new CountDownLatch(THREAD_COUNT);

    static LoadingCache<Key, Graph> graphs = CacheBuilder.newBuilder()
            //.maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES) //缓存项在给定时间内没有被写访问（创建或覆盖），则回收
            .expireAfterWrite(1, TimeUnit.MINUTES)  //缓存项在给定时间内没有被读/写访问，则回收
            .removalListener(new EvictedListener()) //驱逐监听器
            .maximumWeight(1000)    //指定最大总重
            .weigher(new Weigher<Key, Graph>() {
                @Override
                public int weigh(Key key, Graph graph) {
                    return key != null ? Integer.valueOf(key.name) : RANDOM.nextInt(1000);
                }
            })
            .build(
                    new CacheLoader<Key, Graph>() {
                        public Graph load(Key key) throws Exception {
                            CACHE_NO_HIT.increment();
                            return createExpensiveGraph(key);
                        }

                        @Override
                        public Map<Key, Graph> loadAll(Iterable<? extends Key> keys) throws Exception {
                            //TODO
                            return super.loadAll(keys);
                        }
                    });

    static Graph createExpensiveGraph(Key key){
        //TODO
        key.name = RANDOM.nextInt(1000)+"";
        //System.out.println("create graph:"+key.name);
        return new Graph(key.name+"-val");
    }

    static class Key{
        String name;

        public Key(String name) {
            this.name = name;
        }
    }

    static class Graph{
        String value;

        public Graph(String value) {
            this.value = value;
        }
    }

    static class EvictedListener implements RemovalListener<Key, Graph>{

        @Override
        public void onRemoval(RemovalNotification<Key, Graph> removalNotification) {
            CACHE_EVICT_HIT.increment();
            //System.out.println("evict key:"+removalNotification.getKey().name+" graph:"+removalNotification.getValue().value);
        }
    }

    public static void main(String[] args) throws Exception{
        List<Thread> threads = new ArrayList<>();
        for (int i=0; i<THREAD_COUNT; i++){
            threads.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()){
                        try {
                            for (int i=0; i<10000; i++) {
                                //Graph cache = graphs.get(new Key(RANDOM.nextInt(1000) + ""));
                                final Key key = new Key(RANDOM.nextInt(1000) + "");
                                Graph cache = graphs.get(key, new Callable<Graph>() {
                                    @Override
                                    public Graph call() throws Exception {
                                        CACHE_NO_HIT.increment();
                                        return new Graph(key.name+"-val");
                                    }
                                });
                                if (cache != null) {
                                    CACHE_HIT.increment();
                                }
                            }
                            Thread.currentThread().interrupt();
                            LATCH.countDown();
                        } catch (ExecutionException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                }
            }));
        }
        threads.forEach(thread -> {
            thread.start();
        });
        LATCH.await();
        System.out.println("execute count:"+(THREAD_COUNT*10000)+" CACHE_HIT:"+CACHE_HIT+" CACHE_NO_HIT:"+CACHE_NO_HIT+" CACHE_EVICT_HIT:"+CACHE_EVICT_HIT);
    }
}
