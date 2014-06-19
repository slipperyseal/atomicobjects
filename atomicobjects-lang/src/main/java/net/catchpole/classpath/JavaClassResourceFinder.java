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

import net.catchpole.compiler.java.JavaClass;
import org.codehaus.janino.util.resource.Resource;
import org.codehaus.janino.util.resource.ResourceFinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JavaClassResourceFinder extends ResourceFinder {
    private final Map<String, Resource> map = new HashMap<String, Resource>();

    public JavaClassResourceFinder() {
    }

    public Resource findResource(String s) {
        return map.get(s);
    }

    public void add(String name, JavaClass javaClass) {
        map.put(name, new MemoryResource(name, javaClass));
    }

    class MemoryResource implements Resource {
        private String name;
        private JavaClass javaClass;
        private long lastModified = System.currentTimeMillis();

        public MemoryResource(String name, JavaClass javaClass) {
            this.name = name;
            this.javaClass = javaClass;
        }

        public String getFileName() {
            return name;
        }

        public long lastModified() {
            return lastModified;
        }

        public InputStream open() throws IOException {
            //return new ByteArrayInputStream(javaClass.printToBytes());
            throw new RuntimeException("fix this");
        }
    }
}
