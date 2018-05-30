package jvm;

/**
 * <pre>
 *
 *  File: NonHeapTest.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  http://www.cnblogs.com/duanxz/p/6089485.html
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/5/7				lijunjun				Initial.
 *
 * </pre>
 */
import java.nio.ByteBuffer;
import sun.nio.ch.DirectBuffer;

public class NonHeapTest {
    public static void clean(final ByteBuffer byteBuffer) {
        if (byteBuffer.isDirect()) {
            ((DirectBuffer)byteBuffer).cleaner().clean();
        }
    }

    public static void sleep(long i) {
        try {
            Thread.sleep(i);
        }catch(Exception e) {
            /*skip*/
        }
    }
    public static void main(String []args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024 * 200);
        System.out.println("start");
        sleep(5000);
        clean(buffer);//执行垃圾回收
        //System.gc();//执行Full gc进行垃圾回收
        System.out.println("end");
        sleep(5000);
    }
}
