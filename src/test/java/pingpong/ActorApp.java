package pingpong;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 *
 *  File: ActorApp.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/6				lijunjun				Initial.
 *
 * </pre>
 */
public class ActorApp {

    private static final CountDownLatch LATCH = new CountDownLatch(1);

    private static ActorSystem actorSystem = ActorSystem.create("actorSystem");
    private static ActorRef pingActor = actorSystem.actorOf(Props.create(Ping.class));
    private static ActorRef pongActor = actorSystem.actorOf(Props.create(Pong.class));

    private static long startTime = 0;

    public static void main(String[] args) throws Exception{
        startTime = System.currentTimeMillis();
        pongActor.tell(1000000, ActorRef.noSender());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String cmd = br.readLine();
                    while(true) {
                        if ("shutdown".equals(cmd)) {
                            LATCH.countDown();
                        } else {
                            Thread.sleep(1000);
                        }
                        cmd = br.readLine();
                    }
                }catch(Exception e){

                }
            }
        }).start();
        LATCH.await();
        actorSystem.stop(pingActor);
        actorSystem.stop(pongActor);
        actorSystem.terminate();
        Runtime.getRuntime().exit(0);
    }

    static class Ping extends UntypedActor{
        @Override
        public void onReceive(Object o) throws Throwable {
            if (o instanceof Integer){
                Integer i = (Integer) o;
                if (i <= 0){
                    System.out.println("actor pingpong ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    actorSystem.terminate();
                    return;
                }
                if (i % 2 == 1){
                    System.out.println("ping:"+i);
                    pongActor.tell(--i, pingActor);
                }
            }
        }
    }

    static class Pong extends UntypedActor{
        @Override
        public void onReceive(Object o) throws Throwable {
            if (o instanceof Integer){
                Integer i = (Integer) o;
                if (i <= 0){
                    System.out.println("actor pingpong ct:"+(System.currentTimeMillis()-startTime)+"ms");
                    actorSystem.terminate();
                    return;
                }
                if (i % 2 == 0){
                    System.out.println("pong:"+i);
                    pingActor.tell(--i, pongActor);
                }
            }
        }
    }
}
