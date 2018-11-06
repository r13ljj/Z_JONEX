package threadpool;

/**
 * Created by xubai on 2018/11/06 2:29 PM.
 */
public class Task implements Runnable{

    private int no;

    public Task(int no) {
        this.no = no;
    }

    @Override
    public void run() {
        System.out.printf("this is no:%d task\r\n", no);
    }
}
