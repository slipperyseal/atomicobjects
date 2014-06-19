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
import net.catchpole.fuse.annotation.Input;
import net.catchpole.fuse.annotation.Output;
import net.catchpole.fuse.annotation.Transform;
import net.catchpole.lang.Target;
import net.catchpole.lang.Throw;
import net.catchpole.lang.Transformation;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationFuseBuilder {
    private Target<Fuse> target;

    public AnnotationFuseBuilder(Target<Fuse> mapTarget) {
        this.target = mapTarget;
    }

    public void addObject(final Object object) {
        try {
            Class clazz = object.getClass();
            if (((AnnotatedElement) clazz).getAnnotation(net.catchpole.fuse.annotation.Fuse.class) == null) {
                throw new IllegalArgumentException(clazz.getName());
            }

            for (Method method : clazz.getMethods()) {
                Transform transform = method.getAnnotation(Transform.class);
                if (transform != null) {
                    target.set(new TransformFuse(
                            (Transformation) object,
                            method.getParameterTypes()[0],
                            method.getReturnType()));
                }
            }
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }

    public void addClass(final Class clazz) {
        try {
            net.catchpole.fuse.annotation.Fuse fuse =
                    ((AnnotatedElement) clazz).getAnnotation(net.catchpole.fuse.annotation.Fuse.class);
            if (fuse == null) {
                throw new IllegalArgumentException(clazz.getName());
            }

            final Class signatureClass = fuse.classname().length() == 0 ? clazz : Class.forName(fuse.classname());

            for (Method method : clazz.getMethods()) {
                Transform transform = method.getAnnotation(Transform.class);
                if (transform != null) {
                    target.set(new NewInstanceMethodFuse(method.getReturnType(), signatureClass, method.getName(),
                            method.getParameterTypes()));
                }

                Output output = method.getAnnotation(Output.class);
                if (output != null) {
                    target.set(new MethodFuse(method.getReturnType(), signatureClass, method.getName()));
                }
            }

            for (Constructor constructor : clazz.getConstructors()) {
                Input input = ((AnnotatedElement) constructor).getAnnotation(Input.class);
                if (input != null) {
                    target.set(new ConstructorFuse(signatureClass, constructor.getParameterTypes()));
                }
            }
        } catch (Exception e) {
            throw Throw.unchecked(e);
        }
    }
}
