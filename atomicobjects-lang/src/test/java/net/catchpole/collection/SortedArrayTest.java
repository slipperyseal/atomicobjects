package net.catchpole.collection;

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
import net.catchpole.trace.Trace;
import org.junit.Test;

import java.util.Comparator;

public class SortedArrayTest {
    private final Trace trace = Core.getTrace();

    @Test
    public void testSortedArray1() {
        SortedArray<String> sortedArray = new SortedArray<String>();

        sortedArray.add("1");
        sortedArray.add("3");
        sortedArray.add("5");
        sortedArray.add("7");
        sortedArray.add("9");
        sortedArray.add("8");
        sortedArray.add("2");
        sortedArray.add("6");
        sortedArray.add("4");
        sortedArray.trim();
        TestCase.assertEquals(9, sortedArray.size());
        TestCase.assertEquals("1", sortedArray.elementAt(0));
        TestCase.assertEquals("2", sortedArray.elementAt(1));
        TestCase.assertEquals("3", sortedArray.elementAt(2));
        TestCase.assertEquals("4", sortedArray.elementAt(3));
        TestCase.assertEquals("5", sortedArray.elementAt(4));
        TestCase.assertEquals("6", sortedArray.elementAt(5));
        TestCase.assertEquals("7", sortedArray.elementAt(6));
        TestCase.assertEquals("8", sortedArray.elementAt(7));
        TestCase.assertEquals("9", sortedArray.elementAt(8));
    }

    @Test
    public void testSortedArray2() {
        SortedArray<String> sortedArray = new SortedArray<String>();

        sortedArray.add("3");
        sortedArray.add("5");
        sortedArray.add("7");
        sortedArray.add("9");
        sortedArray.add("8");
        sortedArray.add("2");
        sortedArray.add("6");
        sortedArray.add("4");
        sortedArray.add("1");
        sortedArray.trim();

        TestCase.assertEquals(9, sortedArray.size());
        TestCase.assertEquals("1", sortedArray.elementAt(0));
        TestCase.assertEquals("2", sortedArray.elementAt(1));
        TestCase.assertEquals("3", sortedArray.elementAt(2));
        TestCase.assertEquals("4", sortedArray.elementAt(3));
        TestCase.assertEquals("5", sortedArray.elementAt(4));
        TestCase.assertEquals("6", sortedArray.elementAt(5));
        TestCase.assertEquals("7", sortedArray.elementAt(6));
        TestCase.assertEquals("8", sortedArray.elementAt(7));
        TestCase.assertEquals("9", sortedArray.elementAt(8));
    }

    @Test
    public void testSortedArray3() {
        SortedArray<String> sortedArray = new SortedArray<String>();

        sortedArray.add("8");
        sortedArray.add("1");
        sortedArray.add("3");
        sortedArray.add("5");
        sortedArray.add("7");
        sortedArray.add("2");
        sortedArray.add("6");
        sortedArray.add("4");
        sortedArray.add("9");
        sortedArray.trim();

        TestCase.assertEquals(9, sortedArray.size());
        TestCase.assertEquals("1", sortedArray.elementAt(0));
        TestCase.assertEquals("2", sortedArray.elementAt(1));
        TestCase.assertEquals("3", sortedArray.elementAt(2));
        TestCase.assertEquals("4", sortedArray.elementAt(3));
        TestCase.assertEquals("5", sortedArray.elementAt(4));
        TestCase.assertEquals("6", sortedArray.elementAt(5));
        TestCase.assertEquals("7", sortedArray.elementAt(6));
        TestCase.assertEquals("8", sortedArray.elementAt(7));
        TestCase.assertEquals("9", sortedArray.elementAt(8));
    }

