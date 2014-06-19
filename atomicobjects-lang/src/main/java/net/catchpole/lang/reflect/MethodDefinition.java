package net.catchpole.lang.reflect;

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

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Externalizable form for wrapping and resolving non-static public Methods.
 * <p/>
 * Remembers only class, method name and parameter type names without storing
 * return or exception types.
 */
public class MethodDefinition implements Externalizable {
    private String className;
    private String methodName;
    private String[] types;
    private transient Method method;

    /**
     * Public constructor required for Externalization.
     */
    public MethodDefinition() {
    }

    public MethodDefinition(Method method) {
        this.className = method.getDeclaringClass().getName();
        this.methodName = method.getName();

        Class[] paramTypes = method.getParameterTypes();

        if (paramTypes == null) {
            paramTypes = new Class[0];
        }

        this.types = new String[paramTypes.length];
        for (int x = 0; x < paramTypes.length; x++) {
            this.types[x] = paramTypes[x].getName();
        }

        this.method = method;
    }

    public boolean equals(Object o) {
        if (o instanceof MethodDefinition) {
            MethodDefinition md = (MethodDefinition) o;
            return (md.className.equals(className) &&
                    md.methodName.equals(methodName) &&
                    Arrays.equals(md.types, types));
        }
        return false;
    }

    public Method getMethod() throws ClassNotFoundException, NoSuchMethodException {
        if (this.method == null) {
            this.method = Class.forName(className).getMethod(methodName, toClassArray(this.types));
        }
        return this.method;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeUTF(this.className);
        out.writeUTF(this.methodName);
        out.writeInt(this.types.length);
        for (String type : types) {
            out.writeUTF(type);
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.className = in.readUTF();
        this.methodName = in.readUTF();
        this.types = new String[in.readInt()];
        for (int x = 0; x < this.types.length; x++) {
            this.types[x] = in.readUTF();
        }
    }

    private static Class[] toClassArray(String[] types) throws ClassNotFoundException {
        Class[] classes = new Class[types.length];
        for (int x = 0; x < classes.length; x++) {
            classes[x] = Class.forName(types[x]);
        }
        return classes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(this.className);
        sb.append('.');
        sb.append(this.methodName);
        sb.append('(');

        for (int x = 0; x < types.length; x++) {
            sb.append(types[x]);
            if (x != 0) {
                sb.append(',');
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
