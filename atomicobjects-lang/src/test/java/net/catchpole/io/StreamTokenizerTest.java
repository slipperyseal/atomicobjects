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
import org.junit.Test;

import java.io.ByteArrayInputStream;

/**
 */
public class StreamTokenizerTest {
    @Test
    public void testStreamTokenizer() {
        StreamTokenIterator streamTokenIterator = new StreamTokenIterator(
                new ByteArrayInputStream("\t Ay Bee  \t\r\nCee Dee \t".getBytes()));

        TestCase.assertTrue(streamTokenIterator.hasNext());
        TestCase.assertEquals("Ay", streamTokenIterator.next());
        TestCase.assertTrue(streamTokenIterator.hasNext());
        TestCase.assertEquals("Bee", streamTokenIterator.next());
        TestCase.assertTrue(streamTokenIterator.hasNext());
        TestCase.assertEquals("Cee", streamTokenIterator.next());
        TestCase.assertTrue(streamTokenIterator.hasNext());
        TestCase.assertEquals("Dee", streamTokenIterator.next());
        TestCase.assertFalse(streamTokenIterator.hasNext());
    }
}
