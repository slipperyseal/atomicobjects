package net.catchpole.lang.reflect;

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

import java.util.Date;

public class ReflectionTest {
    @Test
    public void testFromString() throws Exception {
        assertObject(new Integer(99), Reflection.fromString(Integer.class, "99"));
        assertObject(new Long(99), Reflection.fromString(Long.class, "99"));
        assertObject(new Short((short) 99), Reflection.fromString(Short.class, "99"));
        assertObject(new Character('a'), Reflection.fromString(Character.class, "a"));
        assertObject(new Double(99.9), Reflection.fromString(Double.class, "99.9"));
        assertObject(new Float(99.9F), Reflection.fromString(Float.class, "99.9"));
        assertObject(new Boolean(true), Reflection.fromString(Boolean.class, "true"));
        assertObject(new Boolean(true), Reflection.fromString(Boolean.class, "on"));
        assertObject(new Boolean(false), Reflection.fromString(Boolean.class, "false"));
    }

    private void assertObject(Object object1, Object object2) {
        Core.getTrace().info(object1.getClass().getName() + ' ' + object1);
        Core.getTrace().info(object2.getClass().getName() + ' ' + object2);

        TestCase.assertEquals(object1, object2);
    }

    public void testSetMethod() {
        Date date = new Date();

        TestCase.assertTrue("callSetMethod", Reflection.callSetMethod(date, "setTime", "1000"));

        TestCase.assertEquals(1000, date.getTime());
    }
}
