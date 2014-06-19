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

import net.catchpole.fuse.criteria.Criteria;

//   Copyright 2010 catchpole.net
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

public class PlanNotFoundException extends Exception {
    private final Criteria inputCriteria;
    private final Criteria outputCriteria;

    public PlanNotFoundException(String message, Criteria inputCriteria, Criteria outputCriteria) {
        super(message);
        this.inputCriteria = inputCriteria;
        this.outputCriteria = outputCriteria;
    }

    public Criteria getInputCriteria() {
        return inputCriteria;
    }

    public Criteria getOutputCriteria() {
        return outputCriteria;
    }

    public String toString() {
        return getMessage() + ' ' + inputCriteria + " > " + outputCriteria;
    }
}
