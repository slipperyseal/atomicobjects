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
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;
import org.junit.Test;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

public class TypeInheritanceTest {
    private Trace trace = Core.getTrace();
    
    @Test
    public void testNumberFormatException() {
        TypeInheritance typeInheritance = new TypeInheritance(NumberFormatException.class);

        trace.info(typeInheritance);

        Iterator<Class> iterator = typeInheritance.iterator();
        TestCase.assertEquals(java.io.Serializable.class, iterator.next());
        TestCase.assertEquals(java.lang.Exception.class, iterator.next());
        TestCase.assertEquals(java.lang.IllegalArgumentException.class, iterator.next());
        TestCase.assertEquals(java.lang.NumberFormatException.class, iterator.next());
        TestCase.assertEquals(java.lang.RuntimeException.class, iterator.next());
        TestCase.assertEquals(java.lang.Throwable.class, iterator.next());
        TestCase.assertFalse(iterator.hasNext());
    }

    
    @Test
    public void testConcurrentHashMap() {
        TypeInheritance typeInheritance = new TypeInheritance(ConcurrentHashMap.class);

        trace.info(typeInheritance);

        Iterator<Class> iterator = typeInheritance.iterator();

        TestCase.assertEquals(java.io.Serializable.class, iterator.next());
        TestCase.assertEquals(java.util.AbstractMap.class, iterator.next());
        TestCase.assertEquals(java.util.Map.class, iterator.next());
        TestCase.assertEquals(java.util.concurrent.ConcurrentHashMap.class, iterator.next());
        TestCase.assertEquals(java.util.concurrent.ConcurrentMap.class, iterator.next());

        TestCase.assertFalse(iterator.hasNext());
    }
}
