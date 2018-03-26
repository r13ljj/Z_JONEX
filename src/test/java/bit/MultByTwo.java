package bit;

/**
 * <pre>
 *
 *  File: MultByTwo.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  TODO
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/26				lijunjun				Initial.
 *
 * </pre>
 */
// Left shifting as a quick way to multiply by 2.
public class MultByTwo {
    public static void main(String args[]) {
        int i;
        int num = 0xFFFFFFE;
        System.out.println(num);
        for(i=0; i<4; i++) {
            num = num << 1;
            System.out.println(num);
        }
    }
}