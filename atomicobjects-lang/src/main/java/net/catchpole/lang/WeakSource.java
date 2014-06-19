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

import java.lang.ref.WeakReference;

/**
 * A Source which uses WeakReferences to allow the Object to be collected when no references to it exist.
 */

public class WeakSource<T> implements Source<T> {
    private final Source<T> source;
    private WeakReference<T> weakReference;

    public WeakSource(Source<T> source) {
        this.source = source;
    }

    public synchronized T get() {
        T value = (weakReference == null ? null : this.weakReference.get());
        if (value == null) {
            value = source.get();
            if (value != null) {
                this.weakReference = new WeakReference<T>(value);
            }
        }
        return value;
    }
}
