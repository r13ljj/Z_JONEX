package metrics;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import java.util.concurrent.TimeUnit;

/**
 * Created by xubai on 2018/10/22 下午4:45.
 */
public class MeterTest {

    public final static MetricRegistry registry = new MetricRegistry();

    public static void main(String[] args) {
        startConsole();
        handleRequest();
        wait5Seconds();
    }


    static void handleRequest(){
        Meter requests = registry.meter("requests");
        requests.mark();

    }

    static void startConsole(){
        ConsoleReporter consoleReporter = ConsoleReporter.forRegistry(registry)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        consoleReporter.start(1, TimeUnit.SECONDS);
    }

    static void wait5Seconds() {
        try {
            Thread.sleep(5*1000);
        }
        catch(InterruptedException e) {}
    }



}
