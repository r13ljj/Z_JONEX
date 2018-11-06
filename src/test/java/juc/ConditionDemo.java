package juc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo {

    // 获取独占锁
    private ReentrantLock lock = new ReentrantLock();
    // 获取条件1
    private Condition cd1 = lock.newCondition();
    // 获取条件2
    private Condition cd2 = lock.newCondition();
    // 获取条件3
    private Condition cd3 = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        ConditionDemo demo = new ConditionDemo();
        ExecutorService es = Executors.newCachedThreadPool();
        Thread1 tr1 = demo.new Thread1();
        Thread2 tr2 = demo.new Thread2();
        Thread3 tr3 = demo.new Thread3();
        // 启动线程任务123.
        es.execute(tr1);
        es.execute(tr2);
        es.execute(tr3);
        Thread.sleep(2000);
        // 依次唤醒各线程任务.
        signalTest(demo);
        es.shutdown();
    }

    // 依次唤醒各线程任务,以观察condition的作用
    public static void signalTest(ConditionDemo demo) throws InterruptedException {
        // 获取独占锁 唤醒cd1的线程
        demo.lock.lock();
        demo.cd1.signal();
        // 释放独占锁 等待thread1执行完毕.
        demo.lock.unlock();
        Thread.sleep(2000);

        // 获取独占锁 唤醒cd2的线程
        demo.lock.lock();
        demo.cd2.signal();
        // 释放独占锁 等待thread2执行完毕.
        demo.lock.unlock();
        Thread.sleep(2000);

        // 获取独占锁 唤醒cd3的线程
        demo.lock.lock();
        demo.cd3.signal();
        // 释放独占锁 等待thread2执行完毕.
        demo.lock.unlock();
        Thread.sleep(2000);
    }

    // 线程任务定义类
    public class Thread1 implements Runnable {

        public Thread1() {

        }

        @Override
        public void run() {
            try {
                // 设置线程名称
                Thread.currentThread().setName(Thread1.class.getSimpleName());
                System.out.printf("%s线程启动\n", Thread.currentThread().getName());
                lock.lock();
                // 在cd1上阻塞，并且释放独占锁lock.
                cd1.await();
                System.out.printf("%s线程被唤醒\n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public class Thread2 implements Runnable {
        public Thread2() {

        }

        @Override
        public void run() {
            try {
                Thread.currentThread().setName(Thread2.class.getSimpleName());
                System.out.printf("%s线程启动\n", Thread.currentThread().getName());
                lock.lock();
                // 在cd2上阻塞，并且释放独占锁lock.
                cd2.await();
                System.out.printf("%s线程被唤醒\n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }

    }

    public class Thread3 implements Runnable {
        public Thread3() {

        }

        @Override
        public void run() {
            try {
                Thread.currentThread().setName(Thread3.class.getSimpleName());
                System.out.printf("%s线程启动\n", Thread.currentThread().getName());
                lock.lock();
                // 在cd3上阻塞，并且释放独占锁lock.
                cd3.await();
                System.out.printf("%s线程被唤醒\n", Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

}
