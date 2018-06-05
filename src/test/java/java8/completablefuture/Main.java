package java8.completablefuture;

import paralexecutor.completablefuture.CompletableFutureTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Created by xubai on 2018/06/06 上午12:20.
 */
public class Main {

    public static void main(String[] args) {
        List<Shop> shops = Arrays.asList(new Shop("BestPrice"),
                new Shop("book"),
                new Shop("cup"),
                new Shop("compute"),
                new Shop("BuyItAll"));
        long start = System.currentTimeMillis();
        System.out.println(findPrices(shops, "myPhone27s"));
        System.out.println("findPrices ct:"+(System.currentTimeMillis()-start)+"ms");

    }

    private static List<String> findPrices(List<Shop> shops, String product){
        return shops.parallelStream()
                .map(shop -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    private static List<String> findPrices2(List<Shop> shops, String product){
        List<CompletableFuture<String>> futures =
                shops.parallelStream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> String.format("%s price is %.2f", shop.getShopName(), shop.getPrice(product)))
                ).collect(Collectors.toList());
        return futures.parallelStream()
                .map(CompletableFuture::join)   //等待所有的异步完成
                .collect(Collectors.toList());
    }



}