package net.catchpole.build;

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

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.ExecutableMemberDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.Parameter;
import com.sun.javadoc.ProgramElementDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Type;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Generates a single file JavaDoc with expanding class method overview.
 */
public class BonsaiDoc {
    private final boolean wide = false;
    private final PrintStream ps;
    private ClassDoc[] classes;
    private PackageDoc[] packages;

    public static boolean start(RootDoc root) throws Exception {
        FileOutputStream fos = new FileOutputStream("./bonsaidoc.html");
        try {
            new BonsaiDoc(root, new PrintStream(fos));
        } finally {
            fos.close();
        }
        return true;
    }

    public BonsaiDoc(RootDoc root, PrintStream ps) {
        this.ps = ps;
        getDocuments(root);

        int indent = longestClassNameWithComment();

        ps.println("<html><body><script type=\"text/javascript\">");
        ps.println("function doExpand(p){if(p.style.display==\"none\"){p.style.display=\"\"; }else{p.style.display=\"none\";}}</script>");
        ps.println("<font face=\"arial\" size=\"5\">BonsaiDoc</font><hr><pre>");

        for (PackageDoc packageDoc : packages) {
            String name = packageDoc.name();
            ps.print("<a href=\"#" + firstClassInPackage(name).toString() + "\">");
            ps.print(name);
            ps.print("</a>");
            if (wide) {
                ps.println(comment(packageDoc.commentText(), indent - name.length()));
            } else {
                ps.print("<br>");
                ps.print(packageDoc.commentText());
                ps.println("<br>");
            }
        }

        ps.println();

        int index = 1;
        for (ClassDoc classDoc : classes) {
            String name = classDoc.toString();
            ps.print("<a name=\"" + name + "\" href=\"javascript:doExpand(d" + index + ")\">");
            ps.print(name);
            ps.print("</a> ");
            ps.println(comment(classDoc.commentText(), indent - name.length()));

            ps.print("<div id=\"d" + index + "\"");
            if (wide) {
                ps.print(" style=\"display: none\"");
            }
            ps.println(">");
            printClass(classDoc, 4);
            ps.println();
            ps.print("</div>");
            index++;
        }
        ps.println("</pre></body></html>");
    }

    private void getDocuments(RootDoc root) {
        this.classes = root.classes();
        Arrays.sort(classes, new ToStringComparator<ClassDoc>());
        this.packages = root.specifiedPackages();
        Arrays.sort(packages, new ToStringComparator<PackageDoc>());
    }

    private void printClass(ClassDoc classDoc, int indent) {
        String name = classDoc.toString();
        indent(indent);
        ps.print(formatModifiers(classDoc));
        ps.print(removePackage(name));

        Type superType = classDoc.superclassType();
        if (superType != null && !superType.toString().equals("java.lang.Object")) {
            ps.print(color(" extends ", "blue"));
            ps.print(formatType(superType.toString()));
        }

        Type[] types = classDoc.interfaceTypes();
        if (types.length > 0) {
            ps.print(color(" implements ", "blue"));
            boolean first = true;
            for (Type type : types) {
                if (!first) {
                    ps.print(", ");
                }
                first = false;
                ps.print(formatType(type.toString()));
            }
        }
        ps.println();

        for (FieldDoc fieldDoc : (FieldDoc[]) sort(classDoc.fields())) {
            printField(fieldDoc);
        }

        for (ConstructorDoc constructor : (ConstructorDoc[]) sort(classDoc.constructors())) {
            printMethod(null, constructor, constructor.parameters(), indent + 4);
        }

        for (MethodDoc method : (MethodDoc[]) sort(classDoc.methods())) {
            printMethod(method.returnType().toString(), method, method.parameters(), indent + 4);
        }

        for (ClassDoc innerCd : classDoc.innerClasses()) {
            printClass(innerCd, indent + 4);
        }
    }

    private ClassDoc firstClassInPackage(String packageName) {
        for (ClassDoc cd : classes) {
            if (cd.containingPackage().name().equals(packageName)) {
                return cd;
            }
        }
        throw new IllegalArgumentException();
    }

