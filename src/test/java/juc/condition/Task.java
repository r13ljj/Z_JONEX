package juc.condition;

import java.util.Random;
import java.util.concurrent.Callable;

/**
 * Created by xubai on 2018/11/05 2:53 PM.
 */
public class Task implements Callable<String> {

    private final static Random RANDOM = new Random();

    private String param;

    public Task(String param) {
        this.param = param;
    }

    @Override
    public String call() throws Exception {
        long start = System.currentTimeMillis();
        try {
            Thread.sleep(RANDOM.nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("task param:%s ct:%dms\r\n", param, (System.currentTimeMillis()-start));
        return null;
    }

}
