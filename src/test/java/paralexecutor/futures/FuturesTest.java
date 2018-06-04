package paralexecutor.futures;

import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * CheckedFuture：方便获取任务执行的异常
 * FutureFallback：用于异常恢复的备份。
 * SettableFuture可以用来设置要返回得值
 * FutureCallback定义了onSuccess和onFailure方法，onSuccess方法会接收一个Future对象，这样我们就可以获取Future的结果
 *
 */
public class FuturesTest {

    public static void main(String[] args) {
        SearchService searchService = new SearchService() {
            @Override
            public ListenableFuture<QueryResult> query(String params) {
                return null;
            }
        };
        ListenableFuture<QueryResult> queryFuture1 = searchService.query("q=phone");
        ListenableFuture<QueryResult> queryFuture2 = Futures.transform(queryFuture1, new AsyncFunction<QueryResult, QueryResult>() {
            @Override
            public ListenableFuture<QueryResult> apply(QueryResult queryResult1) throws Exception {
                return null;
            }
        });
        Futures.addCallback(queryFuture2, new FutureCallback<QueryResult>() {
            @Override
            public void onSuccess(QueryResult queryResult) {
                //最终结果
            }

            @Override
            public void onFailure(Throwable throwable) {
                //失败处理
            }
        });
    }

    public void should_test_furture() throws Exception {
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));

        ListenableFuture future1 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 1.");
                return 1;
            }
        });

        ListenableFuture future2 = service.submit(new Callable<Integer>() {
            public Integer call() throws InterruptedException {
                Thread.sleep(1000);
                System.out.println("call future 2.");
                //       throw new RuntimeException("----call future 2.");
                return 2;
            }
        });
        //对多个ListenableFuture的合并，返回一个当所有Future成功时返回多个Future返回值组成的List对象。
        // 注：当其中一个Future失败或者取消的时候，将会进入失败或者取消
        //successfulAsList：和allAsList相似，唯一差别是对于失败或取消的Future返回值用null代替。不会进入失败或者取消流程。
        final ListenableFuture allFutures = Futures.allAsList(future1, future2);

        //对于ListenableFuture的返回值进行转换
        final ListenableFuture transform = Futures.transform(allFutures, new AsyncFunction<List<Integer>, Boolean>() {
            @Override
            public ListenableFuture apply(List<Integer> results) throws Exception {
                //立即返回一个待返回值的ListenableFuture。
                return Futures.immediateFuture(String.format("success future:%d", results.size()));
            }
        });

        Futures.addCallback(transform, new FutureCallback<Object>() {

            public void onSuccess(Object result) {
                System.out.println(result.getClass());
                System.out.printf("success with: %s%n", result);
            }

            public void onFailure(Throwable thrown) {
                System.out.printf("onFailure%s%n", thrown.getMessage());
            }
        });

        System.out.println(transform.get());
    }

}
