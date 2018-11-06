package threadpool;

/**
 * Created by xubai on 2018/11/06 2:42 PM.
 */
public class Main {

    public static void main(String[] args) {
        ThreadPool pool = new ThreadPool(9);
        for (int i = 0; i < 100; i++) {
            pool.execute(new Task(i));
        }
    }

}
