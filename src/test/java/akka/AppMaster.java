package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class AppMaster {
    public static void main(String[] argArr) {
        Map<String, String> propMap = new HashMap<>();
        // 这个 RemoteActorRefProvider 是要加上的

        propMap.put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");
        propMap.put("akka.remote.netty.tcp.hostname", "192.168.1.11");
        propMap.put("akka.remote.netty.tcp.port", "4711");

        ActorSystem systemObj = ActorSystem.create(
            "helloAkka",
            ConfigFactory.parseMap(propMap)
        );

        ActorRef actorRef = systemObj.actorOf(Props.create(ActorMaster.class), "master");
        actorRef.tell("hello Akka!", ActorRef.noSender());
    }
}

class ActorMaster extends UntypedActor {
    @Override
    public void onReceive(Object objMsg) throws Exception {
        // 注意 : 这里是通过 IP 地址拿到 Actor_Slave 的
        ActorSelection slave = this.getContext().actorSelection("akka.tcp://helloAkka@192.168.1.12:4712/user/slave");
        slave.tell("go to work!", this.getSelf());
    }
}