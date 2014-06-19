package net.catchpole.compiler;

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

import net.catchpole.lang.Throw;
import org.codehaus.janino.JavaSourceClassLoader;
import org.codehaus.janino.util.resource.Resource;
import org.codehaus.janino.util.resource.ResourceFinder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class JaninoRuntimeCompiler implements RuntimeCompiler {
    private final ClassLoader classloader;
    private final Map<String, Resource> sourceTable = new HashMap<String, Resource>();

    public JaninoRuntimeCompiler() {
        this.classloader = new JavaSourceClassLoader(
                this.getClass().getClassLoader(),
                new ResourceFinder() {
                    public Resource findResource(String filename) {
                        return sourceTable.get(filename);
                    }
                },
                null,null
        );
    }

    public Class compile(final String className, final InputStream javaSource) {
        try {
            final String filename = classNameToSourcePath(className);
            final long lastModified = System.currentTimeMillis();

            sourceTable.put(filename, new Resource() {
                public String getFileName() {
                    return filename;
                }

                public long lastModified() {
                    return lastModified;
                }

                public InputStream open() throws IOException {
                    return javaSource;
                }
            });

            return classloader.loadClass(className);
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    private String classNameToSourcePath(final String className) {
        return className.replace('.', '/') + ".java";
    }
}
