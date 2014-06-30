package sk.tomsik68.pw.test;

import static org.junit.Assert.assertArrayEquals;

import java.util.Random;

import org.junit.Test;

public class Long2IntConversionTest {

    @Test
    public void test() {
        System.out.println("testing conversion...");
        int i1,i2;
        Random rand = new Random();
        System.out.println("Random ints are: "+(i1 = rand.nextInt(32)) + " and "+(i2 = rand.nextInt(32)));
        System.out.println("Long is: "+compress(i1,i2));
        System.out.println("Ints are: "+decompress(compress(i1,i2))[0] + " and "+decompress(compress(i1,i2))[1]);
        assertArrayEquals(new int[]{i1,i2},decompress(compress(i1,i2)));
    }
    private long compress(int i1, int i2) {
        long result = i1;
        result = result << 32;
        result = result | i2;
        return result;
    }

    private int[] decompress(long l) {
        int a1 = (int)(l & Integer.MAX_VALUE);
        int a2 = (int)(l >> 32);
        return new int[] { a2, a1 };

    }
}
