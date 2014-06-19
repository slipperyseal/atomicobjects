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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ValueModel implements Model {
    private String name;
    private List values = new ArrayList();
    private List<Model> children = new ArrayList<Model>();
    private Map<String,Object> attributes;

    public ValueModel(String name, Object value) {
        this.name = name;
        this.values.add(value);
    }

    public String getName() {
        return name;
    }

    public Iterator getValues() {
        return values.iterator();
    }

    public Map<String,Object> getAttributes() {
        return attributes;
    }

    public Class getType() {
        return values.size() != 0 ? null : values.iterator().next().getClass();
    }

    public Iterator<Model> iterator() {
        return children.iterator();
    }

    public void addValue(Object value) {
        this.values.add(value);
    }

    public void addChild(Model tag) {
        if (children == null) {
            children = new ArrayList<Model>();
        }
        children.add(tag);
    }

    public void removeChild(ValueModel tag) {
        if (children != null) {
            children.remove(tag);
        }
    }

    public void setAttribute(String name, Object value) {
        if (attributes == null) {
            attributes = new HashMap<String,Object>();
        }
        attributes.put(name, value);
    }

    public String toString() {
        return this.getClass().getSimpleName() + ' ' + name + ' ' + values.size() + " values " + children.size() + " children";
    }
}
