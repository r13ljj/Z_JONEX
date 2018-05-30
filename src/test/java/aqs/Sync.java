package aqs;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Node:
 * int waitStatus	表示节点的状态。其中包含的状态有：
 * CANCELLED，值为1，表示当前的线程被取消；
 * SIGNAL，值为-1，表示当前节点的后继节点包含的线程需要运行，也就是unpark；
 * CONDITION，值为-2，表示当前节点在等待condition，也就是在condition队列中；
 * PROPAGATE，值为-3，表示当前场景下后续的acquireShared能够得以执行；
 * 值为0，表示当前节点在sync队列中，等待着获取锁。
 * Node prev	前驱节点，比如当前节点被取消，那就需要前驱节点和后继节点来完成连接。
 * Node next	后继节点。
 * Node nextWaiter	存储condition队列中的后继节点。
 * Thread thread	入队列时的当前线程
 *
 * getState()、setState()和compareAndSetState()方法来操纵状态的变迁
 *
 * 独占锁/排他锁获取：
 * while(获取锁) {
 *     if (获取到) {
 *         退出while循环
 *     } else {
 *         if(当前线程没有入队列) {
 *             那么入队列
 *         }
 *         阻塞当前线程
 *     }
 * }
 * 释放一个排他锁：
 * if (释放成功) {
 *     删除头结点
 *     激活原头结点的后继节点
 * }
 *
 * acquireInterruptibly
 * 可中断获取锁：无法获取状态的情况下会进入sync队列进行排队，这类似acquire，但是和acquire不同的地方在于它能够在外界对当前线程进行中断的时候提前结束获取状态的操作
 *
 * public final void acquire(int arg) {
 *         if (!tryAcquire(arg) &&
 *             acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
 *             selfInterrupt();
 *     }
 * 尝试获取（调用tryAcquire更改状态，需要保证原子性）；
 * 在tryAcquire方法中使用了同步器提供的对state操作的方法，利用compareAndSet保证只有一个线程能够对状态进行成功修改，而没有成功修改的线程将进入sync队列排队。
 * acquireQueued
 * 如果获取不到，将当前线程构造成节点Node并加入sync队列；
 * 进入队列的每个线程都是一个节点Node，从而形成了一个双向队列，类似CLH队列，这样做的目的是线程间的通信会被限制在较小规模（也就是两个节点左右）。
 * 再次尝试获取，如果没有获取到那么将当前线程从线程调度器上摘下，进入等待状态。
 * //当前节点入sync队列
 * private Node addWaiter(Node mode) {
 *         //当前线程作为节点
 *         //对于一个节点需要做的是将当节点前驱节点指向尾节点（current.prev = tail），尾节点指向它（tail = current），原有的尾节点的后继节点指向它（t.next = current）而这些操作要求是原子的。
 *         //上面的操作是利用尾节点的设置来保证的，也就是compareAndSetTail来完成的。
 *         Node node = new Node(Thread.currentThread(), mode);
 *         //快速尝试在尾部添加
 *         Node pred = tail;
 *         if (pred != null) {
 *             node.prev = pred;
 *             if (compareAndSetTail(pred, node)) {
 *                 pred.next = node;
 *                 return node;
 *             }
 *         }
 *         //尾节点为空的情况=sync队列没有初始化
 *         enq(node);
 *         return node;
 *     }
 *
 * //初始化sync队列
 * private Node enq(final Node node) {
 * 	for (;;) {
 * 		Node t = tail;
 * 	    //尾节点为空
 * 		if (t == null) { // Must initialize
 * 	        //设置头节点
 * 			if (compareAndSetHead(new Node()))
 * 		        //尾节点跟头节点一样
 * 				tail = head;
 *      //尾节点不为空
 *      } else {
 *            //当前节点的prev指向尾节点
 * 			  node.prev = t;
 * 			  //设置当前节点为尾节点
 * 			  if (compareAndSetTail(t, node)) {
 * 			    //同时原来尾巴的next设置为当前节点
 * 			    t.next = node;
 * 			    return t;
 *          }
 *      }
 * }
 *
 * final boolean acquireQueued(final Node node, int arg) {
 *         boolean failed = true;
 *         try {
 *             boolean interrupted = false;
 *             for (;;) {
 *                 //获取当前节点的prev节点
 *                 final Node p = node.predecessor();
 *                 //prev是头节点且能获取锁，则代表当前节点拥有锁
 *                 if (p == head && tryAcquire(arg)) {
 *                     //一旦成功的修改了状态，当前线程或者说节点，就被设置为头节点
 *                     setHead(node);
 *                     p.next = null; // help GC
 *                     failed = false;
 *                     return interrupted;
 *                 }
 *                 //否则等待，中断检测
 *                 if (shouldParkAfterFailedAcquire(p, node) &&
 *                     parkAndCheckInterrupt())
 *                     interrupted = true;
 *             }
 *         } finally {
 *             if (failed)
 *                 cancelAcquire(node);
 *         }
 *     }
 * private void unparkSuccessor(Node node) {
 * 	// 将状态设置为同步状态
 * 	int ws = node.waitStatus;
 * 	if (ws < 0)
 * 		compareAndSetWaitStatus(node, ws, 0);
 *
 *
 * 	//获取当前节点的后继节点，如果满足状态，那么进行唤醒操作
 * 	//如果没有满足状态，从尾部开始找寻符合要求的节点并将其唤醒
 * 	Node s= node.next;
 * if (s == null || s.waitStatus > 0) {
 * 		s = null;
 * 		for (Node t = tail; t != null && t != node; t = t.prev)
 * 			if (t.waitStatus <= 0)
 * 				s = t;
 * 		}
 * 	if (s != null)
 * 		LockSupport.unpark(s.thread);
 * }
 *
 */
public class Sync extends AbstractQueuedSynchronizer {

    protected Sync() {
        super();
    }

    //exclusive mode排它模式获取这个状态。这个方法的实现需要查询当前状态是否允许获取，然后再进行获取（使用compareAndSetState来做）状态
    @Override
    protected boolean tryAcquire(int acquire) {
        assert acquire == 1;
        if (compareAndSetState(0, acquire)){
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    //exclusive mode排他模式释放状态
    @Override
    protected boolean tryRelease(int arg) {
        return super.tryRelease(arg);
    }

    //sharded mode共享模式下获取状态

    @Override
    protected int tryAcquireShared(int arg) {
        return super.tryAcquireShared(arg);
    }

    //sharded mode共享模式下释放状态。
    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }
}
