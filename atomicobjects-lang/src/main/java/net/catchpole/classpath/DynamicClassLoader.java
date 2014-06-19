package net.catchpole.classpath;

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

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public final class DynamicClassLoader extends ClassLoader {
    private final ClassSource classSource;

    public DynamicClassLoader(ClassLoader parent, ClassSource classSource) {
        super(parent);
        this.classSource = classSource;
    }

    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = classSource.findClass(name);
        if (bytes == null) {
            throw new ClassNotFoundException(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }

    protected URL findResource(String string) {
        return super.findResource(string);
    }

    protected Enumeration<URL> findResources(String string) throws IOException {
        return super.findResources(string);
    }
}
