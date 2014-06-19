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
import net.catchpole.lang.Serializer;
import net.catchpole.trace.Core;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class LongHashmapTest {
    @Test
    public void testLongHashmap() throws Exception {
        LongHashMap map = new LongHashMap(8);
        testLongHashmap(map, 8000);
        map = (LongHashMap) new Serializer().deserialize(new Serializer().serialize(map));
        testContents(map, 8000);

        testLongHashmap(new LongHashMap(9), 40000);
    }

    public static void testLongHashmap(LongTable longTable, int entries) {
        // add entries plus some extras
        for (long x = 1; x < entries + 1 + 100; x++) {
            longTable.put(x, new Long(x));
        }
        // remove the extras
        for (long x = entries + 1; x < entries + 1 + 100; x++) {
            longTable.remove(x);
        }
        Core.getTrace().info("Added " + entries + " test entries to " + longTable);
        TestCase.assertEquals(entries, longTable.size());

        testContents(longTable, entries);
    }

    public static void testContents(LongTable longTable, int entries) {
        TestCase.assertEquals(entries, longTable.size());

        ArrayList<Long> list = new ArrayList<Long>();
        LongIterator li = longTable.getKeyIterator();
        while (li.hasNext()) {
            long key = li.next();
            if (key != ((Long) longTable.get(key)).longValue()) {
                TestCase.fail();
            }
            list.add(new Long(key));
        }

        Long[] keys = list.toArray(new Long[list.size()]);
        Arrays.sort(keys);
        int x = 1;
        for (Long longKey : keys) {
            if (x++ != longKey.longValue()) {
                TestCase.fail();
            }
        }
        TestCase.assertEquals(keys.length, entries);

        Core.getTrace().info("Checked " + list.size() + " entries from " + longTable);
    }
}
