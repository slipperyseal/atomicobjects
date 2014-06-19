package net.catchpole.model.search;

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

import net.catchpole.model.Model;
import net.catchpole.trace.Core;

public class PathSearch {
    private Model model;

    public PathSearch(Model model) {
        this.model = model;
    }

    public Model find(String[] list) {
        return find(list, 0);
    }

    public Model find(String[] list, int index) {
        Core.getTrace().info(model.getName(), list, index);
        if (index >= list.length) {
            return model;
        }
        return search(this.model, list, index);
    }

    private Model search(Model model, String[] list, int index) {
        Core.getTrace().info(model.getName(), list, index);
        for (Model subModel : model) {
            if (list[index].equals(subModel.getName())) {
                if (index == list.length - 1) {
                    return subModel;
                } else if (index < list.length) {
                    return search(subModel, list, index + 1);
                }
            }
        }
        return null;
    }
}
