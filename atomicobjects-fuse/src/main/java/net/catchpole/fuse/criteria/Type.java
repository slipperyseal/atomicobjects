package net.catchpole.fuse.criteria;

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

public class Type implements Comparable<Type> {
    private final String name;
    private final Class clazz;
    private final String sortKey;

    public Type(Class clazz) {
        this(null, clazz);
    }

    public Type(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
        this.sortKey = name == null ? clazz.getName() : clazz.getName() + '/' + name;
    }

    public String getName() {
        return name;
    }

    public Class getClassValue() {
        return clazz;
    }

    public boolean isAssignableFrom(Type type) {
        if (name != null ? !name.equals(type.name) : type.name != null) {
            return false;
        }
        return (type.clazz.isAssignableFrom(clazz));
    }

    public boolean isAssignableTo(Type type) {
        if (name != null ? !name.equals(type.name) : type.name != null) {
            return false;
        }
        return (clazz.isAssignableFrom(type.clazz));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return sortKey.equals(((Type) o).sortKey);
    }

    @Override
    public int hashCode() {
        return sortKey.hashCode();
    }

    public int compareTo(Type type) {
        return sortKey.compareTo(type.sortKey);
    }

    public String toString() {
        return sortKey;
    }
}
