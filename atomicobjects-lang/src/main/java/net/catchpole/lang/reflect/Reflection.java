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

import net.catchpole.lang.Throw;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class Reflection {
    // lookup table to determine if a wrapper or primitive type class can be concidered primative
    private static Map<String, Class> truePrimativeTypes = new HashMap<String, Class>();
    // lookup table to determine if a wrapper or primitive type class can be concidered primative
    private static Map<String, Class> allPrimativeTypes = new HashMap<String, Class>();
    private static Map<Class, Class> primativeWrapperTypes = new HashMap<Class, Class>();

    static {
        allPrimativeTypes.put(Byte.class.getName(), Byte.TYPE);
        allPrimativeTypes.put(Integer.class.getName(), Integer.TYPE);
        allPrimativeTypes.put(Short.class.getName(), Short.TYPE);
        allPrimativeTypes.put(Long.class.getName(), Long.TYPE);
        allPrimativeTypes.put(Character.class.getName(), Character.TYPE);
        allPrimativeTypes.put(Boolean.class.getName(), Boolean.TYPE);
        allPrimativeTypes.put(Double.class.getName(), Double.TYPE);
        allPrimativeTypes.put(Float.class.getName(), Float.TYPE);
        allPrimativeTypes.put(Void.class.getName(), Void.TYPE);

        allPrimativeTypes.put(Byte.TYPE.getName(), Byte.TYPE);
        allPrimativeTypes.put(Integer.TYPE.getName(), Integer.TYPE);
        allPrimativeTypes.put(Long.TYPE.getName(), Long.TYPE);
        allPrimativeTypes.put(Short.TYPE.getName(), Short.TYPE);
        allPrimativeTypes.put(Character.TYPE.getName(), Character.TYPE);
        allPrimativeTypes.put(Boolean.TYPE.getName(), Boolean.TYPE);
        allPrimativeTypes.put(Double.TYPE.getName(), Double.TYPE);
        allPrimativeTypes.put(Float.TYPE.getName(), Float.TYPE);
        allPrimativeTypes.put(Void.TYPE.getName(), Void.TYPE);

        truePrimativeTypes.put(Byte.class.getName(), Byte.TYPE);
        truePrimativeTypes.put(Integer.class.getName(), Integer.TYPE);
        truePrimativeTypes.put(Long.class.getName(), Long.TYPE);
        truePrimativeTypes.put(Short.class.getName(), Short.TYPE);
        truePrimativeTypes.put(Character.class.getName(), Character.TYPE);
        truePrimativeTypes.put(Boolean.class.getName(), Boolean.TYPE);
        truePrimativeTypes.put(Double.class.getName(), Double.TYPE);
        truePrimativeTypes.put(Float.class.getName(), Float.TYPE);
        truePrimativeTypes.put(Void.class.getName(), Void.TYPE);

        primativeWrapperTypes.put(Byte.TYPE, Byte.class);
        primativeWrapperTypes.put(Integer.TYPE, Integer.class);
        primativeWrapperTypes.put(Long.TYPE, Long.class);
        primativeWrapperTypes.put(Short.TYPE, Short.class);
        primativeWrapperTypes.put(Character.TYPE, Character.class);
        primativeWrapperTypes.put(Boolean.TYPE, Boolean.class);
        primativeWrapperTypes.put(Double.TYPE, Double.class);
        primativeWrapperTypes.put(Float.TYPE, Float.class);
        primativeWrapperTypes.put(Void.TYPE, Void.class);
    }

    private Reflection() {
    }

    public static boolean isPrimative(String className) {
        return allPrimativeTypes.containsKey(className);
    }

    public static boolean methodsMatch(Method testMethod, Method[] methods) {
        for (Method method : methods) {
            if (methodsMatch(testMethod, method)) {
                return true;
            }
        }
        return false;
    }

    public static boolean methodsMatch(Method method1, Method method2) {
        // read name
        if (!method1.getName().equals(method2.getName())) {
            return false;
        }
        // read return types
        if (!method1.getReturnType().equals(method2.getReturnType())) {
            return false;
        }
        // read params
        if (!Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes())) {
            return false;
        }
        return true;
    }

    public static boolean compatibleMethod(Method method, Class returnType, Class[] parameterTypes) {
        if (!compatibleMethod(method, parameterTypes)) {
            return false;
        }
        if (returnType != null && !method.getReturnType().isAssignableFrom(returnType)) {
            return false;
        }
        return true;
    }

    public static boolean compatibleMethod(Method method, Class[] parameterTypes) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return false;
        }

        // if no parameter types ensure method has non either
        if (parameterTypes == null || parameterTypes.length == 0) {
            return (method.getParameterTypes().length == 0);
        } else {
            return allAssignableFrom(method.getParameterTypes(), parameterTypes);
        }
    }

    public static Class[] toClassArray(Object[] objects) {
        if (objects == null) {
            return new Class[0];
        }
        Class[] classes = new Class[objects.length];
        for (int x = 0; x < classes.length; x++) {
            if (objects[x] != null) {
                classes[x] = objects[x].getClass();
            }
        }
        return classes;
    }

    public static Class wrapperToPrimativeClass(Class wrapperClass) {
        Class primativeClass = truePrimativeTypes.get(wrapperClass.getName());
        return (primativeClass != null ? primativeClass : wrapperClass);
    }

    public static Class primativeToWrapperClass(Class primative) {
        return primativeWrapperTypes.get(primative);
    }

    public static boolean allAssignableFrom(Class[] toClasses, Class[] fromClasses) {
        if (fromClasses == null || toClasses == null || fromClasses.length != toClasses.length) {
            return false;
        }
        for (int x = 0; x < fromClasses.length; x++) {
            if (!toClasses[x].isAssignableFrom(fromClasses[x])) {
                return false;
            }
        }
        return true;
    }

    public static Map<String, Object> getValues(Object bean) {
        HashMap<String, Object> map = new HashMap<String, Object>();

        for (Method method : new ClassProfile(bean.getClass()).getNonObjectMethods()) {
            MethodProfile methodProfile = new MethodProfile(method);

            // method must have no params
            Class[] params = method.getParameterTypes();
            if (params == null || params.length != 0) {
                continue;
            }

            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            // method must be a GET method
            if (!methodProfile.isGetMethod()) {
                continue;
            }
            String name = methodProfile.getPropertyName();

            Object value;
            try {
                value = method.invoke(bean);
            } catch (Exception e) {
                // should not happen as Invocation Target and Access should be OK
                throw new RuntimeException(method.toString(), e);
            }
            map.put(name, value);
        }
        return map;
    }

    public static String toSetMethod(String key) {
        if (key.length() < 1) {
            throw new IllegalArgumentException(key);
        }
        return "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public static String toGetMethod(String key) {
        if (key.length() < 1) {
            throw new IllegalArgumentException(key);
        }
        return "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public static String toIsMethod(String key) {
        if (key.length() < 1) {
            throw new IllegalArgumentException();
        }
        return "is" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public static Object fromString(String value) throws Exception {
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return fromString(Boolean.class, value);
        } else if (value.indexOf('.') != -1) {
            return fromString(Double.class, value);
        } else {
            return fromString(Integer.class, value);
        }
    }

    public static Object fromString(Class type, String value) throws Exception {
        // test by simple name to cover primative and wrapper types
        String simpleName = type.getSimpleName();
        if (simpleName.equalsIgnoreCase("Integer") || simpleName.equalsIgnoreCase("int")) {
            return Integer.parseInt(value);
        } else if (simpleName.equalsIgnoreCase("Long")) {
            return Long.parseLong(value);
        } else if (simpleName.equalsIgnoreCase("Double")) {
            return Double.parseDouble(value);
        } else if (simpleName.equalsIgnoreCase("Float")) {
            return Float.parseFloat(value);
        } else if (simpleName.equalsIgnoreCase("Boolean")) {
            return Boolean.valueOf(value.equals("on") ? "true" : value);
        } else if (simpleName.equalsIgnoreCase("Short")) {
            return Short.parseShort(value);
        } else if (simpleName.equalsIgnoreCase("Character")) {
            return value.charAt(0);
        } else if (simpleName.equalsIgnoreCase("Byte")) {
            return Integer.parseInt(value);
        }
        throw new IllegalArgumentException(type.getName());
    }

    public static boolean callSetMethod(Object object, String methodName, String value) {
        // todo:  refactor me, i suck - implement correct isAssignable check to determine method to call
        for (Method method : object.getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == 1) {
                    // as we dont realy know which set method to try, try all types until something works
                    try {
                        method.invoke(object, fromString(paramTypes[0], value));
                        return true;
                    } catch (Exception e) {
                    }
                }
            }
        }
        return false;
    }

    public static boolean callSetMethod(Object object, String methodName, Object value) {
        // todo:  refactor me, i suck - implement correct isAssignable check to determine method to call
        for (Method method : object.getClass().getMethods()) {
            if (method.getName().equals(methodName)) {
                Class[] paramTypes = method.getParameterTypes();
                if (paramTypes.length == 1) {
                    // as we dont realy know which set method to try, try all types until something works
                    try {
                        method.invoke(object, value);
                        return true;
                    } catch (Exception e) {
                    }
                }
            }
        }
        return false;
    }

    public static String complexToPrimativeName(Class clazz) {
        String name = clazz.getSimpleName().toLowerCase();
        if (name.equals("integer")) {
            return "int";
        }
        if (name.equals("character")) {
            return "char";
        }
        return name;
    }

    /**
     * Where:<br>
     * class ClockFaceRenderer implements Serializable, Transform<Date,BufferedImage><br>
     * <br>
     * getParameterTypes(ClockFaceRenderer.class, Transform.class);<br>
     * returns:<br>
     * <br>
     * Class[] { Date.class, BufferedImage.class }<br>
     */
    public static Class[] getParameterTypes(Class clazz, Class parametizedClass) {
        for (Type type : clazz.getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                if (parameterizedType.getRawType().equals(parametizedClass)) {
                    Type[] paramTypes = parameterizedType.getActualTypeArguments();
                    Class[] classes = new Class[paramTypes.length];
                    System.arraycopy(paramTypes, 0, classes, 0, paramTypes.length);
                    return classes;
                }
            }
        }
        throw new IllegalArgumentException();
    }

}
