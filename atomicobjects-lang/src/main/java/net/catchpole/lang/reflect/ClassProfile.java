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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ClassProfile {
    private static final Method[] baseObjectMethods = Object.class.getMethods();

    private final Class clazz;

    public ClassProfile(Class clazz) {
        this.clazz = clazz;
    }

    public int getArrayDimensions() {
        int d = 0;
        Class clazz = this.clazz;
        while (clazz.isArray()) {
            d++;
            clazz = clazz.getComponentType();
        }
        return d;
    }


    public Class seekArrayComponentType() {
        Class clazz = this.clazz;
        while (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return clazz;
    }

    /**
     * Returns the definition of this type that is Java source code compatible.
     * eg.  String[][]
     */
    public String getSourceDefinition() {
        StringBuilder string = new StringBuilder();

        Class actual = clazz.isArray() ? seekArrayComponentType() : clazz;
        string.append(actual.isPrimitive() ? Reflection.complexToPrimativeName(actual) :
                actual.getPackage().getName().equals("java.lang") ? actual.getSimpleName() : actual.getName());

        int d = getArrayDimensions();
        for (int x = 0; x < d; x++) {
            string.append("[]");
        }
        return string.toString();
    }

    public String getSourceBoxing(String variableName) {
        if (clazz.isPrimitive()) {
            Class wrapper = Reflection.primativeToWrapperClass(clazz);
            return "new " + wrapper.getSimpleName() + "(" + variableName + ")";
        } else {
            return variableName;
        }
    }

    public String getSourceUnboxingPre() {
        if (clazz.isPrimitive()) {
            return "((" + Reflection.primativeToWrapperClass(clazz).getSimpleName() + ")";
        } else {
            return "";
        }
    }

    public String getSourceUnboxingPost() {
        if (clazz.isPrimitive()) {
            return ")." + clazz.getSimpleName() + "Value()";
        } else {
            return "";
        }
    }

    /**
     * Returns all methods for a class except those which are inheritied from
     * the Object class such as hashcode, toString etc.
     */
    public Method[] getNonObjectMethods() {
        List<Method> list = new ArrayList<Method>();
        for (Method method : clazz.getMethods()) {
            if (!Reflection.methodsMatch(method, baseObjectMethods)) {
                list.add(method);
            }
        }
        return list.toArray(new Method[list.size()]);
    }
}
