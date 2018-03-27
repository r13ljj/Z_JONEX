package bit;

/**
 * <pre>
 *
 *  File: BitOperation.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  1.左移位（<<）：将操作符左侧的操作数向左移动操作符右侧指定的位数。移动的规则是在二进制的低位补0。
 *  2.有符号右移位（>>）：将操作符左侧的操作数向右移动操作符右侧指定的位数。移动的规则是，如果被操作数的符号为正，则在二进制的高位补0；如果被操作数的符号为负，则在二进制的高位补1。
 *  3.无符号右移位（>>>）：将操作符左侧的操作数向右移动操作符右侧指定的位数。移动的规则是，无论被操作数的符号是正是负，都在二进制位的高位补0。
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/26				lijunjun				Initial.
 *
 * </pre>
 */
public class BitOperation {

    public static void main(String[] args) {
        /**
         * think in java:
         * 对char，byte或者short进行移位处理，那么在移位进行之前，它们会自动转换成一个int。只有右侧的5个低位才会有用。
         * 这样可防止我们在一个int数里移动不切实际的位数。若对一个long值进行处理，最后得到的结果也是long。
         * 此时只会用到右侧的6个低位，防止移动超过long值里现成的位数。
         */
        int i = 1245;
        String s = Integer.toBinaryString(i);
        int y1 = i>>2;
        String s1 = Integer.toBinaryString(y1);
        int y2 = i>>34;
        String s2 = Integer.toBinaryString(y2);
        System.out.println(i + "->" + s + "  " + y1 + "->" + s1 + "  " + y2 + "->" + s2);
        int y3 = i >>> 34;
        String s3 = Integer.toBinaryString(y3);
        System.out.println(i + "->" + s + "  " + y3 + "->" + s3);

        //================================================================================
        System.out.println(1<<3);
        //1<<35=1<<(35%32)=1<<3=8
        System.out.println(1<<35);

        System.out.println(100>>3);
        //100>>35=100>>(35%32)=100>>3=12
        System.out.println(100>>35);
        System.out.println(100>>>35);

        System.out.println("1 << 3 = " + (1 << 3));
        System.out.println("(byte) 1 << 35 = " + ((byte) 1 << (32 + 3)));
        System.out.println("(short) 1 << 35 = " + ((short) 1 << (32 + 3)));
        System.out.println("(char) 1 << 35 = " + ((char) 1 << (32 + 3)));
        System.out.println("1 << 35 = " + (1 << (32 + 3)));
        System.out.println("1L << 67 = " + (1L << (64 + 3)));
        // 此处需要Java5.0及以上版本支持
        System.out.println("new Integer(1) << 3 = " + (new Integer(1) << 3));
        System.out.println("10000 >> 3 = " + (10000 >> 3));
        System.out.println("10000 >> 35 = " + (10000 >> (32 + 3)));
        System.out.println("10000L >>> 67 = " + (10000L >>> (64 + 3)));
    }

}
