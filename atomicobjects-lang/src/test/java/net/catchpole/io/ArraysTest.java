package net.catchpole.io;

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
import net.catchpole.trace.Core;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ArraysTest {
    @Test
    public void testMaxIndex() {
        float[] values = new float[]{0.1F, 0.2F, 0.3F, 0.2F, 0.1F};

        TestCase.assertEquals(2, Arrays.maxIndex(values));
    }

    @Test
    public void testBitOperations() {
        TestCase.assertEquals(9, Arrays.setBit(1, 3, true));
        TestCase.assertEquals(1, Arrays.setBit(9, 3, false));

        long[] bits = new long[]{0, 0};
        Arrays.setBit(bits, 64, true);
        TestCase.assertEquals(0, bits[0]);
        TestCase.assertEquals(1, bits[1]);
        TestCase.assertTrue(Arrays.isBit(bits, 64));

        Arrays.setBit(bits, 64, false);
        TestCase.assertEquals(0, bits[0]);
        TestCase.assertEquals(0, bits[1]);
        TestCase.assertFalse(Arrays.isBit(bits, 64));
    }

    @Test
    public void testListReverse() {
        {
            List list = new ArrayList();
            list.add("1");
            list.add("2");
            list.add("3");
            list.add("4");

            Arrays.reverse(list);

            TestCase.assertEquals("4", list.get(0));
            TestCase.assertEquals("3", list.get(1));
            TestCase.assertEquals("2", list.get(2));
            TestCase.assertEquals("1", list.get(3));
        }
        {
            List list = new ArrayList();
            list.add("1");
            list.add("2");
            list.add("3");

            Arrays.reverse(list);

            TestCase.assertEquals("3", list.get(0));
            TestCase.assertEquals("2", list.get(1));
            TestCase.assertEquals("1", list.get(2));
        }
    }

    @Test
    public void testIndexOf() throws IOException {
        TestCase.assertEquals(5, Arrays.indexOf("1234567890".getBytes(), 10, "678".getBytes()));
        TestCase.assertEquals(-1, Arrays.indexOf("1234567890".getBytes(), 10, "xyz".getBytes()));
    }

    @Test
    public void testSpoolUntilBounaryLines() throws IOException {
        InputStream is = new ByteArrayInputStream(
                "ABC1ABCDEABC2ABCDEABCDEABC3ABCDEABC4ABCDEExtra".getBytes()
            );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        TestCase.assertTrue(Arrays.spoolUntilBoundary(is, baos, "ABCDE".getBytes()));
        TestCase.assertEquals("ABC1", baos.toString());

        TestCase.assertTrue(Arrays.spoolUntilBoundary(is, baos, "ABCDE".getBytes()));
        TestCase.assertEquals("ABC1ABC2", baos.toString());

        TestCase.assertTrue(Arrays.spoolUntilBoundary(is, baos, "ABCDE".getBytes()));
        TestCase.assertEquals("ABC1ABC2", baos.toString());

        TestCase.assertTrue(Arrays.spoolUntilBoundary(is, baos, "ABCDE".getBytes()));
        TestCase.assertEquals("ABC1ABC2ABC3", baos.toString());

        TestCase.assertTrue(Arrays.spoolUntilBoundary(is, baos, "ABCDE".getBytes()));
        TestCase.assertEquals("ABC1ABC2ABC3ABC4", baos.toString());

        TestCase.assertFalse(Arrays.spoolUntilBoundary(is, baos, "ABCDE".getBytes()));
        TestCase.assertEquals("ABC1ABC2ABC3ABC4Extra", baos.toString());

        Core.getTrace().info(baos);
    }
}
