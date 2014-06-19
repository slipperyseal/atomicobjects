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

/**
 */
public class StringsTest {
    @Test
    public void testToUpperCase() {
        TestCase.assertEquals('B', Strings.toUpperCase('B'));
        TestCase.assertEquals('B', Strings.toUpperCase('b'));
    }

    @Test
    public void testToLowerCase() {
        TestCase.assertEquals('b', Strings.toLowerCase('B'));
        TestCase.assertEquals('b', Strings.toLowerCase('b'));
    }

    @Test
    public void testToJavaCase() {
        TestCase.assertEquals("thisIsTheMethod", Strings.javaCase("THIS_IS-THE_ METHOD", false));
        TestCase.assertEquals("ThisIsTheMethod", Strings.javaCase("THIS_IS-THE_ METHOD", true));
    }
}

