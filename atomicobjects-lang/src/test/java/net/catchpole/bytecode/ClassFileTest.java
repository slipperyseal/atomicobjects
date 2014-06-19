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

import net.catchpole.trace.Core;
import net.catchpole.trace.Trace;
import org.junit.Test;

public class ClassFileTest {
    private DynamicClassLoader dynamicClassLoader = new DynamicClassLoader(this.getClass().getClassLoader());
    private Trace trace = Core.getTrace(this);

    private Class load(Trace trace, ClassFile classFile) throws Exception {
        trace.info(classFile.toDetailedString());
        dynamicClassLoader.addJavaClass(classFile);
        Class clazz = dynamicClassLoader.findClass(classFile.getClassName());
        trace.info(clazz);
        return clazz;
    }

    @Test
    public void testClassFileSelf() throws Exception {
        Trace trace = this.trace.drill();

        ClassFile classFile = new ClassFile(ClassFileTest.class);
        load(trace, classFile);
    }

    @Test
    public void testClassFileNew() throws Exception {
        Trace trace = this.trace.drill();

        ClassFile classFile = new ClassFile("net.catchpole.Test");
        trace.info(classFile.toDetailedString());
        //load(classFile);
    }

    @Test
    public void testClassFileAddConstant() throws Exception {
        Trace trace = this.trace.drill();

        ClassFile classFile = new ClassFile("net.catchpole.Test");
        classFile.addConstant("Test 1 2 3");
        trace.info(classFile.toDetailedString());
        //load(classFile);
    }
}

