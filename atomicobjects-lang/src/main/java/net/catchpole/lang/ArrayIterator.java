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

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
    private final T[] objects;
    private final int len;
    private int x = 0;

    public ArrayIterator(T[] objects) {
        this(objects, objects.length);
    }

    public ArrayIterator(T[] objects, int len) {
        this.objects = objects;
        this.len = len;
    }

    public boolean hasNext() {
        return x < len;
    }

    public T next() {
        return this.objects[x++];
    }

    public void remove() {
        this.objects[x - 1] = null;
    }
}
