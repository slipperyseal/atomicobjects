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
import net.catchpole.fuse.signature.ByteArrayInputStreamSignature;
import net.catchpole.fuse.signature.RandomAccessArraySignature;
import net.catchpole.fuse.signature.RenderedImageWriterSignature;
import net.catchpole.fuse.signature.StringSignature;
import net.catchpole.search.PlanNotFoundException;
import net.catchpole.search.Search;
import net.catchpole.search.SimpleSearch;

import java.util.Iterator;

public class JunctionBox implements Junction {
    private FuseBox fuseBox = new FuseBox();
    private Search search = new SimpleSearch(fuseBox);

    public JunctionBox() {
        fuseBox.addClass(ByteArrayInputStreamSignature.class);
        fuseBox.addClass(StringSignature.class);
        fuseBox.addClass(RenderedImageWriterSignature.class);
        fuseBox.addClass(RandomAccessArraySignature.class);
    }

    public Plan findPlan(Criteria inputCriteria, Criteria outputCriteria) throws PlanNotFoundException {
        return new Plan(search.search(inputCriteria, outputCriteria));
    }

    public void dispose() {
    }

    public Iterator iterator() {
        return null;
    }
}
