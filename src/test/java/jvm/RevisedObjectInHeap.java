package jvm;

import sun.misc.Unsafe;

/**
 * <pre>
 *
 *  File: RevisedObjectInHeap.java
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
public class RevisedObjectInHeap {

    private long address = 0;

    private Unsafe unsafe = GetUsafeInstance.getUnsafeInstance();

    // 让对象占用堆内存,触发Full GC
    private byte[] bytes = null;

    public RevisedObjectInHeap()
    {
        address = unsafe.allocateMemory(2 * 1024 * 1024);
        //跟unsafe.allocateMemory没有关系，创建堆内对象为了能被回收触发finalize()
        bytes = new byte[1024 * 1024];
    }

    @Override
    protected void finalize() throws Throwable
    {
        super.finalize();
        System.out.println("finalize." + bytes.length);
        unsafe.freeMemory(address);
    }

    public static void main(String[] args)
    {
        while (true)
        {
            RevisedObjectInHeap heap = new RevisedObjectInHeap();
            System.out.println("memory address=" + heap.address);
        }
    }

    static class GetUsafeInstance{
        static Unsafe getUnsafeInstance(){
            //TODO
            return null;
        }
    }

}
