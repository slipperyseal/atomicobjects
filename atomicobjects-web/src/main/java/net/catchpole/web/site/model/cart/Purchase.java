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

import java.util.Map;
import java.util.TreeMap;

public class Purchase {
    private Item item;
    private int quantity = 1;
    private Map<String,Option> options = new TreeMap<String, Option>();

    public Purchase(Item item) {
        this.item = item;
    }

    public void addOption(Option option) {
        this.options.put(option.getCategory(), option);
    }

    public Iterable<Option> getOptions() {
        return options.values();
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return item.getPrice() * quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Purchase{ " + getOptionSKU() +
                " item=" + item +
                ", quantity=" + quantity +
                ", options=" + options +
                '}';
    }

    public String getOptionSKU() {
        StringBuffer sb = new StringBuffer();
        for (Option option : options.values()) {
            if (sb.length() == 0) {
                sb.append('-');
            }
            sb.append(option.getCode());
        }
        return item.getSku() + sb.toString();
    }

    public boolean mergablePurchase(Purchase purchase) {
        if (item != null ? !item.getSku().equals(purchase.item.getSku()) : purchase.item != null) return false;
        if (options != null ? !options.equals(purchase.options) : purchase.options != null) return false;

        return true;
    }
}