    @Test
    public void testSortedArray4() {
        SortedArray<String> sortedArray = new SortedArray<String>();

        sortedArray.add("4");
        sortedArray.add("1");
        sortedArray.add("4");
        sortedArray.add("3");
        sortedArray.add("2");
        sortedArray.add("1");
        sortedArray.add("2");
        sortedArray.add("3");
        sortedArray.trim();

        TestCase.assertEquals(8, sortedArray.size());
        TestCase.assertEquals("1", sortedArray.elementAt(0));
        TestCase.assertEquals("1", sortedArray.elementAt(1));
        TestCase.assertEquals("2", sortedArray.elementAt(2));
        TestCase.assertEquals("2", sortedArray.elementAt(3));
        TestCase.assertEquals("3", sortedArray.elementAt(4));
        TestCase.assertEquals("3", sortedArray.elementAt(5));
        TestCase.assertEquals("4", sortedArray.elementAt(6));
        TestCase.assertEquals("4", sortedArray.elementAt(7));
    }

    @Test
    public void testSortedArrayReplace() {
        SortedArray<String> sortedArray = new SortedArray<String>();

        TestCase.assertFalse(sortedArray.addUnique("4"));
        TestCase.assertTrue(sortedArray.addUnique("4"));
        TestCase.assertFalse(sortedArray.addUnique("3"));
        TestCase.assertTrue(sortedArray.addUnique("3"));
        TestCase.assertFalse(sortedArray.addUnique("1"));
        TestCase.assertTrue(sortedArray.addUnique("1"));
        TestCase.assertFalse(sortedArray.addUnique("2"));
        TestCase.assertTrue(sortedArray.addUnique("2"));
        sortedArray.trim();

        for (Comparable c : sortedArray) {
            trace.info(c);
        }

        TestCase.assertEquals(4, sortedArray.size());
        TestCase.assertEquals("1", sortedArray.elementAt(0));
        TestCase.assertEquals("2", sortedArray.elementAt(1));
        TestCase.assertEquals("3", sortedArray.elementAt(2));
        TestCase.assertEquals("4", sortedArray.elementAt(3));
    }

    @Test
    public void testSortedArrayReplace_Comparator() {
        SortedArray<String> sortedArray = new SortedArray<String>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                // test comparator is reverse sort
                return o1.compareTo(o2);
            }
        });

        TestCase.assertFalse(sortedArray.addUnique("4"));
        TestCase.assertTrue(sortedArray.addUnique("4"));
        TestCase.assertFalse(sortedArray.addUnique("3"));
        TestCase.assertTrue(sortedArray.addUnique("3"));
        TestCase.assertFalse(sortedArray.addUnique("1"));
        TestCase.assertTrue(sortedArray.addUnique("1"));
        TestCase.assertFalse(sortedArray.addUnique("2"));
        TestCase.assertTrue(sortedArray.addUnique("2"));
        sortedArray.trim();

        for (Comparable c : sortedArray) {
            trace.info(c);
        }

        TestCase.assertEquals(4, sortedArray.size());
        TestCase.assertEquals("1", sortedArray.elementAt(0));
        TestCase.assertEquals("2", sortedArray.elementAt(1));
        TestCase.assertEquals("3", sortedArray.elementAt(2));
        TestCase.assertEquals("4", sortedArray.elementAt(3));
    }

    @Test
    public void testSortedArrayComparator() {
        SortedArray<String> sortedArray = new SortedArray<String>(
                new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        // test comparator is reverse sort
                        return o2.compareTo(o1);
                    }
                }
        );

        TestCase.assertFalse(sortedArray.addUnique("4"));
        TestCase.assertTrue(sortedArray.addUnique("4"));
        TestCase.assertFalse(sortedArray.addUnique("3"));
        TestCase.assertTrue(sortedArray.addUnique("3"));
        TestCase.assertFalse(sortedArray.addUnique("1"));
        TestCase.assertTrue(sortedArray.addUnique("1"));
        TestCase.assertFalse(sortedArray.addUnique("2"));
        TestCase.assertTrue(sortedArray.addUnique("2"));
        sortedArray.trim();

        for (Comparable c : sortedArray) {
            trace.info(c);
        }

        TestCase.assertEquals(4, sortedArray.size());
        TestCase.assertEquals("4", sortedArray.elementAt(0));
        TestCase.assertEquals("3", sortedArray.elementAt(1));
        TestCase.assertEquals("2", sortedArray.elementAt(2));
        TestCase.assertEquals("1", sortedArray.elementAt(3));
    }
}
