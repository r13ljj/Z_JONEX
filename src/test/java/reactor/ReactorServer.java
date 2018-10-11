package reactor;

/**
 * Created by xubai on 2018/10/10 下午5:22.
 */
public class ReactorServer{

    static Selector selector = new Selector();
    static Dispatcher dispatcher = new Dispatcher(selector);
    static Acceptor acceptor = new Acceptor(selector);

    public static void main(String[] args) {
        dispatcher.registerHandler(new EventHandler());
        Thread thread = new Thread(acceptor);
        thread.start();
        new Thread(){
            @Override
            public void run() {
                dispatcher.handleEvents();
            }
        }.start();
        acceptor.accept(new Input("msg1"));
    }

}
