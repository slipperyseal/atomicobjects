package net.catchpole.dom.builder;

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

import net.catchpole.dom.ElementBuilder;
import net.catchpole.lang.MapTarget;
import net.catchpole.model.Model;

import java.util.Iterator;

public class XsdBuilder implements ElementBuilder {
    public String getName(Model model) {
        String name = model.getName();
        if (name == null) {
            return model.getType().getSimpleName();
        }
        return name.replace(' ', '_');
    }

    public void setAttributes(Model model, MapTarget<String, String> setter) {
        if (model.getType() != null && !model.getType().getName().startsWith("java.")) {
            setter.set("xsd-type", model.getType().getSimpleName());
        }
    }

    public String getText(Model model) {
        // only end nodes
        if (!model.iterator().hasNext()) {
            Iterator i = model.getValues();
            if (i != null) {
                while (i.hasNext()) {
                    Object value = i.next();
                    if (value != null) { // && (value instanceof String || !value.getClass().getName().startsWith("java."))) {
                        String text = value.toString();
                        if (text.length() > 0) {
                            return text;
                        }
                    }
                }
            }
        }
        return null;
    }
}
