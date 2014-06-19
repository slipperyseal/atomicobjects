package net.catchpole.web.site.model.cart;

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

import java.util.*;

public class Catalog {
    private Map<String,List<Item>> items = new TreeMap<String,List<Item>>();
    private Map<String,Item> itemsSku = new HashMap<String,Item>();

    public Catalog() {
    }

    public Item getSku(String sku) {
        return itemsSku.get(sku);
    }

    public Map<String,List<Item>> getItems() {
        return items;
    }

    public void addItem(Item item) {
        List<Item> itemList = items.get(item.getCategory());
        if (itemList == null) {
            itemList = new ArrayList<Item>();
            items.put(item.getCategory(), itemList);
        }
        itemList.add(item);
        itemsSku.put(item.getSku(), item);
    }

    public Iterable<Item> getItems(String category) {
        return items.get(category);
    }

    public Iterable<String> getCategories() {
        return items.keySet();
    }
}
