package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HelloAkka1 {
    public static void main(String[] argArr) {
        ActorSystem systemObj = ActorSystem.create("helloAkka");
        ActorRef actorRef = systemObj.actorOf(Props.create(Actor_SayHello.class));
        actorRef.tell("hello Akka!", ActorRef.noSender());
    }
}

class Actor_SayHello extends UntypedActor {
    @Override
    public void onReceive(Object objMsg) throws Exception {
        System.out.println("receive : " + objMsg);
    }
}