package net.catchpole.fuse;

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
import net.catchpole.fuse.criteria.Criteria;
import net.catchpole.fuse.example.StreamEater;
import net.catchpole.search.Search;
import net.catchpole.search.SimpleSearch;
import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;
import org.junit.Test;

import java.io.InputStream;

public class PlanTest {
    private Trace trace = Core.getTrace();
    private FuseBox fuseBox = new ExampleFuseBox().get();
    private Search search = new SimpleSearch(fuseBox);

    @Test
    public void testFuse_String_ByteArray() throws Exception {
        test("Turn me into a byte array", null, byte[].class);
    }

    @Test
    public void testFuse_ByteArray_InputStream() throws Exception {
        test("Turn me into an InputStream".getBytes(), null, InputStream.class);
    }

    @Test
    public void testFuse_String_InputStream() throws Exception {
        test("Turn me into an InputStream", null, InputStream.class);
    }

    @Test
    public void testFuse_String_StreamEater() throws Exception {
        test("Turn me into a StreamEater", null, StreamEater.class);
    }

    public Object test(Object parameter, String name, Class resultClass) throws Exception {
        trace.info();
        trace.info("INPUT  >", parameter, parameter.getClass(), name);
        Object[] resultArray = find(parameter, name, resultClass);
        TestCase.assertNotNull(resultArray);
        TestCase.assertTrue(resultArray.length > 0);
        Object result = resultArray[0];
        trace.info("RESULT >", result, result.getClass());
        TestCase.assertTrue(resultClass.isAssignableFrom(result.getClass()));
        return result;
    }

    public Object[] find(Object inputObject, String inputName, Class outputClass) throws Exception {
        try {

            Plan plan = new Plan(search.search(new Criteria(inputName, inputObject.getClass()), new Criteria(outputClass)));

            trace.info(plan);
            for (Fuse fuse : plan.getFuseList()) {
                trace.info("  fuse: ", fuse);
            }

            for (Object object : plan.getInvolkIterable(new Object[]{inputObject})) {
                trace.info("  iterator: " + object);
            }

            return plan.involk(new Object[]{inputObject});
        } catch (Exception e) {
            TestCase.fail(e.getClass().getName() + ' ' + e.getMessage());
        }
        return null;
    }
}
