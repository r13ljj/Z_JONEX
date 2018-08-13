package bitmap;

/**
 * 问题：
 *      给一台普通PC，2G内存，要求处理一个包含40亿个不重复并且没有排过序的无符号的int整数，给出一个整数，问如果快速地判断这个整数是否在文件40亿个数据当中？
 * 问题思考：
 *      40亿个int占（40亿*4）/1024/1024/1024 大概为14.9G左右，很明显内存只有2G，放不下，因此不可能将这40亿数据放到内存中计算。
 *      要快速的解决这个问题最好的方案就是将数据搁内存了，所以现在的问题就在如何在2G内存空间以内存储着40亿整数。一个int整数在java中是占4个字节的即要32bit位，
 *      如果能够用一个bit位来标识一个int整数那么存储空间将大大减少，算一下40亿个int需要的内存空间为40亿/8/1024/1024大概为476.83 mb，
 *      这样的话我们完全可以将这40亿个int数放到内存中进行处理。
 * 具体思路：
 *      1个int占4字节即4*8=32位，那么我们只需要申请一个int数组长度为 int tmp[1+N/32]即可存储完这些数据，其中N代表要进行查找的总数，
 *      tmp中的每个元素在内存在占32位可以对应表示十进制数0~31,所以可得到BitMap表:
 *      tmp[0]:可表示0~31
 *      tmp[1]:可表示32~63
 *      tmp[2]可表示64~95
 *      ...
 *
 * 如何判断int数字在tmp数组的哪个下标，这个其实可以通过直接除以32取整数部分，
 * 例如：整数8除以32取整等于0，那么8就在tmp[0]上。另外，我们如何知道了8在tmp[0]中的32个位中的哪个位，这种情况直接mod上32就ok，
 * 又如整数8，在tmp[0]中的第8 mod上32等于8，那么整数8就在tmp[0]中的第八个bit位（从右边数起）。
 */
public class Bitmap {

    private final static int BITSPERWORD = 32;
    private final static int SHIFT = 5;
    private final static byte MASK = 0x1F;

    int[] bitmap;

    int count;

    public Bitmap(int count) {
        this.count = count;
        bitmap = new int[1+count/BITSPERWORD];
    }

    //set 设置所在的bit位为1
    void set(int i){
        //i >>> SHIFT 除以32取整数部分: i/32
        //i & MASK 除以32取模mod: i%32
        bitmap[i >>> SHIFT] |= 1 << (i & MASK);
    }

    //clear  清理所在bit位为0
    void clear(int i){
        bitmap[i >>> SHIFT] &= ~(1 << (i & MASK));
    }

    int test(int i){
        return bitmap[i >>> SHIFT] & (1 << (i & MASK));
    }

    public static void main(String[] args) {
        int i = 8;
        System.out.println(i >>> SHIFT);
        System.out.println(MASK);
        System.out.println("0x"+Integer.toHexString(31));
        System.out.println(i & MASK);
        System.out.println(1 << (i & MASK));
        Bitmap bitmap = new Bitmap(Integer.MAX_VALUE);
        bitmap.set(i);
        System.out.println(Integer.toBinaryString(bitmap.test(i)));
        System.out.println("======================");
        int a = 5;
        System.out.println(Integer.toBinaryString(a));
        System.out.println(Integer.toBinaryString(~a));
        a = a^1;
        System.out.println(Integer.toBinaryString(a));
        byte b = 0x05;
        byte BITS = 0x08;
        System.out.println(1 << (b & BITS));
    }

}
