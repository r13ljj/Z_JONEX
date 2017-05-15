package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class HelloAkka2 {
    public static void main(String[] argArr) {
        ActorSystem systemObj = ActorSystem.create("helloAkka");
        ActorRef actorRef = systemObj.actorOf(Props.create(Actor_Master.class));
        actorRef.tell("hello Akka!", ActorRef.noSender());
    }
}

class Actor_Master extends UntypedActor {
    @Override
    public void onReceive(Object objMsg) throws Exception {
        // 注意 : 在这里创建另外一个 Actor，也就是 Actor_Slave
        ActorRef slave = this.getContext().actorOf(Props.create(Actor_Slave.class));
        slave.tell("go to work!", this.getSelf());
    }
}

class Actor_Slave extends UntypedActor {
    @Override
    public void onReceive(Object objMsg) throws Exception {
        if (objMsg.equals("go to work!")) {
            // 只有收到 go to work! 这条消息时,
            // 才输出 yes sir!
            System.out.println("yes sir!");
        }
    }
}