package net.catchpole.robot;

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

import org.junit.Test;

public class DelegateGeneratorTest {
    @Test
    public void testDelegateGenerator_List() throws Exception {
//    todo: fix
//        Implementor implementor = new Implementor();
//
//        DelegateGenerator delegateGenerator = new DelegateGenerator(ArrayList.class);
//        Class clazz = implementor.implement(delegateGenerator, List.class);
//
//        assertEquals("net.catchpole.runtime.java.util.List", clazz.getName());
//
//        ArrayList arrayList = new ArrayList();
//        arrayList.add("Yo!");
//        List list = (List)clazz.getConstructor(ArrayList.class).newInstance(arrayList);
//        assertEquals(1, list.size());
    }

    @Test
    public void testDelegateGenerator_Callable() throws Exception {
//    todo: fix
//        Implementor implementor = new Implementor();
//
//        DelegateGenerator delegateGenerator = new DelegateGenerator(Callable.class);
//        Class clazz = implementor.implement(delegateGenerator, List.class);
//
//        final Object[] results = new Object[2];
//        List list = (List)clazz.getConstructor(Callable.class).newInstance(new Callable() {
//                public Object call(String methodName, Object[] parameters) {
//                    results[0] = methodName;
//                    results[1] = parameters[0];
//                    return Boolean.TRUE;
//                }
//            });
//
//        assertEquals(true, list.add("happy"));
//        assertEquals(results[0], "add");
//        assertEquals(results[1], "happy");
    }
}
