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

import java.util.ArrayList;
import java.util.List;

public class Constructor implements CodeElement {
    private final JavaClass javaClass;
    private final List<Member> memberParams = new ArrayList<Member>();

    public Constructor(JavaClass javaClass) {
        this.javaClass = javaClass;
    }

    public void addParameter(Member member) {
        this.memberParams.add(member);
    }

    public void print(CodeWriter codeWriter) {
        codeWriter.println();
        codeWriter.print("public ");
        codeWriter.print(javaClass.getSimpleName());
        codeWriter.print('(');
        int x = 0;
        for (Member member : memberParams) {
            if (x++ != 0) {
                codeWriter.print(',');
            }
            codeWriter.print(member.getType().getName());
            codeWriter.print(' ');
            codeWriter.print(member.getName());
        }
        codeWriter.println(") {");
        codeWriter.indent();

        for (Member member : memberParams) {
            codeWriter.print("this.");
            codeWriter.print(member.getName());
            codeWriter.print(" = ");
            codeWriter.print(member.getName());
            codeWriter.println(';');
        }

        codeWriter.outdent();
        codeWriter.println("}");
    }
}
