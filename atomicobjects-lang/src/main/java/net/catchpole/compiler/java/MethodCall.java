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
import net.catchpole.lang.Throw;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodCall implements Statement {
    private final Method sourceMethod;
    private final Method targetMethod;
    private final Member member;
    private final boolean returning;

    public MethodCall(Method sourceMethod, Member member, boolean returning) {
        this.sourceMethod = sourceMethod;
        try {
            this.targetMethod = member.getType().getMethod(sourceMethod.getName(), sourceMethod.getParameterTypes());
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
        this.member = member;
        this.returning = returning;
    }

    public void print(CodeWriter codeWriter) {
        if (returning && !sourceMethod.getReturnType().equals(Void.TYPE)) {
            codeWriter.print("return ");
        }

        if (Modifier.isStatic(targetMethod.getModifiers())) {
            codeWriter.print(targetMethod.getClass().getName());
        } else {
            if (member == null) {
                throw new IllegalArgumentException("No member for non-static method: " + sourceMethod.getName());
            }
            codeWriter.print("this.");
            codeWriter.print(member.getName());
        }
        codeWriter.print('.');
        codeWriter.print(targetMethod.getName());
        codeWriter.print('(');
        int p = 1;
        for (Class param : targetMethod.getParameterTypes()) {
            if (p != 1) {
                codeWriter.print(", ");
            }
            codeWriter.print("p" + p++);
        }
        codeWriter.println(");");
    }
}
