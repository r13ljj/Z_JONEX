package reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * <pre>
 *
 *  File: FluxTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/28				lijunjun				Initial.
 *
 * </pre>
 */
public class FluxTest {

    public static void main(String[] args) {
        Flux.just("hello", "reactor").subscribe(System.out::println);
        Flux.fromArray(new Integer[]{1, 2, 30}).subscribe(System.out::println);
        Flux.empty().subscribe(System.out::println);
        Flux.range(1, 10).subscribe(System.out::println);
        Flux.interval(Duration.of(10, ChronoUnit.SECONDS)).subscribe(System.out::println);
        //Flux.intervalMillis(1000).subscribe(System.out::println);
        System.out.println("======================================");
        //通过同步和逐一的方式来产生 Flux 序列
        Flux.generate(sink -> {
            sink.next("hello");
            sink.complete();
        }).subscribe(System.out::println);
        System.out.println("======================================");
        Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (value == 10){
                sink.complete();
            }
            return list;
        }).subscribe(System.out::println);
        System.out.println("======================================");
        //create()方法与 generate()方法的不同之处在于所使用的是 FluxSink 对象。FluxSink 支持同步和异步的消息产生，并且可以在一次调用中产生多个元素
        Flux.create(sink -> {
            for (int i=0; i<10; i++){
                sink.next(i);
            }
            sink.complete();
        }).subscribe(System.out::println);
        System.out.println("======================================");
        //输出的是 5 个包含 20 个元素的数组
        Flux.range(1, 100).buffer(20).subscribe(System.out::println);
        //输出的是 2 个包含了 10 个元素的数组
        //Flux.intervalMillis(100).bufferMillis(1001).take(2).toStream().forEach(System.out::println);
        Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).buffer(Duration.of(1001, ChronoUnit.MILLIS)).take(2).toStream().forEach(System.out::println);
        //输出的是 5 个包含 2 个元素的数组,每当遇到一个偶数就会结束当前的收集
        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);
        //输出的是 5 个包含 2 个元素的数组，数组里面包含的只有偶数
        Flux.range(1, 10).bufferWhile(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("======================================");
        //filter过滤
        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);
        System.out.println("======================================");
        //window 操作符是把当前流中的元素收集到另外的 Flux 序列
        Flux.range(1, 100).window(20).subscribe(System.out::println);
        Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).window(Duration.of(1001, ChronoUnit.MILLIS)).take(2).toStream().forEach(System.out::println);
        System.out.println("======================================");
        //zipWith 操作符把当前流中的元素与另外一个流中的元素按照一对一的方式进行合并
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);
        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2))
                .subscribe(System.out::println);
        System.out.println("======================================");
        //take 系列操作符用来从当前流中提取元素
        //take(long n)，take(Duration timespan)和 takeMillis(long timespan)：按照指定的数量或时间间隔来提取。
        //takeLast(long n)：提取流中的最后 N 个元素。
        //takeUntil(Predicate<? super T> predicate)：提取元素直到 Predicate 返回 true。
        //takeWhile(Predicate<? super T> continuePredicate)： 当 Predicate 返回 true 时才进行提取。
        //takeUntilOther(Publisher<?> other)：提取元素直到另外一个流开始产生元素
        Flux.range(1, 1000).take(10).subscribe(System.out::println);
        Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
        System.out.println("======================================");
        //reduce 和 reduceWith 操作符对流中包含的所有元素进行累积操作，得到一个包含计算结果的 Mono 序列
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);
        System.out.println("======================================");
        //merge 和 mergeSequential 操作符用来把多个流合并成一个 Flux 序列
        Flux.merge(Flux.interval(Duration.between(Instant.ofEpochMilli(0), Instant.ofEpochMilli(100))).take(5), Flux.interval(Duration.between(Instant.ofEpochMilli(50), Instant.ofEpochMilli(100))).take(5))
                .toStream()
                .forEach(System.out::println);
        Flux.mergeSequential(Flux.interval(Duration.between(Instant.ofEpochMilli(50), Instant.ofEpochMilli(100))).take(5), Flux.interval(Duration.between(Instant.ofEpochMilli(50), Instant.ofEpochMilli(100))).take(5))
                .toStream()
                .forEach(System.out::println);
        System.out.println("======================================");
        //flatMap 和 flatMapSequential 操作符把流中的每个元素转换成一个流，再把所有流中的元素进行合并
        /*Flux.just(5, 10)
                .flatMap(x -> Flux.interval(Duration.between(Instant.ofEpochMilli(x * 50), Instant.ofEpochMilli(100))).take(x))
                .toStream()
                .forEach(System.out::println);*/
        //concatMap 操作符的作用也是把流中的每个元素转换成一个流，再把所有流进行合并
        Flux.just(5, 10)
                .concatMap(x -> Flux.interval(Duration.between(Instant.ofEpochMilli(x * 50), Instant.ofEpochMilli(100))).take(x))
                .toStream()
                .forEach(System.out::println);
        //combineLatest 操作符把所有流中的最新产生的元素合并成一个新的元素
        Flux.combineLatest(
                Arrays::toString,
                Flux.interval(Duration.of(100, ChronoUnit.MILLIS)).take(5),
                Flux.interval(Duration.between(Instant.ofEpochMilli(50), Instant.ofEpochMilli(100))).take(5)
        ).toStream().forEach(System.out::println);
        //通过 subscribe()方法处理正常和错误消息
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);
        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .retry(1)
                .subscribe(System.out::println);
        //下面几种不同的调度器实现:
        //当前线程，通过 Schedulers.immediate()方法来创建。
        //单一的可复用的线程，通过 Schedulers.single()方法来创建。
        //使用弹性的线程池，通过 Schedulers.elastic()方法来创建。线程池中的线程是可以复用的。当所需要时，新的线程会被创建。如果一个线程闲置太长时间，则会被销毁。该调度器适用于 I/O 操作相关的流的处理。
        //使用对并行操作优化的线程池，通过 Schedulers.parallel()方法来创建。其中的线程数量取决于 CPU 的核的数量。该调度器适用于计算密集型的流的处理。
        //使用支持任务调度的调度器，通过 Schedulers.timer()方法来创建。
        //从已有的 ExecutorService 对象中创建调度器，通过 Schedulers.fromExecutorService()方法来创建。
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .toStream()
                .forEach(System.out::println);
        
    }

}
