package pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by xubai on 2018/11/02 6:15 PM.
 */
public class ConnectionTest {

    private final static Random RANDOM = new Random();

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 63003;
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(100);
        config.setMaxIdle(10);
        config.setMinIdle(3);
        config.setMaxWaitMillis(3000);
        config.setTestOnBorrow(true);
        ConnectionPoolFactory factory = new ConnectionPoolFactory(host, port, config);
        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                ConnectionPoolFactory.Status status = factory.getPoolStatus();
                System.out.println(status);
            }
        }, 1, 3, TimeUnit.SECONDS);
        Socket connection = null;
        try {
            connection = factory.getConnection();
            System.out.println("get conection success:"+connection);
            Thread.sleep(10000);
            for (int i = 0; i < 100; i++) {
                Thread t = new Thread(){
                    @Override
                    public void run() {
                        Socket conn = null;
                        try {
                            conn = factory.getConnection();
                            Thread.sleep(RANDOM.nextInt(15000));
                        } catch (Exception e) {
                            e.printStackTrace();e.printStackTrace();
                        }finally {
                            try {
                                factory.releaseConnection(conn);
                            } catch (Exception e) {
                                //NOOP
                            }
                        }
                    }
                };
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null){
                try {
                    //connection.close();
                    factory.releaseConnection(connection);
                } catch (Exception e) {
                    //NOOP
                }
            }
        }
    }

}
