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

import java.lang.reflect.Method;

public class NewInstanceMethodFuse implements Fuse {
    private final Method method;
    private final Criteria inputCriteria;
    private final Criteria outputCriteria;

    public NewInstanceMethodFuse(Class resultClass, Class methodClass, String methodName, Class[] parameterClasses) throws Exception {
        this.method = methodClass.getMethod(methodName, parameterClasses);
        this.outputCriteria = new Criteria(resultClass);
        this.inputCriteria = new Criteria(parameterClasses);
    }

    public Criteria getOutputCriteria() {
        return outputCriteria;
    }

    public Criteria getInputCriteria() {
        return inputCriteria;
    }

    public Object[] involk(Object[] params) throws Exception {
        Object object = method.getDeclaringClass().newInstance();
        return new Object[]{method.invoke(object, params)};
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + method + ' ' + outputCriteria + '=' + inputCriteria;
    }

}
