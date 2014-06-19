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

import net.catchpole.compiler.JaninoRuntimeCompiler;
import net.catchpole.compiler.RuntimeCompiler;
import net.catchpole.compiler.writer.OutputStreamCodeWriter;
import net.catchpole.io.Arrays;
import net.catchpole.io.MemoryInputOutputStream;
import net.catchpole.lang.Throw;
import net.catchpole.trace.Core;

public class Implementor {
    private RuntimeCompiler runtimeCompiler = new JaninoRuntimeCompiler();

    public Implementor() {
    }

    public Class implement(SourceGenerator sourceGenerator, Class implementationType) {
        MemoryInputOutputStream memoryInputOutputStream = new MemoryInputOutputStream(1024);
        try {
            final String packageName = "net.catchpole.runtime." + implementationType.getPackage().getName();
            final String generatedName = packageName + '.' + implementationType.getSimpleName();

            OutputStreamCodeWriter outputStreamCodeWriter = new OutputStreamCodeWriter(memoryInputOutputStream.getOutputStream());

            sourceGenerator.generateSource(implementationType, outputStreamCodeWriter, packageName, implementationType.getSimpleName());
            outputStreamCodeWriter.close();

            Core.getTrace().info(new String(Arrays.streamToByteArray(memoryInputOutputStream.getInputStream())));

            return runtimeCompiler.compile(generatedName, memoryInputOutputStream.getInputStream());
        } catch (Exception e) {
            Core.getTrace().info(new String(memoryInputOutputStream.getBytes()));
            throw Throw.unchecked(e);
        }
    }

    public Object getInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
