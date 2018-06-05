package java8.completablefuture;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by xubai on 2018/06/05 下午11:54.
 */
public class Shop {

    private static final Random RANDOM = new Random();

    private String shopName;

    public Shop(String name) {
        this.shopName = name;
    }

    public double getPrice(String product){
        return calculatePrice(product);
    }

    public Future<Double> getPriceAsync(String product){
        CompletableFuture<Double> priceFuture = new CompletableFuture<Double>();
        new Thread(() -> {
            try {
                double price = this.calculatePrice(product);
                priceFuture.complete(price);
            } catch (Exception e) {
                //将内部异常抛出
                priceFuture.completeExceptionally(e);
            }
        }).start();
        return priceFuture;
    }


    private double calculatePrice(String product){
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return RANDOM.nextDouble() * product.charAt(0) + product.charAt(1);
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}
