

import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过内存映射的方法访问硬盘上的文件，效率要比read和write系统调用高:
 * read()是系统调用，首先将文件从硬盘拷贝到内核空间的一个缓冲区，再将这些数据拷贝到用户空间，实际上进行了两次数据拷贝；
 * map()也是系统调用，但没有进行数据拷贝，当缺页中断发生时，直接将文件从硬盘拷贝到用户空间，只进行了一次数据拷贝。
 */
public class PingPongMapMain {
    public static void main(String... args) throws IOException {
        boolean odd;
        switch (args.length < 1 ? "usage" : args[0].toLowerCase()) {
            case "odd":
                odd = true;
                break;
            case "even":
                odd = false;
                break;
            default:
                System.err.println("Usage: java PingPongMain [odd|even]");
                return;
        }
        int runs = 10000000;
        long start = 0;
        System.out.println("Waiting for the other odd/even");
        String tmpdir = System.getProperty("java.io.tmpdir");
        System.out.println("tmpdir:"+tmpdir);
        File counters = new File(tmpdir, "counters.deleteme");
        counters.deleteOnExit();

        //MapMode mode：内存映像文件访问的方式，共三种
        //MapMode.READ_ONLY：只读，试图修改得到的缓冲区将导致抛出异常。
        //MapMode.READ_WRITE：读/写，对得到的缓冲区的更改最终将写入文件；但该更改对映射到同一文件的其他程序不一定是可见的。
        //MapMode.PRIVATE：私用，可读可写,但是修改的内容不会写入文件，只是buffer自身的改变，这种能力称之为”copy on write”。
        try (FileChannel fc = new RandomAccessFile(counters, "rw").getChannel()) {
            //FileChannel提供了map方法把文件映射到虚拟内存
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, 1024);
            //返回directbuffer内存地址
            //1.第一次访问address所指向的内存区域，导致缺页中断，中断响应函数会在交换区中查找相对应的页面，如果找不到（也就是该文件从来没有被读入内存的情况），则从硬盘上将文件指定页读取到物理内存中（非jvm堆内存）。
            //2.如果在拷贝数据时，发现物理内存不够用，则会通过虚拟内存机制（swap）将暂时不用的物理页面交换到硬盘的虚拟内存中。
            long address = ((DirectBuffer) mbb).address();
            for (int i = -1; i < runs; i++) {
                for (; ; ) {
                    //读取address地址的值
                    long value = UNSAFE.getLongVolatile(null, address);
                    boolean isOdd = (value & 1) != 0;
                    if (isOdd != odd)
                    	//wait for the other side.
                        continue;
                    //make the change atomic, just in case there is more than one odd/even process
                    if (UNSAFE.compareAndSwapLong(null, address, value, value + 1))
                        break;
                }
                if (i == 0) {
                    System.out.println("Started");
                    start = System.nanoTime();
                }
            }
        }
        System.out.printf("... Finished, average ping/pong took %,d ns%n",
                (System.nanoTime() - start) / runs);
    }

    static final Unsafe UNSAFE;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
}