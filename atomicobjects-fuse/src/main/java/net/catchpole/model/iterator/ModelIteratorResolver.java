package net.catchpole.model.iterator;

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

import net.catchpole.model.Model;

import java.util.Iterator;

public abstract class ModelIteratorResolver<T> implements Iterator<Model> {
    private Iterator<T> iterator;

    public ModelIteratorResolver() {
    }

    public ModelIteratorResolver(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator != null && iterator.hasNext();
    }

    public Model next() {
        return iterator == null ? null : resolve(iterator.next());
    }

    public void remove() {
        if (iterator != null) {
            iterator.remove();
        }
    }

    public abstract Model resolve(T object);
}
