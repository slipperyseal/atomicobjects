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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Item {
    private String sku;
    private String category;
    private String name;
    private int price;
    private String description;
    private Map<String,List<Option>> options = new TreeMap<String,List<Option>>();

    public Item(String sku, String category, String name, int price, String description) {
        this.sku = sku;
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public void addOption(Option option) {
        List<Option> optionList = options.get(option.getCategory());
        if (optionList == null) {
            optionList = new ArrayList<Option>();
            options.put(option.getCategory(), optionList);
        }
        optionList.add(option);
    }

    public Iterable<Option> getOptions(String category) {
        return options.get(category);
    }

    public Iterable<String> getCategories() {
        return options.keySet();
    }

    public Map<String, List<Option>> getOptions() {
        return options;
    }

    @Override
    public String toString() {
        return "Item[" +
                "sku='" + sku + '\'' +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ']';
    }
}
