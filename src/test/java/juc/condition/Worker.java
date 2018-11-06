package juc.condition;

/**
 * Created by xubai on 2018/11/05 2:43 PM.
 */
public class Worker implements Runnable{

    private String name;

    public Worker(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        Acceptor.accept(name);
        //System.out.printf("woker:%s retrun\r\n", name);
    }
}
