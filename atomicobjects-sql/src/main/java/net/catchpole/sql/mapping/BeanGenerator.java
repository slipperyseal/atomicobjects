package net.catchpole.sql.mapping;

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

import net.catchpole.lang.Strings;
import net.catchpole.sql.meta.Column;
import net.catchpole.sql.meta.DatabaseMetaTools;
import net.catchpole.sql.meta.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class BeanGenerator {
    private Table table;
    private String packageName;
    private String className;
    private boolean testBounds;

    public BeanGenerator(Table table, String packageName, boolean testBounds) {
        this.table = table;
        this.packageName = packageName;
        this.testBounds = testBounds;
        this.className = Strings.javaCase(table.getTableName(), true);
    }

    public void write(File dir) throws IOException {
        File file = new File(dir, className + ".java");
        OutputStream os = new FileOutputStream(file);
        try {
            write(new PrintWriter(os));
        } finally {
            os.close();
        }
    }

    public String getClassName() {
        return className;
    }

    public void write(PrintWriter pw) {
        pw.print("package ");
        pw.println(packageName);
        pw.println();

        pw.print("public class ");
        pw.print(className);
        pw.println(" implements Serializable {");

        for (Column column : table.getColumns()) {
            pw.print("    private ");
            pw.print(DatabaseMetaTools.getJavaMappingType(column));
            pw.print(' ');
            pw.print(Strings.javaCase(column.getColumnName(), false));
            pw.println(';');
        }

        pw.println();

        StringBuilder toString = new StringBuilder(128);
        for (Column column : table.getColumns()) {
            String type = DatabaseMetaTools.getJavaMappingType(column);
            String memberName = Strings.javaCase(column.getColumnName(), false);
            String methodName = Strings.javaCase(column.getColumnName(), true);

            toString.append(" + ' ' + ");
            toString.append(memberName);

            pw.print("    public ");
            pw.print(type);
            pw.print(" get");
            pw.print(methodName);
            pw.println("() {");

            pw.print("        return this.");
            pw.print(memberName);
            pw.println(";");
            pw.println("    }");
            pw.println();

            pw.print("    public void set");
            pw.print(methodName);
            pw.print("(");
            pw.print(type);
            pw.print(' ');
            pw.print(memberName);
            pw.println(") {");

            if (testBounds && type.equals("java.lang.String")) {
                pw.print("        if (");
                pw.print(memberName);
                pw.print(" != null && ");
                pw.print(memberName);
                pw.print(".length() > ");
                pw.print(column.getColumnSize());
                pw.println(") {");
                pw.print("            throw new IllegalArgumentException(\"");
                pw.print(className);
                pw.print('.');
                pw.print(memberName);
                pw.print(" > ");
                pw.print(column.getColumnSize());
                pw.println("\");");
                pw.println("        }");
            }

            pw.print("        this.");
            pw.print(memberName);
            pw.print(" = ");
            pw.print(memberName);
            pw.println(";");
            pw.println("    }");
            pw.println();
        }
        pw.println("    public String toString() {");
        pw.print("        return \"");
        pw.print(className);
        pw.print(":\"");
        pw.print(toString);
        pw.println(';');
        pw.println("    }");

        pw.println('}');
        pw.flush();
    }
}
