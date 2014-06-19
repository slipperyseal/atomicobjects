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

import java.util.Map;

public class MapEntry<K, V> implements Map.Entry<K, V> {
    private final Map<K, V> map;
    private final K key;

    public MapEntry(Map<K, V> map, K key) {
        this.map = map;
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return map.get(key);
    }

    public V setValue(V value) {
        return map.put(key, value);
    }
}
