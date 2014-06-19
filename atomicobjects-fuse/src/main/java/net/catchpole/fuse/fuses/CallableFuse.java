package net.catchpole.fuse.fuses;

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
import net.catchpole.fuse.criteria.Criteria;
import net.catchpole.robot.Implementor;
import net.catchpole.robot.generator.Callable;
import net.catchpole.robot.generator.DelegateGenerator;
import net.catchpole.trace.Core;

public class CallableFuse implements Fuse {
    private final Criteria inputCriteria;
    private final Criteria outputCriteria;
    private final Class resultImplementation;

    public CallableFuse(Implementor implementor, Criteria inputCriteria, Class resultType) {
        this.inputCriteria = inputCriteria;
        this.outputCriteria = new Criteria(resultType);

        DelegateGenerator delegateGenerator = new DelegateGenerator(Callable.class);
        this.resultImplementation = implementor.implement(delegateGenerator, resultType);
    }

    public Criteria getInputCriteria() {
        return inputCriteria;
    }

    public Criteria getOutputCriteria() {
        return outputCriteria;
    }

    public Object[] involk(Object[] params) throws Exception {
        Object result = resultImplementation.getConstructor(Callable.class).newInstance(new Callable() {
            public Object call(String methodName, Object[] parameters) {
                Core.getTrace().info(methodName, parameters);
                return methodName;
            }
        });

        return new Object[] { result };
    }
}
