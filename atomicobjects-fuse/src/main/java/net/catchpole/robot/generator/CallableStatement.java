package net.catchpole.robot.generator;

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

import net.catchpole.compiler.java.Member;
import net.catchpole.compiler.java.Statement;
import net.catchpole.compiler.writer.CodeWriter;
import net.catchpole.lang.Throw;
import net.catchpole.lang.reflect.ClassProfile;

import java.lang.reflect.Method;

public class CallableStatement implements Statement {
    private final static Method targetMethod;
    static {
        try {
            targetMethod = Callable.class.getMethod("call", String.class, Object[].class);
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    private final Method sourceMethod;
    private final Member member;
    private final boolean returning;

    public CallableStatement(Method sourceMethod, Member member, boolean returning) {
        this.sourceMethod = sourceMethod;
        this.member = member;
        this.returning = returning;
    }

    public void print(CodeWriter codeWriter) {
        final boolean doReturn = (returning && !sourceMethod.getReturnType().equals(Void.TYPE));
        final Class returnType = sourceMethod.getReturnType();

        if (doReturn) {
            codeWriter.print("return ");
            if (!returnType.isPrimitive()) {
                if (!returnType.isAssignableFrom(Object.class)) {
                    codeWriter.print('(');
                    codeWriter.print(new ClassProfile(returnType).getSourceDefinition());
                    codeWriter.print(')');
                }
            } else {
                codeWriter.print(new ClassProfile(returnType).getSourceUnboxingPre());
            }
        }

        codeWriter.print("this.");
        codeWriter.print(member.getName());
        codeWriter.print('.');
        codeWriter.print(targetMethod.getName());
        codeWriter.print("(\"");
        codeWriter.print(sourceMethod.getName());
        codeWriter.print("\", ");

        Class[] paramTypes = sourceMethod.getParameterTypes();
        if (paramTypes.length > 0) {
            codeWriter.print("new Object[] { ");
            int p = 1;
            for (Class param : paramTypes) {
                if (p != 1) {
                    codeWriter.print(", ");
                }
                codeWriter.print(new ClassProfile(param).getSourceBoxing("p"+ p++));
            }
            codeWriter.print(" }");
        } else {
            codeWriter.print("null");
        }
        codeWriter.print(" )");
        if (doReturn) {
            if (returnType.isPrimitive()) {
                codeWriter.print(new ClassProfile(returnType).getSourceUnboxingPost());
            }
        }
        codeWriter.println(";");
    }
}
