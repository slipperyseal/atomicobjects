package net.catchpole.model.search;

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

public class SingleValue<T> {
    private final Model model;

    public SingleValue(Model model) {
        this.model = model;
    }

    public T value() {
        Iterator<T> i = model.getValues();
        if (i != null) {
            if (i.hasNext()) {
                T value = i.next();
                if (i.hasNext()) {
                    throw new IllegalArgumentException("Not a single value");
                }
                return value;
            }
        }
        throw new IllegalArgumentException("No value");
    }

    public T value(T defaultValue) {
        Iterator<T> i = model.getValues();
        if (i != null) {
            if (i.hasNext()) {
                T value = i.next();
                if (i.hasNext()) {
                    throw new IllegalArgumentException("Not a single value");
                }
                return value;
            }
        }
        return defaultValue;
    }
}
