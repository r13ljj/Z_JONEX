package bit;

/**
 * <pre>
 *
 *  File: MathBitOperation.java
 *
 *  Copyright (c) 2018, globalegrow.com All Rights Reserved.
 *
 *  Description:
 *  other:
 *  1.byte、short、char在做移位运算之前，会被自动转换为int类型，然后再进行运算。
 *  2.byte、short、int、char类型的数据经过移位运算后结果都为int型。
 *  3.long经过移位运算后结果为long型。
 *  4.在左移位（<<）运算时，如果要移位的位数大于被操作数对应数据类型所能表示的最大位数，那么先将要求移位数对该类型所能表示的最大位数求余后，再将被操作数移位所得余数对应的数值，效果不变。比如1<<35=1<<(35%32)=1<<3=8。
 *  5.对于有符号右移位（>>）运算和无符号右移位（>>>）运算，当要移位的位数大于被操作数对应数据类型所能表示的最大位数时，那么先将要求移位数对该类型所能表示的最大位数求余后，再将被操作数移位所得余数对应的数值，效果不变。。比如100>>35=100>>(35%32)=100>>3=12。
 *
 *  Revision History
 *  Date,					Who,					What;
 *  2018/3/26				lijunjun				Initial.
 *
 * </pre>
 */
public class MathBitOperation {

    /**
     * 由a^b可得按位相加后没有进位的和；
     * 由a&b可得可以产生进位的地方；
     * 由(a&b)<<1得到进位后的值。
     * 那么  按位相加后 原位和 + 进位和  就是加法的和了，而  a^b +  (a&b)<<1  相当于把  +  两边再代入上述三步进行加法计算。
     * 直到进位和为0说明没有进位了则此时原位和即所求和。
     */
    public int add(int a, int b){
        int x = a ^ b;
        System.out.println("a ^ b="+x);
        int y = (a & b) << 1;
        System.out.println("(a&b) << 1="+y);
        if(y != 0){
            return add(x, y);
        }else{
            return x;
        }
    }

    /**
     * 由-b=+(-b)，~（b-1）=-b可得a-b=a+（-b）=a+(~(b-1))。把减法转化为加法即可。
     */
    public int subtract(int a, int b){
        int B = ~(b-1);
        return add(a, B);
    }

    /**
     * //左移位“<<”把被操作数每向左移动一位，效果等同于将被操作数乘以2
     * 从乘数的低位到高位，遇到1并且这个1在乘数的右起第i（i从0开始数）位，那么就把被乘数左移i位得到 temp_i 。
     * 直到乘数中的1遍历完后，把根据各位1而得到的被乘数的左移值们 temp_i 相加起来即得乘法结果。
     * 那么根据这个原理，可以得到实现代码：
     * 这里要点为：
     * 用i记录当前遍历的乘数位,当前位为1则被乘数左移i位并加到和中，同时i++处理下一位；
     * 为0则乘数右移，i++，处理下一位......直到乘数==0说明乘数中的1遍历完了。此时把和返回即可
     *
     */
    public int multiply(int a, int b){
        int i=0;
        int res=0;
        while(b!=0){//乘数为0则结束
            //处理乘数当前位
            if((b&1)==1){//最低位为1，奇数
                res+=(a<<i);
                b=b>>1;
                ++i;//i记录当前位是第几位
            }else{
                b=b>>1;
                ++i;
            }
        }
        return res;
    }

    /**
     * //无符号右移位">>>"把被操作数每向右移动一位，效果等同于将被操作数除以2
     * 求a可以由多少个b组成。那么由此我们可得除法的实现：求a能减去多少个b，做减法的次数就是除法的商。
     */
    public int divide(int a, int b){
        int res=-1;
        if(a<b){
            return 0;
        }else{
            res=divide(subtract(a, b), b)+1;
        }
        return res;
    }

    public static void main(String[] args) {
        MathBitOperation test = new MathBitOperation();
        System.out.println(test.add(12, 15));
        System.out.println(test.multiply(23, 3));
        System.out.println(test.divide(123, 5));
    }

}
