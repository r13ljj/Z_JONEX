package jvm;

/**
 * <pre>
 *
 *  File: App.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/4/10				lijunjun				Initial.
 *
 * </pre>
 */
public class App {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：-verbose:gc -Xms20M -Xmx20M -Xmn10M -XX:SurvivorRatio=8
     * -XX:PretenureSizeThreshold=3145728
     *
     -Xms20M
     -Xmx20M
     -Xmn10M
     -XX:PretenureSizeThreshold=3145728
     -XX:SurvivorRatio=8
     -verbose:gc
     -XX:+PrintGCDetails
     -XX:+PrintGCDateStamps
     -Xloggc:gcc.log
     *
     */
    public static void main(String[] args) {
        //byte[] allocation = new byte[9*_1MB];
        //byte[] allocation = new byte[11*_1MB];
        //byte[][] allocation = new byte[11][_1MB]; // 直接分配在老年代中
        byte[] allocation = new byte[4*_1MB];
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
