package net.catchpole.lang;

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

import net.catchpole.collection.SortedArray;

import java.util.Comparator;
import java.util.Iterator;

public class TypeInheritance implements Iterable<Class> {
    private final SortedArray<Class> types = new SortedArray<Class>(new Comparator<Class>() {
        public int compare(Class o1, Class o2) {
            return o1.getName().compareTo(o2.getName());
        }
    });

    public TypeInheritance(Class clazz) {
        if (clazz.isInterface()) {
            types.addUnique(clazz);
            for (Class interfaceClass : clazz.getInterfaces()) {
                types.addUnique(interfaceClass);
            }
        } else {
            do {
                // ignore java.lang.Object
                if (clazz.getSuperclass() != null) {
                    types.addUnique(clazz);

                    for (Class interfaceClass : clazz.getInterfaces()) {
                        types.addUnique(interfaceClass);
                    }
                }
            } while ((clazz = clazz.getSuperclass()) != null);
        }
    }

    public Iterator<Class> iterator() {
        return types.iterator();
    }

    @Override
    public int hashCode() {
        return types.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return types.equals(obj);
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + types;
    }
}
