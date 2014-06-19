package net.catchpole.compiler.java;

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

import net.catchpole.compiler.writer.CodeWriter;
import net.catchpole.lang.reflect.ClassProfile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class JavaMethod implements CodeElement {
    private final String name;
    private final Class returnType;
    private final LinkedHashMap<String, Class> params = new LinkedHashMap<String, Class>();
    private final List<Statement> statements = new ArrayList<Statement>();
    private final List<Throwable> exceptions = new ArrayList<Throwable>();
    private int paramNumber = 1;

    public JavaMethod(String name, Class returnType) {
        this.name = name;
        this.returnType = returnType != null && returnType.getName().equals("null") ? null : returnType;
    }

    public void addParam(Class type, String name) {
        params.put(name, type);
    }

    public String addParam(Class type) {
        String name = "p" + paramNumber++;
        params.put(name, type);
        return name;
    }

    public void addStatement(Statement statement) {
        this.statements.add(statement);
    }

    public void addException(Throwable throwable) {
        this.exceptions.add(throwable);
    }

    public void print(CodeWriter codeWriter) {
        codeWriter.println();
        codeWriter.print("public ");
        codeWriter.print((returnType == null) ? "void" : new ClassProfile(returnType).getSourceDefinition());
        codeWriter.print(' ');
        codeWriter.print(name);
        codeWriter.print('(');

        boolean first = true;
        for (String paramName : params.keySet()) {
            if (!first) {
                codeWriter.print(", ");
            }
            first = false;
            codeWriter.print(new ClassProfile(params.get(paramName)).getSourceDefinition());
            codeWriter.print(' ');
            codeWriter.print(paramName);
        }
        codeWriter.print(") ");

        boolean firstException = true;
        for (Throwable throwable : exceptions) {
            if (firstException) {
                codeWriter.print("throws ");
                firstException = false;
            } else {
                codeWriter.print(',');
            }
            codeWriter.print(throwable.getClass().getName());
            codeWriter.print(' ');
        }
        codeWriter.println("{");
        codeWriter.indent();
        for (Statement statement : statements) {
            statement.print(codeWriter);
        }
        codeWriter.outdent();
        codeWriter.println('}');
    }
}
