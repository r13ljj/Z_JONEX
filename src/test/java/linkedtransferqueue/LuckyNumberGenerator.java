package linkedtransferqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 *
 */
public class LuckyNumberGenerator {

    public static void main(String[] args) {
        /*TransferQueue<String> queue = new LinkedTransferQueue<String>();
        Thread producer = new Thread(new Producer(queue));
        producer.setDaemon(true); //设置为守护进程使得线程执行结束后程序自动结束运行
        producer.start();
        for (int i = 0; i < 10; i++) {
            Thread consumer = new Thread(new Consumer(queue));
            consumer.setDaemon(true);
            consumer.start();
            try {
                // 消费者进程休眠一秒钟，以便生产者获得CPU生产产品
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        LuckyNumberGenerator generator = new LuckyNumberGenerator();
        generator.testTransferQueue();
        //generator.testBlockingQueue();
    }

    public void testTransferQueue(){
        try {
            TransferQueue<String> queue = new LinkedTransferQueue<String>();
            int count = 1000000;
            for(int i=0; i<count; i++){
                //queue.transfer("transfer queue"+i);
                queue.put("transfer queue"+i);
            }
            long start = System.currentTimeMillis();
            Thread consumer = new Thread(new Consumer(queue));
            //consumer.setDaemon(true);
            consumer.start();
            System.out.println("transfer queue count:"+count+" ct:"+(System.currentTimeMillis()-start)+"ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testBlockingQueue(){
        /*try {
            BlockingQueue<String> queue = new LinkedBlockingQueue<>();
            int count = 1000000;
            for(int i=0; i<count; i++){
                queue.add("linkedblocking queue"+i);
            }
            long start = System.currentTimeMillis();
            Thread consumer = new Thread(new Consumer(queue));
            //consumer.setDaemon(true);
            consumer.start();
            System.out.println("linkedblocking queue count:"+count+" ct:"+(System.currentTimeMillis()-start)+"ms");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
