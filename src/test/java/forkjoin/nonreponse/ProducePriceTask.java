package forkjoin.nonreponse;

import java.util.List;
import java.util.concurrent.RecursiveAction;

/**
 * RecursiveAction: 如果你的任务没有返回结果
 * RecursiveTask: 如果你的任务返回结果
 */
public class ProducePriceTask extends RecursiveAction {

    private List<Product> products;

    private int first;
    private int last;

    private double increment;

    public ProducePriceTask (List<Product> products, int first, int last, double increment) {
        this.products=products;
        this.first=first;
        this.last=last;
        this.increment=increment;
    }

    @Override
    protected void compute() {
        if (last-first<10000) {
            updatePrices();
        } else {
            int middle=(last+first)/2;
            System.out.printf("Task: Pending tasks:%s first:%s last:%s\n", getQueuedTaskCount(), first, last);
            ProducePriceTask t1=new ProducePriceTask(products, first,middle+1, increment);
            ProducePriceTask t2=new ProducePriceTask(products, middle+1,last, increment);
            invokeAll(t1, t2);
        }

    }

    public void updatePrices() {
        System.out.println("Task: current id:"+Thread.currentThread().getId());
        for (int i=first; i<last; i++){
            Product product=products.get(i);
            product.setPrice(product.getPrice()*(1+increment));
        }
    }
}
