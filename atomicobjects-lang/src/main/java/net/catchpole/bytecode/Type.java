package net.catchpole.bytecode;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Java Type being a Class file, a primative type or an array of primative or Class types.
 * This class can also be used to encode, decode or resolve Java Type Signatures as used within class files.
 */

public class Type {
    private static final Map<Class, Character> codes = new HashMap<Class, Character>();

    static {
        codes.put(Boolean.TYPE, 'Z');
        codes.put(Byte.TYPE, 'B');
        codes.put(Character.TYPE, 'C');
        codes.put(Short.TYPE, 'S');
        codes.put(Integer.TYPE, 'I');
        codes.put(Long.TYPE, 'J');
        codes.put(Float.TYPE, 'F');
        codes.put(Double.TYPE, 'D');
        codes.put(Void.TYPE, 'V');
    }

    private char typeCode;
    private String className;
    private int arrayDimensions;
    private String typeSignature;

    public Type(String className) {
        this.className = className;
        this.typeCode = 'L';
    }

    public Type(String className, int arrayDimensions) {
        this.className = className;
        this.typeCode = 'L';
        this.arrayDimensions = arrayDimensions;
    }

    public Type(Class type) {
        Character typeCode = codes.get(type);
        if (typeCode != null) {
            this.typeCode = typeCode;
        } else if (type.isArray()) {
            Class finalType = findFinalComponentType(type);
            typeCode = codes.get(finalType);
            if (typeCode == null) {
                this.typeCode = 'L';
                this.className = finalType.getName();
            } else {
                this.typeCode = typeCode;
            }
            this.arrayDimensions = findArrayDimensions(type);
        } else {
            this.typeCode = 'L';
            this.className = type.getName();
        }
    }

    public String getTypeSignature() {
        if (typeSignature == null) {
            StringBuilder sb = new StringBuilder(50);
            for (int x = 0; x < this.arrayDimensions; x++) {
                sb.append('[');
            }
            sb.append(getTypeCode());
            if (className != null) {
                sb.append(className.replace('.', '/'));
                sb.append(';');
            }
            typeSignature = sb.toString();
        }
        return typeSignature;
    }

    public String toString() {
        return getTypeSignature();
    }

    public char getTypeCode() {
        return typeCode;
    }

    public String getClassName() {
        return className;
    }

    public int getArrayDimensions() {
        return arrayDimensions;
    }

    private static int findArrayDimensions(Class type) {
        int depth = 0;
        while (type.isArray()) {
            depth++;
            type = type.getComponentType();
        }
        return depth;
    }

    private static Class findFinalComponentType(Class type) {
        while (type.getComponentType().isArray()) {
            type = type.getComponentType();
        }
        return type.getComponentType();
    }

}
