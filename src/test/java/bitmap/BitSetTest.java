package bitmap;

import java.util.BitSet;

/**
 * bitmap java implemention test
 */
public class BitSetTest {

    public static void main(String[] args) {
        int[] array = {1, 2, 3, 0, 22, 35, 90};
        BitSet bitmap = new BitSet(array.length);
        for (int i=0; i<array.length; i++){
            bitmap.set(array[i]);
        }
        System.out.println(bitmap.get(35));
        System.out.println(bitmap.get(34));
    }

}
