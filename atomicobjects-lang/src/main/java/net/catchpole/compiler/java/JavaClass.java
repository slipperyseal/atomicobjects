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
import net.catchpole.lang.Strings;

import java.util.ArrayList;
import java.util.List;

public class JavaClass implements CodeElement {
    private final String classPackage;
    private final String className;
    private final String qualifiedClassName;
    private final List<Member> members = new ArrayList<Member>();
    private final List<Constructor> constructors = new ArrayList<Constructor>();
    private final List<JavaMethod> javaMethods = new ArrayList<JavaMethod>();
    private final List<Class> interfaces = new ArrayList<Class>();
    private Class superClass;

    public JavaClass(String classPackage, String className) {
        this.classPackage = classPackage;
        this.className = className;
        this.qualifiedClassName = classPackage + '.' + className;
    }

    public void addInterface(Class clazz) {
        interfaces.add(clazz);
    }

    public void setSuperClass(Class clazz) {
        this.superClass = clazz;
    }

    public void addMember(Member javaMember) {
        members.add(javaMember);
    }

    public void addConstuctor(Constructor constructor) {
        constructors.add(constructor);
    }

    public void addMethod(JavaMethod javaMethod) {
        javaMethods.add(javaMethod);
    }

    public String getPackageName() {
        return classPackage;
    }

    public String getSimpleName() {
        return className;
    }

    public String getName() {
        return qualifiedClassName;
    }

    public String getNameForMember(Class classDef) {
        String name = Strings.javaCase(classDef.getSimpleName());
        while (hasMemberNamed(name)) {
            name += "2";
        }
        return name;
    }

    public boolean hasMemberNamed(String name) {
        for (Member member : members) {
            if (member.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void print(CodeWriter codeWriter) {
        // package
        codeWriter.print("package ");
        codeWriter.print(classPackage);
        codeWriter.println(';');
        codeWriter.println();

        // class
        codeWriter.print("public class ");
        codeWriter.print(className);

        // extends
        if (superClass != null) {
            codeWriter.print(" extends ");
            codeWriter.print(superClass.getName());
        }

        // implements interfaces
        if (interfaces.size() != 0) {
            codeWriter.print(" implements ");
            boolean first = true;
            for (Class clazz : interfaces) {
                if (!first) {
                    codeWriter.print(',');
                }
                first = false;
                codeWriter.print(clazz.getName());
            }
        }
        codeWriter.println(" {");
        codeWriter.indent();

        // members
        for (Member member : members) {
            member.print(codeWriter);
        }

        // constructors
        //printElements(constructors, codeWriter);
        for (Constructor constructor : constructors) {
            constructor.print(codeWriter);
        }

        // methods
        for (JavaMethod javaMethod : javaMethods) {
            javaMethod.print(codeWriter);
        }

        // members getters and setters
        for (Member member : members) {
            if (member.isGetable()) {
                codeWriter.println();
                codeWriter.print("public ");
                codeWriter.print(member.getType().getSimpleName());
                codeWriter.print(" get");
                codeWriter.print(Strings.sentenceCase(member.getName()));
                codeWriter.println("() {");
                codeWriter.indent();
                codeWriter.print("return this.");
                codeWriter.print(member.getName());
                codeWriter.println(';');
                codeWriter.outdent();
                codeWriter.println('}');
            }
            if (member.isSetable()) {
                codeWriter.println();
                codeWriter.print("public void set");
                codeWriter.print(Strings.sentenceCase(member.getName()));
                codeWriter.print("(");
                codeWriter.print(member.getType().getSimpleName());
                codeWriter.print(' ');
                String var = member.getName();
                codeWriter.print(var);
                codeWriter.println(") {");
                codeWriter.indent();
                codeWriter.print("this.");
                codeWriter.print(var);
                codeWriter.print(" = ");
                codeWriter.print(var);
                codeWriter.println(';');
                codeWriter.outdent();
                codeWriter.println('}');
            }
        }

        codeWriter.outdent();
        codeWriter.println('}');
    }

    public void printElements(List<CodeElement> list, CodeWriter codeWriter) {
        if (list.size() > 0) {
            for (CodeElement codeElement : list) {
                codeElement.print(codeWriter);
            }

            codeWriter.println();
        }
    }
}
