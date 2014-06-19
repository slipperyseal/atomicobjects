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

import java.util.HashMap;

public class DynamicClassLoader extends ClassLoader {
    private final HashMap<String, byte[]> bytesTable = new HashMap<String, byte[]>();

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addJavaClass(ClassFile javaClass) throws Exception {
        addClass(javaClass.getClassName(), javaClass.build());
    }

    public void removeJavaClass(ClassFile javaClass) {
        removeClass(javaClass.getClassName());
    }

    public void addClass(String name, byte[] bytes) throws Exception {
        this.bytesTable.put(name, bytes);
    }

    public void removeClass(String name) {
        this.bytesTable.remove(name);
    }

    public Class findClass(String name) {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassData(String name) {
        return bytesTable.get(name);
    }
}
