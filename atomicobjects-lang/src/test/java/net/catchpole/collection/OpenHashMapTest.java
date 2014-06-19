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
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class OpenHashMapTest {
    private int len = 50000;
    private int loops = 50;
    private Integer[] values = new Integer[len];

    @Test
    public void testQuickHashMap() {
        // precalculate values so that no extra allocs occur during time operation
        for (int x = 0; x < len; x++) {
            values[x] = new Integer(x);
        }

        compare(new OpenHashMap<Integer, Integer>(), new HashMap<Integer, Integer>());
        compare(new OpenHashMap<Integer, Integer>(), new HashMap<Integer, Integer>());
    }

    private void compare(Map<Integer, Integer> map1, Map<Integer, Integer> map2) {
        double a = time(map1);
        double b = time(map2);
        Core.getTrace().info(map1.getClass().getName() + " is " + ((double) ((int) ((b / a) * 1000))) / 1000 + " times faster than " + map2.getClass().getName());
    }

    private int time(Map<Integer, Integer> map) {
        long start = System.currentTimeMillis();

        for (int y = 0; y < loops; y++) {
            map.clear();
            for (int x = 0; x < len; x++) {
                map.put(values[x], values[x]);
            }

            TestCase.assertEquals(len, map.size());

            for (int x = 0; x < len; x++) {
                if (!values[x].equals(map.get(values[x]))) {
                    TestCase.fail("Fail to get key " + x);
                }
            }
        }

        int result = (int) (System.currentTimeMillis() - start);
        Core.getTrace().info((loops * len) + " puts and gets: " + map.getClass().getName() + ' ' + result + " millis");
        return result;
    }
}
