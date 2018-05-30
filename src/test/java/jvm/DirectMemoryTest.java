package jvm;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *
 *  File: DirectMemoryTest.java
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
public class DirectMemoryTest {

    /**
     * @VM args:
     *  -XX:MaxDirectMemorySize=40m
     *  -verbose:gc
     *  -XX:+PrintGCDetails
     *  -XX:+DisableExplicitGC //增加此参数一会儿就会内存溢出java.lang.OutOfMemoryError: Direct buffer memory
     */
    public static void testDirectByteBuffer() {
        List<ByteBuffer> list = new ArrayList<ByteBuffer>();
        while(true) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1 * 1024 * 1024);
            //list.add(buffer);
        }
    }

    public static void main(String[] args) {
        DirectMemoryTest.testDirectByteBuffer();
    }

}
