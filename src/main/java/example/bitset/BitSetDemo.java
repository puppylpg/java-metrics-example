package example.bitset;

import java.util.BitSet;

/**
 * BitSet底层是long数组，即64bit一个slot。
 *
 * 存储一个数n（只能是int）时，直接将第n个bit设为1。
 * 具体而言：先判断属于哪个slot（除以64，取整），然后将这个slot的第n%64位（取余）设为1。
 *
 * 读取一个数据n时，就是看第n位为1，那这个数就是n。具体使用了{@link BitSet#nextSetBit(int)}，
 * (u * BITS_PER_WORD) + Long.numberOfTrailingZeros(word)，找出是第几个bit。
 *
 * 缺陷：如果存储的数据很大，最大为{@link Integer#MAX_VALUE}，需要{@link Integer#MAX_VALUE}个bit，
 * 也即{@link Integer#MAX_VALUE}/64个long大小的空间。
 *
 * @author liuhaibo on 2018/08/14
 */
public class BitSetDemo {

    public static void main(String args[]) {
        BitSet bits1 = new BitSet(16);
        BitSet bits2 = new BitSet(16);
        bits1.set(5);
        bits1.set(67);
        bits1.set(10086);
        bits1.set(Integer.MAX_VALUE);
        System.out.println(bits1);

        // set some bits
        for(int i=0; i<16; i++) {
            if((i%2) == 0) bits1.set(i);
            if((i%5) != 0) bits2.set(i);
        }
        System.out.println("Initial pattern in bits1: ");
        System.out.println(bits1);
        System.out.println("\nInitial pattern in bits2: ");
        System.out.println(bits2);

        // AND bits
        bits2.and(bits1);
        System.out.println("\nbits2 AND bits1: ");
        System.out.println(bits2);

        // OR bits
        bits2.or(bits1);
        System.out.println("\nbits2 OR bits1: ");
        System.out.println(bits2);

        // XOR bits
        bits2.xor(bits1);
        System.out.println("\nbits2 XOR bits1: ");
        System.out.println(bits2);
    }
}
