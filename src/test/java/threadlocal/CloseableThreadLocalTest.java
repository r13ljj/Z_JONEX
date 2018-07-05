package threadlocal;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *
 *  File: CloseableThreadLocalTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/7/3				lijunjun				Initial.
 *
 * </pre>
 */
public class CloseableThreadLocalTest {

    private final static Random RANDOM = new Random();

    private static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(100, 100,
            3000, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1000));

    final static CloseableThreadLocal<Map<String,Object>> docValuesLocal = new CloseableThreadLocal<Map<String,Object>>() {
        @Override
        protected Map<String,Object> initialValue() {
            return new HashMap<>();
        }
    };


    public static void main(String[] args) {
        String fieldName = "goodsTitle_";
        int i=0;
        for(i=0; i<100; i++){
            System.out.println("i:"+(++i));
            GetFieldValue getThread = new GetFieldValue(fieldName+i);
            THREAD_POOL.execute(getThread);
            /*if (i % 10 == 0){
                CloseFieldValue closeThread = new CloseFieldValue();
                THREAD_POOL.execute(closeThread);
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }*/
        }
    }

    static class GetFieldValue implements Runnable{



        private String field;

        public GetFieldValue(String field) {
            this.field = field;
        }

        @Override
        public void run() {
            for(int i=0; i< 1000000; i++) {

                Map<String, Object> dvFields = docValuesLocal.get();

                Object previous = dvFields.get(field);
                if (previous != null && previous instanceof Integer) {
                    System.out.println("get field:" + field + " value:" + previous);
                } else {
                    int dv = RANDOM.nextInt(100);
                    System.out.println("field:" + field + " value is null,generate:" + dv);
                    dvFields.put(field, dv);
                }
            }
        }
    }

    static class CloseFieldValue implements Runnable{
        @Override
        public void run() {
            docValuesLocal.close();
        }
    }



}
