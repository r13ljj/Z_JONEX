package paralexecutor.completablefuture;

import scala.Int;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 *
 *  File: CompletableFutureTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/5				lijunjun				Initial.
 *
 * </pre>
 */
public class CompletableFutureTest {

    private final static ExecutorService executors = Executors.newFixedThreadPool(8);
    private final static Random RANDOM = new Random();

    public static void main(String[] args) throws Exception{
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i = 1/0;
            return 100;
        });
        //int result = future.get();
        int result = future.join();
    }

    /**
     * 创建CompletableFuture
     * 未指定executor，使用ForkJoinPool.commonPool()
     * runAsync(Supplier<U> supplier) 创建无返回值的CompletableFuture
     * runAsync(Supplier<U> supplier, Executor executor)
     * supplyAsync(Supplier<U> supplier)
     * supplyAsync(Supplier<U> supplier, Executor executor)
     *
     * @return
     */
    public CompletableFuture<Integer> create(){
        CompletableFuture<Void> noResultFuture = CompletableFuture.runAsync(() -> {
            //do something
        });
        noResultFuture = CompletableFuture.runAsync(() -> {
            //do something
        }, executors);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return 88;
        });
        future = CompletableFuture.supplyAsync(() -> {
            return 99;
        }, executors);
        return future;
    }

    /**
     * 完成时处理
     * whenComplete(BiConsumer<? super T, ? super Throwable> action)
     * whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action)
     * whenCompleteAsync(BiConsumer<? super T, ? super Throwable> action, executor)
     * exceptionally(Function<Throwable, ? extends T> fn) 返回一个新的CompletableFuture，用来处理原来CompletableFuture发生异常时
     *
     * @param future
     */
    public void complete(CompletableFuture<Integer> future)throws Exception{
        CompletableFuture<Integer> completableFuture = future.whenComplete((v, e) -> {
            System.out.println("receive value:"+v);
            System.out.println("catch exception:"+e);
        });
        Integer result = completableFuture.get();
    }

    /**
     * 转换/串联执行
     * thenApply(Function<? super T,? extends U> fn)
     * thenApplyAsync(Function<? super T,? extends U> fn)
     * thenApplyAsync(Function<? super T,? extends U> fn, Executor executor)
     *
     * thenCompose：针对返回值为CompletableFuture的函数；
     * thenApply：针对返回值为其他类型的函数；
     * thenAccept：针对返回值为void的函数
     *
     * thenCombine 方法只是将两个CF联合起来的方法之一，其他的方法包括：
     * thenAcceptBoth：与thenCombine类似，但是它接受一个返回值为void的函数；
     * runAfterBoth：接受一个 Runnable，在两个CF都完成后执行；
     * applyToEither：接受一个一元函数（unary function），会将首先完成的CF的结果提供给它；
     * acceptEither：与applyToEither类似，接受一个一元函数，但是结果为void；
     * runAfterEither：接受一个Runnable，在其中一个CF完成后就执行。
     *
     *
     * @throws Exception
     */
    public void then(CompletableFuture<Integer> future)throws Exception{
        CompletableFuture<String> thenFuture = future.thenAcceptAsync(i -> {
            i = i * 10;
        }).thenApply(i -> i.toString());
        String result = thenFuture.get();
    }

    /**
     * 纯消费(执行Action)
     * thenAccept(Consumer<? super T> action)
     * thenAcceptAsync(Consumer<? super T> action)
     * thenAcceptAsync(Consumer<? super T> action, Executor executor)
     * 组合另一个异步结果
     * thenAcceptBoth(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action)
     * thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action)
     * thenAcceptBothAsync(CompletionStage<? extends U> other, BiConsumer<? super T,? super U> action, Executor executor)
     * runAfterBoth(CompletionStage<?> other,  Runnable action)
     * 完成时执行另一个Runnable
     * thenRun(Runnable action)
     * thenRunAsync(Runnable action)
     * thenRunAsync(Runnable action, Executor executor)
     *
     * @return
     */
    public void action(CompletableFuture<Integer> future)throws Exception{
        CompletableFuture<Void> f = future.thenAcceptAsync(this::printData);
        future.thenAcceptBoth(CompletableFuture.completedFuture(10), (x, y) -> System.out.println(x * y));
        System.out.println(f.get());
    }

    /**
     * 组合/内嵌：返回结果将是一个新的CompletableFuture，这个新的CompletableFuture会组合原来的CompletableFuture和函数返回的CompletableFuture
     * thenCompose(Function<? super T, ? extends CompletionStage<U>> fn)
     * thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn)
     * thenComposeAsync(Function<? super T, ? extends CompletionStage<U>> fn, Executor executor)
     */
    public void compose(CompletableFuture<Integer> future)throws Exception{
        CompletableFuture<String> f = future.thenCompose(i -> {
            return CompletableFuture.supplyAsync(() -> {
                return i * 10 + "";
            });
        });
    }

    /**
     * 并行
     * thenCombine(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
     * thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn)
     * thenCombineAsync(CompletionStage<? extends U> other, BiFunction<? super T,? super U,? extends V> fn, Executor executor)
     *
     * @param future
     */
    public void parallel(CompletableFuture<Integer> future)throws Exception{
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            return "abc";
        });
        CompletableFuture<String> f = future.thenCombine(future2, (x, y) -> x + "-" + y);
        f.thenAcceptAsync(this::printData);
    }

    /**
     * 任意一个CompletableFuture计算完成的时候就会执行
     * acceptEither(CompletionStage<? extends T> other, Consumer<? super T> action)
     * acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action)
     * acceptEitherAsync(CompletionStage<? extends T> other, Consumer<? super T> action, Executor executor)
     *
     * @param future
     */
    public void either(CompletableFuture<Integer> future)throws Exception{
        future.acceptEither(CompletableFuture.completedFuture(99), x -> {
            System.out.println("either x:"+x);
        });
    }

    /**
     * allOf(CompletableFuture<?>... cfs) 所有都执行
     * anyOf(CompletableFuture<?>... cfs) 只要有一个完成都执行
     *
     * @param futures
     */
    public void parallelAll(CompletableFuture<Integer>... futures)throws Exception{
        CompletableFuture.allOf(futures);
        CompletableFuture.anyOf(futures);
    }


    public static <T> CompletableFuture<List<T>> sequence(List<CompletableFuture<T>> futures) {
        CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.<T>toList()));
    }
    public static <T> CompletableFuture<List<T>> sequence(Stream<CompletableFuture<T>> futures) {
        List<CompletableFuture<T>> futureList = futures.filter(f -> f != null).collect(Collectors.toList());
        return sequence(futureList);
    }

    public static <T> CompletableFuture<T> toCompletable(Future<T> future, Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return future.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }, executor);
    }






    private Integer getData(){
        return RANDOM.nextInt(100);
    }

    private void printData(Integer data){
        System.out.println("print data:"+data);
    }
    private void printData(String data){
        System.out.println("print data:"+data);
    }

}
