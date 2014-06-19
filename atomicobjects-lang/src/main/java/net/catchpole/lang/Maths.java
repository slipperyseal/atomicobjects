package net.catchpole.lang;

//   Copyright 2014 catchpole.net
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

public final class Maths {
    private Maths() {
    }

    /**
     * Returns a bit mask for the specified number of bits.
     * <p/>
     * <p>A bit mask is used with the arithmetic AND operator to
     * filter the bits of a value.
     * <p/>
     * <p>eg. A mask for 8 bits would return 0xff or decimal 255.
     * <p/>
     * <p>Any number of bits 32 or above returns -1 (0xffffffff) which masks all bits.
     *
     * @param numberOfBits
     * @return a bit mask
     */
    public static int bitMask(int numberOfBits) {
        if (numberOfBits >= 32) {
            return -1;
        }
        return (numberOfBits == 0 ? 0 : powerOf(2, numberOfBits) - 1);
    }

    /**
     * Power of method for integer math.
     * <p/>
     * <p> eg. 2 to the power of 8 equals 256.
     *
     * @param value
     * @param powerOf
     * @return the result of the calculation
     */
    public static int powerOf(int value, int powerOf) {
        if (powerOf == 0) {
            return 0;
        }
        int r = value;
        for (int x = 1; x < powerOf; x++) {
            r = r * value;
        }
        return r;
    }

    public static long powerOf(long value, int powerOf) {
        if (powerOf == 0) {
            return 0;
        }
        long r = value;
        for (int x = 1; x < powerOf; x++) {
            r = r * value;
        }
        return r;
    }

    /**
     * Returns the number of bits required to store a number.
     * <p/>
     * eg. 200 requires 8 bits
     *
     * @param value
     * @return number of bits
     */
    public static int bitsRequired(int value) {
        int bits = 0;
        while (value != 0) {
            bits++;
            value >>= 1;
        }
        return bits;
    }

    /**
     * Returns the number of bits required to store a number.
     * <p/>
     * eg. 200 requires 8 bits
     *
     * @param value
     * @return number of bits
     */
    public static int bitsRequired(long value) {
        int bits = 0;
        while (value != 0) {
            bits++;
            value >>= 1;
        }
        return bits;
    }

    public static int fitPowerOf2(int n) {
        if (n <= 0) {
            return 0;
        }
        --n;
        int t = 1;
        for (int y = 0; y < 32; y++) {
            n |= n >> t;
            t += t;
        }
        return n + 1;
    }

    public static boolean isPowerOf2(int n) {
        return n != 0 && (n & (n - 1)) == 0;
    }

    public static int changeBit(boolean set, int value, int mask) {
        return (set ? (value & ~mask) : (value | mask));
    }
}
