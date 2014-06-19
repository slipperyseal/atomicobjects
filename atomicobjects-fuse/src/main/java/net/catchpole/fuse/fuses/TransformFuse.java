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
import net.catchpole.lang.Transformation;
import net.catchpole.lang.reflect.Reflection;

public class TransformFuse implements Fuse {
    private final Transformation transformation;
    private final Criteria inputCriteria;
    private final Criteria outputCriteria;

    public TransformFuse(Transformation transformation) {
        this.transformation = transformation;
        Class[] types = Reflection.getParameterTypes(transformation.getClass(), Transformation.class);
        this.inputCriteria = new Criteria(types[0]);
        this.outputCriteria = new Criteria(types[1]);
    }

    public TransformFuse(Transformation transformation, Class source, Class result) {
        this.transformation = transformation;
        this.inputCriteria = new Criteria(source);
        this.outputCriteria = new Criteria(result);
    }

    public Criteria getOutputCriteria() {
        return outputCriteria;
    }

    public Criteria getInputCriteria() {
        return inputCriteria;
    }

    public Object[] involk(Object[] params) throws Exception {
        return new Object[]{transformation.transform(params[0])};
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + transformation;
    }
}
