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

/**
 * Distributes hashing across an array of LongHashmaps providing
 * mutiple smaller arrays rather one large, synchronized array.
 * <p/>
 * <p>Distributes entries across tables by selecting lower bits of the keys.
 */
public class LongTableDistributer<T> implements LongTable<T> {
    private final LongTable<T>[] tables;
    private final int mask;

    public LongTableDistributer(int distributeBits, int hashBits) {
        this.tables = (LongTable<T>[]) new LongHashMap[(int) Math.pow(2, distributeBits)];
        for (int x = 0; x < tables.length; x++) {
            this.tables[x] = new LongHashMap<T>(hashBits);
        }
        this.mask = Maths.bitMask(distributeBits);
    }

    public void put(long key, T object) {
        this.tables[(int) key & mask].put(key, object);
    }

    public T get(long key) {
        return this.tables[(int) key & mask].get(key);
    }

    public int size() {
        int size = 0;
        for (LongTable table : tables) {
            size += table.size();
        }
        return size;
    }

    public void remove(long key) {
        this.tables[(int) key & mask].remove(key);
    }

    public LongIterator getKeyIterator() {
        return new ChainedLongIterator(tables);
    }
}
