package metrics;

/**
 * Created by xubai on 2018/10/22 下午5:52.
 */
public class MyQueueTest {

    public static void main(String[] args) throws Exception{
        QueueManager queueManager = new QueueManager("jonex");
        queueManager.monitor();
        queueManager.addEvent(queueManager.new Event());
        Thread.sleep(3000L);
        for (int i = 0; i < 100; i++) {
            queueManager.addEvent(queueManager.new Event());
        }
        Thread.sleep(10000L);
        for (int i = 0; i < 2; i++) {
            queueManager.takeEvent();
        }
        Thread.sleep(3000L);
        for (int i = 0; i < 100; i++) {
            queueManager.takeEvent();
        }
        Thread.sleep(10000L);
    }

}
