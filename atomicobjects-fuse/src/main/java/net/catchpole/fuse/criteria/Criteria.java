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

import net.catchpole.collection.SortedArray;

import java.util.Iterator;


public class Criteria implements Iterable<Type> {
    private final SortedArray<Type> types = new SortedArray<Type>();

    public Criteria(Class[] clazzArray) {
        for (Class clazz : clazzArray) {
            types.addUnique(new Type(clazz));
        }
    }

    public Criteria(Class clazz) {
        types.addUnique(new Type(clazz));
    }

    public Criteria(String key, Class clazz) {
        types.addUnique(new Type(key, clazz));
    }

    public void add(String key, Class clazz) {
        types.addUnique(new Type(key, clazz));
    }

    public Iterator<Type> iterator() {
        return types.iterator();
    }

    public boolean isAssignableFrom(Criteria criteria) {
        if (types.size() != criteria.types.size()) {
            return false;
        }
        Iterator<Type> iterator = criteria.types.iterator();
        for (Type type : types) {
            if (!type.isAssignableFrom(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    public boolean isAssignableTo(Criteria criteria) {
        if (types.size() != criteria.types.size()) {
            return false;
        }
        Iterator<Type> iterator = criteria.types.iterator();
        for (Type type : types) {
            if (!type.isAssignableTo(iterator.next())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Criteria criteria = (Criteria) o;

        if (types != null ? !types.equals(criteria.types) : criteria.types != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return types != null ? types.hashCode() : 0;
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + types;
    }
}
