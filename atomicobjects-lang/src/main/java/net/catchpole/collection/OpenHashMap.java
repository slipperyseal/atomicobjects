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

import net.catchpole.lang.Maths;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An optimized Hashed Map implementation.
 * <p/>
 * <p>This Hashmap does not allow nulls to be used as keys or values.
 * <p/>
 * <p>It uses single open hashing arrays sized to binary powers (256, 512 etc) rather
 * than those divisable by prime numbers.  This allows the hash offset calculation to be
 * a simple binary masking operation.
 */
public class OpenHashMap<K, V> implements Map<K, V>, Externalizable {
    private boolean referenceCompare = false;
    private K[] keys;
    private V[] values;

    // total number of entries in this table
    private int size;
    // number of bits for the value table (eg. 8 bits = 256 entries)
    private int bits;
    // the number of bits in each sweep zone.
    private int sweepbits;
    // the size of a sweep (2 to the power of sweepbits)
    private int sweep;
    // the sweepmask used to create sweep zone offsets
    private int sweepmask;

    public OpenHashMap() {
        this(256);
    }

    public OpenHashMap(int size) {
        resize(Maths.bitsRequired(size < 256 ? 256 : size));
    }

    public OpenHashMap(int size, boolean referenceCompare) {
        this(size);
        this.referenceCompare = referenceCompare;
    }

    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException(this.getClass().getName() + " key");
        }

        for (; ;) {
            int off = getBucketOffset(key);
            int end = off + sweep;
            for (; off < end; off++) {
                K searchKey = keys[off];
                if (searchKey == null) {
                    // insert
                    keys[off] = key;
                    size++;

                    V previous = values[off];
                    values[off] = value;
                    return previous;
                } else if (compare(searchKey, key)) {
                    // replace
                    V previous = values[off];
                    values[off] = value;
                    return previous;
                }
            }
            resize(this.bits + 1);
        }
    }

    public V get(Object key) {
        int off = getBucketOffset(key);
        int end = sweep + off;
        for (; off < end; off++) {
            if (keys[off] != null && compare(keys[off], key)) {
                return values[off];
            }
        }
        return null;
    }

    public V remove(Object key) {
        int off = getBucketOffset(key);
        int end = sweep + off;
        for (; off < end; off++) {
            if (keys[off] != null && compare(keys[off], key)) {
                keys[off] = null;
                V previous = values[off];
                values[off] = null;
                size--;
                return previous;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        for (V v : values) {
            if (v != null && compare(v, value)) {
                return true;
            }
        }
        return false;
    }

    public void clear() {
        Arrays.fill(keys, null);
        Arrays.fill(values, null);
        size = 0;
    }

    public Set<K> keySet() {
        Set<K> set = new HashSet<K>();
        for (K key : keys) {
            if (key != null) {
                set.add(key);
            }
        }
        return set;
    }

    public Collection<V> values() {
        Collection<V> list = new ArrayList<V>();
        for (V value : values) {
            if (value != null) {
                list.add(value);
            }
        }
        return list;
    }

    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();
        for (K key : keys) {
            if (key != null) {
                set.add(new MapEntry<K, V>(this, key));
            }
        }
        return set;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // remember the number of bits
        out.writeInt(this.bits);
        // remember the total number of entries
        out.writeInt(this.size);
        // write all entries
        for (int x = 0; x < this.keys.length; x++) {
            if (keys[x] != null) {
                out.writeObject(keys[x]);
                out.writeObject(values[x]);
            }
        }
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        // resize to old bit size
        int bitSize = in.readInt();
        if (bitSize != bits) {
            resize(bitSize);
        }
        // read all entries
        int size = in.readInt();
        for (int x = 0; x < size; x++) {
            this.put((K) in.readObject(), (V) in.readObject());
        }
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + this.size;
    }

    private void resize(int bits) {
        this.bits = bits;
        this.sweepbits = bits / 4;
        this.sweep = Maths.powerOf(2, sweepbits) * 4;
        this.sweepmask = Maths.bitMask(bits - this.sweepbits) << sweepbits;

        // remember old values so we can recreate the entries
        K[] existingKeys = this.keys;
        V[] existingValues = this.values;

        // create the arrays
        this.values = (V[]) new Object[Maths.powerOf(2, bits) + sweep];
        this.keys = (K[]) new Object[this.values.length];
        this.size = 0;

        // re-add the previous entries if resizing
        if (existingKeys != null) {
            for (int x = 0; x < existingKeys.length; x++) {
                if (existingKeys[x] != null) {
                    put(existingKeys[x], existingValues[x]);
                }
            }
        }
        //Core.getTrace().info(this.getClass().getName() + " resize(" + bits + ")\t" + keys.length + '/' + size + "\tbits: " + bits + "\tsweepbits:" + sweepbits + " sweep: " + sweep);
    }

    private int getBucketOffset(Object key) {
        return (key.hashCode() << this.sweepbits) & this.sweepmask;
    }

    public Iterator<K> getKeyIterator() {
        return new KeyIterator();
    }

    class KeyIterator implements Iterator<K> {
        private K[] list;
        private int index = 0;
        private K last;

        public KeyIterator() {
            list = (K[]) new Object[size];
            int index = 0;
            for (K l : keys) {
                if (l != null) {
                    list[index++] = l;
                }
            }
        }

        public boolean hasNext() {
            return index != list.length;
        }

        public K next() {
            if (index != list.length) {
                return last = list[index++];
            } else {
                throw new IllegalStateException();
            }
        }

        public void remove() {
            if (last == null) {
                throw new IllegalStateException();
            }
            OpenHashMap.this.remove(last);
        }
    }

    private boolean compare(Object v1, Object v2) {
        return referenceCompare ? v1 == v2 : v1.equals(v2);
    }
}
