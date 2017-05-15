package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

public class AppSlave {
    public static void main(String[] argArr) {
        Map<String, String> propMap = new HashMap<>();
        propMap.put("akka.actor.provider", "akka.remote.RemoteActorRefProvider");
        propMap.put("akka.remote.netty.tcp.hostname", "192.168.1.12");
        propMap.put("akka.remote.netty.tcp.port", "4712");

        ActorSystem systemObj = ActorSystem.create(
            "helloAkka",
            ConfigFactory.parseMap(propMap)
        );

        ActorRef actorRef = systemObj.actorOf(Props.create(Actor_Slave.class), "slave");
        actorRef.tell("hello Akka!", ActorRef.noSender());
    }
}

class ActorSlave extends UntypedActor {
    @Override
    public void onReceive(Object objMsg) throws Exception {
        if (objMsg.equals("go to work!")) {
            System.out.println("yes sir!");
        }else{
        	System.out.println("i don't know what are you say:"+objMsg);
        }
    }
}