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

/**
 * Fast open-hashing implementation of a long key to Object HashMap.
 * <p/>
 * <p>This implementation assumes that long values are generally uniform in their
 * lower bit ranges, regardless of whether they are scattered or within a certain range.
 * <p/>
 * <p>The key reference zero is reserved as a null value.
 * <p/>
 * <p>This object maintains single arrays for keys and object references.
 * The index into the arrays is determined by masking a portion of the
 * key and shifting it to provide a series of small buckets within the
 * array.  To insert an entry the a sweep is searched until an empty
 * key space is found.  A sweep is 4 times the length of a bucket, to
 * reduce the need to rehash.
 * <p/>
 * <p>If no key space is found within a sweep, the table size is doubled.
 * <p/>
 * <p>As the key for this table is a primitive long, bucket searching, even for large
 * bucket sizes, would be much faster than Object based comparator tests as found in
 * normal HashMaps.
 * <p/>
 * <p>While performance is high, the slowest situation is where lookup occurs for entries
 * that do not exist, as an entire sweep area must be searched.
 */
public class LongHashMap<T> implements LongTable<T>, Externalizable {
    private long[] keys;
    private T[] objects;

    // total number of entries in this table
    private int size;
    // number of bits for the object table (eg. 8 bits = 256 entries)
    private int bits;
    // the number of bits in each sweep zone.
    private int sweepbits;
    // the size of a sweep (2 to the power of sweepbits)
    private int sweep;
    // the sweepmask used to create sweep zone offsets
    private int sweepmask;

    public LongHashMap() {
        this(8);
    }

    public LongHashMap(int bits) {
        resize(bits < 8 ? 8 : bits);
    }

    public synchronized void put(long key, T object) {
        if (key == 0) {
            throw new IllegalArgumentException(Long.toString(key));
        }

        for (; ;) {
            int off = getBucketOffset(key);
            int end = off + sweep;
            for (; off < end; off++) {
                long searchKey = keys[off];
                if (searchKey == 0) {
                    // insert
                    keys[off] = key;
                    objects[off] = object;
                    size++;
                    return;
                } else if (searchKey == key) {
                    // replace
                    objects[off] = object;
                    return;
                }
            }
            resize(this.bits + 1);
        }
    }

    public synchronized T get(long key) {
        int off = getBucketOffset(key);
        int end = sweep + off;
        for (; off < end; off++) {
            if (keys[off] == key) {
                return objects[off];
            }
        }
        return null;
    }

    public synchronized void remove(long key) {
        int off = getBucketOffset(key);
        int end = sweep + off;
        for (; off < end; off++) {
            if (keys[off] == key) {
                keys[off] = 0;
                objects[off] = null;
                size--;
                return;
            }
        }
    }

    public int size() {
        return size;
    }

    private void resize(int bits) {
        this.bits = bits;
        this.sweepbits = bits / 4;
        this.sweep = Maths.powerOf(2, sweepbits) * 4;
        this.sweepmask = Maths.bitMask(bits - this.sweepbits) << sweepbits;

        // Core.getTrace().info("bits: " + bits + " sweepbits:" + sweepbits + " sweep: " + sweep + " sweepmask:" + sweepmask);

        // remember old values so we can recreate the entries
        long[] existingKeys = this.keys;
        T[] existingObjects = this.objects;

        // create the arrays
        this.objects = (T[]) new Object[Maths.powerOf(2, bits) + sweep];
        this.keys = new long[this.objects.length];
        this.size = 0;

        // re-add the previous entries if resizing
        if (existingKeys != null) {
            for (int x = 0; x < existingKeys.length; x++) {
                if (existingKeys[x] != 0) {
                    put(existingKeys[x], existingObjects[x]);
                }
            }
        }
    }

    private int getBucketOffset(long key) {
        return ((int) key << this.sweepbits) & this.sweepmask;
    }

    public synchronized String toString() {
        return this.getClass().getSimpleName() + ' ' + this.bits + ':' + this.size;
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        // remember the number if bits
        out.writeInt(this.bits);
        // remember the total number of entries
        out.writeInt(this.size);
        // write all entries
        for (int x = 0; x < this.keys.length; x++) {
            if (keys[x] != 0) {
                out.writeLong(keys[x]);
                out.writeObject(objects[x]);
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
            this.put(in.readLong(), (T) in.readObject());
        }
    }

    public LongIterator getKeyIterator() {
        return new KeyIterator();
    }

    class KeyIterator implements LongIterator {
        long[] list;
        int index = 0;

        public KeyIterator() {
            list = new long[size];
            int index = 0;
            for (long l : keys) {
                if (l != 0) {
                    list[index++] = l;
                }
            }
        }

        public boolean hasNext() {
            return index != list.length;
        }

        public long next() {
            if (index != list.length) {
                return list[index++];
            } else {
                throw new IllegalStateException();
            }
        }
    }
}
