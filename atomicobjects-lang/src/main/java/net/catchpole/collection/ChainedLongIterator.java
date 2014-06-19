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

/**
 * A LongInterator that inturn iterates a list of LongTables.
 */
public class ChainedLongIterator implements LongIterator {
    private final LongTable[] tables;
    private LongIterator longIterator;
    private int index;

    public ChainedLongIterator(LongTable[] tables) {
        this.tables = tables;
    }

    public boolean hasNext() {
        for (; ;) {
            if (index >= tables.length) {
                return false;
            }
            if (longIterator == null) {
                longIterator = tables[index].getKeyIterator();
            }
            if (longIterator.hasNext()) {
                return true;
            } else {
                index++;
                longIterator = null;
            }
        }
    }

    public long next() {
        if (index >= tables.length) {
            throw new IllegalStateException();
        }
        if (longIterator == null) {
            longIterator = tables[index].getKeyIterator();
        }
        return longIterator.next();
    }
}
