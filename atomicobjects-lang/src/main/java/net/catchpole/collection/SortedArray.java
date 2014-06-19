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

import net.catchpole.io.Arrays;
import net.catchpole.lang.ArrayIterator;

import java.util.Comparator;
import java.util.Iterator;

public class SortedArray<T> implements Iterable<T> {
    private T[] array = (T[]) new Object[4];
    private int len;
    private final Comparator comparator;

    public SortedArray() {
        comparator = null;
    }

    public SortedArray(final Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public int size() {
        return len;
    }

    public void add(final T object) {
        checkResize();
        Comparable comparable = comparator != null ? null : (Comparable) object;
        for (int x = 0; x < len; x++) {
            if ((comparator != null ?
                    comparator.compare(object, array[x]) :
                    comparable.compareTo(array[x])) <= 0) {
                for (int y = array.length - 1; y != x; y--) {
                    array[y] = array[y - 1];
                }
                array[x] = object;
                len++;
                return;
            }
        }
        array[len++] = object;
    }

    public boolean addUnique(final T object) {
        Comparable comparable = comparator != null ? null : (Comparable) object;
        for (int x = 0; x < len; x++) {
            int compare = comparator != null ?
                    comparator.compare(object, array[x]) :
                    comparable.compareTo(array[x]);
            if (compare == 0 && object.equals(array[x])) {
                array[x] = object;
                return true;
            }
            if (compare <= 0) {
                checkResize();
                for (int y = array.length - 1; y != x; y--) {
                    array[y] = array[y - 1];
                }
                array[x] = object;
                len++;
                return false;
            }
        }
        checkResize();
        array[len++] = object;
        return false;
    }

    public boolean contains(final T object) {
        for (int x = 0; x < len; x++) {
            if (array[x].equals(object)) {
                return true;
            }
        }
        return false;
    }

    private void checkResize() {
        if (len == array.length) {
            array = Arrays.resizeArray(array, array.length < 64 ?
                    array.length + 8 : array.length + (array.length / 4));
        }
    }

    public T elementAt(final int x) {
        if (x >= len) {
            throw new IndexOutOfBoundsException();
        }
        return array[x];
    }

    public void trim() {
        if (len != array.length) {
            array = Arrays.resizeArray(array, len);
        }
    }

    public Iterator<T> iterator() {
        return new ArrayIterator<T>(array, len);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        for (T object : this) {
            sb.append(' ');
            sb.append(object);
        }
        sb.append(' ');
        sb.append('}');
        return sb.toString();
    }
}
