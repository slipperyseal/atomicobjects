package net.catchpole.search;

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

import net.catchpole.fuse.Fuse;
import net.catchpole.fuse.FuseBox;
import net.catchpole.fuse.criteria.Criteria;

import java.util.ArrayList;
import java.util.List;

public class SimpleSearch implements Search {
    private final FuseBox fuseBox;

    public SimpleSearch(FuseBox fuseBox) {
        this.fuseBox = fuseBox;
    }

    public List<Fuse> search(Criteria inputCriteria, Criteria outputCriteria) throws PlanNotFoundException {
        return new Searcher(inputCriteria, outputCriteria).getFuseList();
    }

    private class Searcher {
        private final Criteria firstInputCriteria;
        private final List<Fuse> fuseList = new ArrayList<Fuse>();

        public Searcher(Criteria inputCriteria, Criteria outputCriteria) throws PlanNotFoundException {
            this.firstInputCriteria = inputCriteria;
            if (!searchLoop(outputCriteria, 0)) {
                throw new PlanNotFoundException("can't find", inputCriteria, outputCriteria);
            }
        }

        private boolean searchLoop(Criteria resultCriteria, int depth) {
            // prevent infinite loops
            if (depth > fuseBox.total()) {
                return false;
            }
            for (Fuse fuse : fuseBox.findByOutputCriteria(resultCriteria)) {
                if (fuse.getInputCriteria().isAssignableTo(firstInputCriteria) ||
                        searchLoop(fuse.getInputCriteria(), depth + 1)) {
                    fuseList.add(fuse);
                    return true;
                }
            }
            return false;
        }

        public List<Fuse> getFuseList() {
            return fuseList;
        }
    }
}
