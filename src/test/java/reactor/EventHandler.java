package reactor;

/**
 * Created by xubai on 2018/10/10 下午5:18.
 */
public class EventHandler {

    public Output handler(Event event){
        System.out.println("handle event:"+event);
        return new Output();
    }

}
