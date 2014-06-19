package net.catchpole.collection;

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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class OpenHashMapKeySet<K, V> implements Set<Map.Entry<K, V>> {
    private final OpenHashMap<K, V> map;

    public OpenHashMapKeySet(OpenHashMap<K, V> map) {
        this.map = map;
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    public Iterator<Map.Entry<K, V>> iterator() {
        return null; //todo: implemenet
    }

    public Object[] toArray() {
        return new Object[0];  //todo: implement
    }

    public <T> T[] toArray(T[] a) {
        return null;  //todo:  implement
    }

    public boolean add(Map.Entry<K, V> e) {
        return map.put(e.getKey(), e.getValue()) == null;
    }

    public boolean remove(Object o) {
        return map.remove(o) != null;
    }

    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!map.containsKey(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends Map.Entry<K, V>> c) {
        boolean change = false;
        for (Map.Entry<K, V> entry : c) {
            V previous = map.put(entry.getKey(), entry.getValue());
            if (previous != null && !previous.equals(entry.getValue())) {
                change = true;
            }
        }
        return change;
    }

    public boolean retainAll(Collection<?> c) {
        boolean change = false;
        for (K key : map.keySet()) {
            if (!c.contains(key)) {
                if (map.remove(key) != null) {
                    change = true;
                }
            }
        }
        return change;
    }

    public boolean removeAll(Collection<?> c) {
        boolean change = false;
        for (Object o : c) {
            map.remove(o);
        }
        return change;
    }

    public void clear() {
        map.clear();
    }
}
