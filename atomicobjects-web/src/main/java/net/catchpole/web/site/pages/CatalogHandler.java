package net.catchpole.web.site.pages;

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

import net.catchpole.web.site.model.Backing;
import net.catchpole.web.site.model.cart.Catalog;
import net.catchpole.web.site.model.cart.Item;

public class CatalogHandler implements PathHandler {
    public boolean handle(Backing backing, String[] path) {
        if (path.length < 3) {
            return false;
        }

        Catalog catalog = backing.getCatalog();
        if (catalog.getItems().containsKey(path[1])) {
            Item item = catalog.getSku(path[2]);
            if (item != null) {
                backing.setSelectedItem(item);
            }
        }
        return true;
    }
}
