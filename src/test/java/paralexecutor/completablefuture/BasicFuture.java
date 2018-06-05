package paralexecutor.completablefuture;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>
 *
 *  File: BasicFuture.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/6/5				lijunjun				Initial.
 *
 * </pre>
 */
public class BasicFuture {

    public static void main(String[] args)throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        Future<Integer> f = executor.submit(() -> {
            //calculate
            return 100;
        });
        int result = f.get();
    }

}
