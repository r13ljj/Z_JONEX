package reactor;

import java.util.List;

/**
 * Created by xubai on 2018/10/10 下午5:15.
 */
public class Dispatcher {

    private Selector selector;
    private EventHandler eventHandler;

    public Dispatcher(Selector selector) {
        this.selector = selector;
    }

    public void registerHandler(EventHandler handler){
        this.eventHandler = handler;
    }

    public void handleEvents(){
        System.out.println("start handle events");
        dispath();
    }

    private void dispath(){
        while (true){
            List<Event> events = selector.select(100);
            events.parallelStream().map(event -> eventHandler.handler(event));
        }
    }

}
