package net.catchpole.model;

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

import net.catchpole.model.iterator.ModelIteratorResolver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BeanModel implements Model {
    private static BeanDecoder beanDecoder = new BeanDecoder();

    private String name;
    private Object bean;

    public BeanModel(Object bean) {
        this.bean = bean;
    }

    public BeanModel(String name, Object bean) {
        this.name = name;
        this.bean = bean;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return bean == null ? null : bean.getClass();
    }

    public Iterator getValues() {
        List list = new ArrayList();
        list.add(bean);
        return list.iterator();
    }

    public Iterator<Model> iterator() {
        if (bean instanceof Iterable) {
            return new ModelIteratorResolver<Object>(((Iterable) bean).iterator()) {
                public Model resolve(Object key) {
                    return new BeanModel(key);
                }
            };
        }

        final Map map = beanDecoder.decode(bean);

        return new ModelIteratorResolver<Object>(map.keySet().iterator()) {
            public Model resolve(Object key) {
                return new BeanModel(key.toString(), map.get(key));
            }
        };
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + name + ' ' + bean.getClass().getName();
    }
}
