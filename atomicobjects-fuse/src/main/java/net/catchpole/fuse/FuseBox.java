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

import net.catchpole.fuse.criteria.Criteria;
import net.catchpole.fuse.fuses.AnnotationFuseBuilder;
import net.catchpole.lang.FilterIterable;
import net.catchpole.lang.Target;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FuseBox implements Iterable<Fuse> {
    private final List<Fuse> fuses = new ArrayList<Fuse>();
    private final AnnotationFuseBuilder annotationFuseBuilder = new AnnotationFuseBuilder(new Target<Fuse>() {
        public void set(Fuse value) {
            addFuse(value);
        }
    });

    public void addFuse(final Fuse fuse) {
        fuses.add(fuse);
    }

    public void addObject(final Object object) {
        this.annotationFuseBuilder.addObject(object);
    }

    public void addClass(final Class clazz) {
        this.annotationFuseBuilder.addClass(clazz);
    }

    public int total() {
        return this.fuses.size();
    }

    public Iterable<Fuse> findByInputCriteria(final Criteria inputCriteria) {
        return new FilterIterable<Fuse>(fuses) {
            public boolean include(Fuse fuse) {
                return fuse.getInputCriteria().isAssignableTo(inputCriteria);
            }
        };
    }

    public Iterable<Fuse> findByOutputCriteria(final Criteria outputCriteria) {
        return new FilterIterable<Fuse>(
                this.fuses) {
            public boolean include(final Fuse fuse) {
                return fuse.getOutputCriteria().isAssignableFrom(outputCriteria);
            }
        };
    }

    public Iterator<Fuse> iterator() {
        return this.fuses.iterator();
    }
}
