/*    This file is part of ProperWeather.

    ProperWeather is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    ProperWeather is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with ProperWeather.  If not, see <http://www.gnu.org/licenses/>.*/
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
