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

import net.catchpole.compiler.java.Constructor;
import net.catchpole.compiler.java.JavaClass;
import net.catchpole.compiler.java.JavaMethod;
import net.catchpole.compiler.java.Member;
import net.catchpole.compiler.java.MethodCall;
import net.catchpole.compiler.java.Statement;
import net.catchpole.compiler.writer.CodeWriter;
import net.catchpole.lang.reflect.ClassProfile;
import net.catchpole.robot.SourceGenerator;

import java.lang.reflect.Method;

public class DelegateGenerator implements SourceGenerator {
    private final Class targetType;

    public DelegateGenerator(Class targetType) {
        this.targetType = targetType;
    }

    public void generateSource(Class implementationType, CodeWriter codeWriter, String packageName, String className) {
        JavaClass javaClass = new JavaClass(packageName, className);
        javaClass.addInterface(implementationType);

        Member member = new Member(targetType, javaClass.getNameForMember(targetType), false, false);
        javaClass.addMember(member);

        Constructor constructor = new Constructor(javaClass);
        constructor.addParameter(member);
        javaClass.addConstuctor(constructor);

        for (Method method : new ClassProfile(implementationType).getNonObjectMethods()) {
            JavaMethod javaMethod = new JavaMethod(method.getName(), method.getReturnType());

            for (Class paramTypes : method.getParameterTypes()) {
                javaMethod.addParam(paramTypes);
            }

            Statement methodCall = targetType.isAssignableFrom(Callable.class) ?
                    new CallableStatement(method, member, true) :
                    new MethodCall(method, member, true);

            javaMethod.addStatement(methodCall);

            javaClass.addMethod(javaMethod);
        }

        javaClass.print(codeWriter);
    }
}
