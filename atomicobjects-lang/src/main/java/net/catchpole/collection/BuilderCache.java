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

import net.catchpole.lang.KeyedSource;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BuilderCache<K, V> implements Iterable<K> {
    private final Map<K, V> map = new HashMap<K, V>();
    private final KeyedSource<K, V> keyedSource;

    public BuilderCache(KeyedSource<K, V> keyedSource) {
        this.keyedSource = keyedSource;
    }

    public V get(K key) {
        V value = map.get(key);
        if (value == null) {
            value = keyedSource.get(key);
            map.put(key, value);
        }
        return value;
    }

    public Iterator<K> iterator() {
        return map.keySet().iterator();
    }
}
