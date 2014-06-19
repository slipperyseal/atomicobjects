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

import junit.framework.TestCase;
import org.junit.Test;

public class MathsTest {
    @Test
    public void testPowerOf() {
        TestCase.assertEquals(0, Maths.powerOf(2, 0));
        TestCase.assertEquals(2, Maths.powerOf(2, 1));
        TestCase.assertEquals(65536, Maths.powerOf(2, 16));
        TestCase.assertEquals(1000, Maths.powerOf(10, 3));
        TestCase.assertEquals(279936, Maths.powerOf(6, 7));
    }
    
    @Test
    public void testBitMask() {
        TestCase.assertEquals(0, Maths.bitMask(0));
        TestCase.assertEquals(1, Maths.bitMask(1));
        TestCase.assertEquals(3, Maths.bitMask(2));
        TestCase.assertEquals(0xf, Maths.bitMask(4));
        TestCase.assertEquals(0xff, Maths.bitMask(8));
        TestCase.assertEquals(0xffff, Maths.bitMask(16));
        TestCase.assertEquals(0x7FFFFFFF, Maths.bitMask(31));
        TestCase.assertEquals(-1, Maths.bitMask(32));
    }

    @Test
    public void testBitsRequired() {
        TestCase.assertEquals(0, Maths.bitsRequired(0));
        TestCase.assertEquals(1, Maths.bitsRequired(1));
        TestCase.assertEquals(2, Maths.bitsRequired(2));
        TestCase.assertEquals(3, Maths.bitsRequired(4));
        TestCase.assertEquals(4, Maths.bitsRequired(8));
        TestCase.assertEquals(5, Maths.bitsRequired(16));
        TestCase.assertEquals(6, Maths.bitsRequired(32));
        TestCase.assertEquals(7, Maths.bitsRequired(64));
        TestCase.assertEquals(8, Maths.bitsRequired(128));
        TestCase.assertEquals(8, Maths.bitsRequired(200));
        TestCase.assertEquals(9, Maths.bitsRequired(300));

        TestCase.assertEquals(0, Maths.bitsRequired(0L));
        TestCase.assertEquals(1, Maths.bitsRequired(1L));
        TestCase.assertEquals(2, Maths.bitsRequired(2L));
        TestCase.assertEquals(40, Maths.bitsRequired(0xffffffffffl));
    }

    @Test
    public void testFitPowerOf2() {
        TestCase.assertEquals(0, Maths.fitPowerOf2(0));
        TestCase.assertEquals(1, Maths.fitPowerOf2(1));
        TestCase.assertEquals(2, Maths.fitPowerOf2(2));
        TestCase.assertEquals(4, Maths.fitPowerOf2(3));
        TestCase.assertEquals(4, Maths.fitPowerOf2(4));
        TestCase.assertEquals(32, Maths.fitPowerOf2(20));
        TestCase.assertEquals(32, Maths.fitPowerOf2(32));
        TestCase.assertEquals(256, Maths.fitPowerOf2(200));
        TestCase.assertEquals(2048, Maths.fitPowerOf2(2000));
    }
}
