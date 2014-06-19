package net.catchpole.fuse;

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

import net.catchpole.lang.Throw;

import java.util.Iterator;
import java.util.List;

public class Plan {
    private final List<Fuse> fuseList;

    public Plan(List<Fuse> fuseList) {
        this.fuseList = fuseList;
    }

    public Object[] involk(Object[] parameterArray) throws Exception {
        Object[] next = parameterArray;
        for (Fuse fuse : fuseList) {
            next = fuse.involk(next);
        }
        return next;
    }

    public Iterable<Fuse> getFuseList() {
        return fuseList;
    }

    public Iterable getInvolkIterable(final Object[] parameterArray) {
        return new Iterable() {
            public Iterator iterator() {
                return new Iterator() {
                    private final Iterator<Fuse> iterator = fuseList.iterator();
                    private Object[] next = parameterArray;

                    public boolean hasNext() {
                        return iterator.hasNext();
                    }

                    public Object next() {
                        try {
                            Fuse fuse = iterator.next();
                            return (next = fuse.involk(next))[0];
                        } catch (Exception e) {
                            throw Throw.unchecked(e);
                        }
                    }

                    public void remove() {
                    }
                };
            }
        };
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + fuseList.size() + " fuses";
    }
}