    private int longestClassNameWithComment() {
        int len = 0;
        for (ClassDoc cd : classes) {
            if (cd.toString().length() > len && cd.commentText().trim().length() > 0) {
                len = cd.toString().length();
            }
        }
        return len;
    }

    private String comment(String string, int inset) {
        string = string.trim();
        int i = string.indexOf('.');
        if (i != -1) {
            string = string.substring(0, i + 1);
        }
        StringBuffer sb = new StringBuffer(string);
        remove(sb, "\r");
        remove(sb, "\n");
        remove(sb, "\t");
        remove(sb, "  ");

        if (sb.length() == 0) {
            return "";
        }
        if (inset > 0) {
            for (int y = 0; y < inset; y++) {
                sb.insert(0, ' ');
            }
        }
        return "<i>" + color(sb.toString(), "gray") + "</i>";
    }

    public void printField(FieldDoc fieldDoc) {
        ps.print("        ");
        ps.print(formatModifiers(fieldDoc));
        ps.print(formatType(fieldDoc.type().toString()) + ' ' + fieldDoc.name());
        ps.println(comment(fieldDoc.commentText(), 4));
    }

    public void printMethod(String returnType, ExecutableMemberDoc memberDoc, Parameter[] params, int indent) {
        indent(indent);

        ps.print(formatModifiers(memberDoc));
        if (returnType != null) {
            ps.print(formatType(returnType) + ' ');
        }

        ps.print(memberDoc.name() + '(');

        boolean first = true;
        for (Parameter parameter : params) {
            if (!first) {
                ps.print(", ");
            }
            first = false;
            ps.print(formatType(parameter.type().toString()) + ' ' + parameter.name());
        }

        ps.print(")");

        Type[] exceptions = memberDoc.thrownExceptionTypes();
        if (exceptions != null && exceptions.length > 0) {
            Arrays.sort(exceptions);
            ps.print(color(" throws ", "blue"));

            first = true;
            for (Type type : exceptions) {
                if (!first) {
                    ps.print(", ");
                }
                first = false;
                ps.print(formatType(type.toString()));
            }

        }
        ps.print(' ');
        ps.print(comment(memberDoc.commentText(), 5));

        ps.println();
    }

    private void remove(StringBuffer sb, String value) {
        int x;
        while ((x = sb.indexOf(value)) != -1) {
            sb.delete(x, x + 1);
        }
    }

    private String formatType(String type) {
        String shortType = removePackage(type);
        type = removeArray(type);

        for (ClassDoc cd : classes) {
            if (cd.toString().equals(type)) {
                return "<a href=\"#" + type + "\">" + shortType + "</a>";
            }
        }
        return color(shortType, "purple");
    }

    private String removeArray(String type) {
        if (type.endsWith("[]")) {
            type = type.substring(0, type.length() - 2);
        }
        return type;
    }

    private Object[] sort(Object[] array) {
        Arrays.sort(array);
        return array;
    }

    private String removePackage(String string) {
        int i = string.lastIndexOf('.');
        if (i != -1) {
            string = string.substring(i + 1);
        }
        return string;
    }

    public String formatModifiers(ProgramElementDoc doc) {
        StringBuffer sb = new StringBuffer(32);
        if (doc.isPublic()) {
            sb.append("public ");
        }
        if (doc.isPrivate()) {
            sb.append("private ");
        }
        if (doc.isProtected()) {
            sb.append("protected ");
        }
        if (doc.isStatic()) {
            sb.append("static ");
        }
        if (doc.isInterface()) {
            sb.append("interface ");
        }
        if (doc.isFinal()) {
            sb.append("final ");
        }
        if (doc.isClass()) {
            sb.append("class ");
        }
        return (sb.length() == 0) ? "" : color(sb.toString(), "blue");
    }

    public String color(String string, String color) {
        return "<font color=" + color + ">" + string + "</font>";
    }

    public void indent(int spaces) {
        for (int x = 0; x < spaces; x++) {
            ps.print(' ');
        }
    }

    private class ToStringComparator<T> implements Comparator<T> {
        public int compare(T o, T o1) {
            return (o.toString().compareTo(o1.toString()));
        }
    }
}
