package forkjoin.nonreponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>
 *
 *  File: ThreadPoolMain.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/30				lijunjun				Initial.
 *
 * </pre>
 */
public class ThreadPoolMain {

    private static final ExecutorService thread_pool = Executors.newFixedThreadPool(8);

    public static void main(String[] args)throws Exception {
        ProductListGenerator generator=new ProductListGenerator();
        int count = 10000000;
        List<Product> products=generator.generate(count);
        int pageSize = 10000;
        int index = 0;
        List<Future> futures = new ArrayList<>();
        long start = System.currentTimeMillis();
        do{
            List<Product> subList = products.subList(index, (index+pageSize) > count ? count : index+pageSize);
            final ProducePriceTask task=new ProducePriceTask(subList, 0, (index+pageSize) > count ? count-index : pageSize, 0.20);
            futures.add(thread_pool.submit(new Runnable() {
                @Override
                public void run() {
                    task.updatePrices();
                }
            }));
            index += pageSize;
        }while(index < count);
        for (Future f : futures){
            f.get();
        }
        System.out.println("Main: execute ct:"+(System.currentTimeMillis()-start)+"ms");
        System.exit(-1);
    }

}
