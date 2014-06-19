package net.catchpole.bytecode;

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
import net.catchpole.classpath.DynamicURLClassLoader;
import org.junit.Test;

import java.io.File;

public class DynamicURLClassLoaderTest {
    @Test
    public void testClassLoader() throws Exception {
        DynamicURLClassLoader dynamicURLClassLoader = new DynamicURLClassLoader("local", new File("./classes/"));

        // instantiate a class from the project which probably wont be deleted or refactored, and not in the
        // system class loader
        String className = "java.util.HashMap";
        Object object = dynamicURLClassLoader.loadClass(className).newInstance();

        TestCase.assertEquals(className, object.getClass().getName());
    }
}
