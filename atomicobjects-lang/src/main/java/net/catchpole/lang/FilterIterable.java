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

public abstract class FilterIterable<T> implements Iterable<T> {
    private final Iterable<T> iterable;

    public FilterIterable(Iterable<T> iterable) {
        this.iterable = iterable;
    }

    public Iterator<T> iterator() {
        return new FilterIterator<T>(iterable.iterator()) {
            @Override
            public boolean include(T type) {
                return FilterIterable.this.include(type);
            }
        };
    }

    public abstract boolean include(T type);
}
