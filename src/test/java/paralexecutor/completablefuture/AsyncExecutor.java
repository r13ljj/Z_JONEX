package paralexecutor.completablefuture;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by xubai on 2018/10/13 上午10:25.
 */
public class AsyncExecutor {

    private static final ThreadPoolExecutor threadpool =
            new ThreadPoolExecutor(4, 9, 3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    private static Object mutex = new Object();

    public static void main(String[] args) throws Exception{
        List<AbstractTask<String>> tasks = new ArrayList<>();
        tasks.add(new AbstractTask<String>(){
            @Override
            protected String execute() {
                return "hello";
            }
        });
        tasks.add(new AbstractTask<String>(){
            @Override
            protected String execute() {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "world";
            }
        });
        tasks.add(new AbstractTask<String>(){
            @Override
            protected String execute() {
                return "jonex";
            }
        });
        AsyncExecutor asyncExecutor = new AsyncExecutor();
        asyncExecutor.execute(tasks, new Merger());


    }

    public void execute(List<AbstractTask<String>> tasks, Merger merger)throws Exception{
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (AbstractTask<String> task : tasks) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return task.execute();
            }, threadpool);
            futures.add(future);
        }
        CompletableFuture<Void> allof = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        CompletableFuture<List<String>> listFuture = allof.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<String>toList()));
        //List<String> list = listFuture.get();
        //list.forEach(System.out::println);
        listFuture.whenComplete((v, e) -> {
            merger.addResult(v);
            merger.run();
        });
        System.out.println("sumbit task over");
    }

    public CompletableFuture<List<String>> submit(List<AbstractTask<String>> tasks){
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (AbstractTask<String> task : tasks) {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
                return task.execute();
            }, threadpool);
            futures.add(future);
        }
        CompletableFuture<Void> allof = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        CompletableFuture<List<String>> listFuture = allof.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<String>toList()));
        listFuture.whenComplete((v, e) -> {
            //mutex.notifyAll();
        });
        System.out.println("sumbit task over");
        return listFuture;
    }


    static abstract class AbstractTask<V> implements Callable<V> {

         @Override
         public V call() throws Exception {
             return this.execute();
         }

         protected abstract V execute();
    }

    static class Merger implements Runnable{

        private Channel<List<String>> channel = new Channel<List<String>>() {
            @Override
            public void read(List<String> list) {
                System.out.println("read:"+list);
            }

            @Override
            public void write(List<String> list) {
                System.out.println("write:"+list);
            }
        };

        private List<String> results;

        public void addResult(List<String> result){
            this.results = result;
        }

        @Override
        public void run() {
            channel.write(results);
        }
    }

    interface Channel<T>{

        void read(T t);

        void write(T t);
    }

}
