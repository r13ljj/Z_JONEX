package juc.condition;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by xubai on 2018/11/05 2:44 PM.
 */
public class Acceptor {

    private static ConcurrentHashMap<String, AsyncExecutor> executorMap = new ConcurrentHashMap<>();

    private static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public static void accept(String param){
        long start = System.currentTimeMillis();
        System.out.printf("[%d] acceptor param:%s start\r\n", start, param);
        AsyncExecutor<String> asyncExecutor = null;
        try {
            readWriteLock.readLock().lock();
            asyncExecutor = executorMap.get(param);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
        boolean exist = false;
        if (asyncExecutor == null){
            //asyncExecutor = new AsyncExecutor<String>(lock);
            try {
                asyncExecutor = new AsyncExecutor<String>();
                asyncExecutor.join(new Task(param));
                readWriteLock.writeLock().lock();
                executorMap.putIfAbsent(param, asyncExecutor);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                readWriteLock.writeLock().unlock();
            }
        }else{
            exist = true;
            Lock lock = asyncExecutor.lock;
            try {
                lock.lock();
                Condition condition = lock.newCondition();
                asyncExecutor.addCondition(condition);
                System.out.printf("asyncExecutor:%s new condition\r\n", param);
                condition.await(3000L, TimeUnit.MILLISECONDS);
                System.out.printf("asyncExecutor:%s condition back\r\n", param);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        long end = System.currentTimeMillis();
        System.out.printf("[%d] acceptor:%s asyncExecutor:%s  return ct:%dms\r\n", end, exist, param, (end-start));
        List<String> results = asyncExecutor.get();
        try {
            readWriteLock.writeLock().lock();
            executorMap.remove(param);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

}
