package forkjoin.nonreponse;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * RecursiveAction: 如果你的任务没有返回结果
 * RecursiveTask: 如果你的任务返回结果
 */
public class Main {

    public static void main(String[] args) {
        ProductListGenerator generator=new ProductListGenerator();
        List<Product> products=generator.generate(10000000);
        long start = System.currentTimeMillis();
        ProducePriceTask task=new ProducePriceTask(products, 0, products.size(), 0.20);
        ForkJoinPool pool=new ForkJoinPool();
        pool.execute(task);
        do {
            System.out.printf("Main: Thread Count: %d\n",pool.getActiveThreadCount());
            System.out.printf("Main: Thread Steal: %d\n",pool.getStealCount());
            System.out.printf("Main: Parallelism: %d\n",pool.getParallelism());
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!task.isDone());
        System.out.println("Main: execute ct:"+(System.currentTimeMillis()-start)+"ms");
        pool.shutdown();
        //检查假设任务完成时没有出错
        if (task.isCompletedNormally()){
            System.out.printf("Main: The process has completednormally.\n");
        }
        //在增长之后，所有产品的价格应该是12。将价格不是12的所有产品的名称和价格写入到控制台，用来检查它们错误地增长它们的价格
        for (int i=0; i<products.size(); i++){
            Product product=products.get(i);
            if (product.getPrice()!=12) {
                //System.out.printf("Product %s: %f\n",product.getName(),product.getPrice());
            }
        }
        System.out.printf("Main: End of the program.\n");
    }

}
